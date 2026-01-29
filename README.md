# BDNVS-project

Demo:https://youtu.be/Fp9dOGanJnM

Diagrame: https://excalidraw.com/#json=6vFLCbGEnERwFk3rZPiZ1,R1yc7HwAlR4ZozHer10LHg

Documentație Arhitectură Caching Spring Boot
1. Design și Integrare
Aplicația integrează Redis pentru a îmbunătăți performanța pentru interogări frecvente, căutări semantice și operațiuni geospațiale. Arhitectura de caching interacționează cu MySQL/PostgreSQL (pentru date structurate) și utilizează Redis prin intermediul bibliotecii UnifiedJedis.
Seturi de Date Identificate pentru Caching
•	Articole: Metadate accesate frecvent și locație geospațială.
•	Documente (Editor): Conținut în timp real pentru editorul de text, gestionat via WebSocket/REST.
•	Fact Checks și Surse (Pipelines): Rezultate ale procesării AI și căutări semantice bazate pe vectori (embeddings).
Prezentare Generală a Arhitecturii
Sistemul utilizează o combinație de strategii de caching pentru optimizarea operațiunilor de citire/scriere:
1.	Nivel Aplicație: Controllerele Spring Boot (ArticleController, EditorController, EditorController2) și Pipeline-urile AI gestionează logica.
2.	Nivel Cache: Redis acționează ca un stocare rapidă de date și coadă de sincronizare.
3.	Nivel Persistență: Baze de date SQL/NoSQL stochează înregistrările permanente.
________________________________________
2. Strategii de Caching Implementate
Cache-Aside / Fallback (Articole)
•	Implementare: ArticleController.java.
•	Flux:
1.	Sistemul încearcă să preia articolul din baza de date primară (ArticleRepo).
2.	Dacă baza de date nu răspunde (excepție), se face fallback către Redis (RedisArticleRepo.getArticleById).
3.	Read Repair: Când un articol este procesat, acesta este salvat în Redis cu un TTL pentru acces rapid ulterior.
Snippet Cod (ArticleController.java):
Java
try {
    article = repo.getArticleById(id); // Încearcă
DB
} catch(Exception e) {
    log.info("baza de date nu
raspunde");
    article = cache.getArticleById(id); //
Fallback la Cache
}
 
// Salvare ulterioară în cache pentru optimizare
cache.saveWithKeyandTTL(feedkey, article, new Long(60*30));
Read-Through (Documente Editor)
•	Implementare: EditorController2.java.
•	Flux:
1.	Cerere pentru încărcarea unui document (loadContent).
2.	Se verifică Redis pentru conținutul documentului folosind cheia document:{id}.
3.	Hit: Returnează conținutul din Redis.
4.	Miss: Preia din DocumentRepo (DB), scrie în Redis, apoi returnează conținutul.
Snippet Cod (EditorController2.java):
Java
String key = "document:" + docId;
String content = jedis.get(key); // Verifică Cache
 
if (content == null) {
    Document doc = repo.findById(docId).get(); //
Preia din DB
    content = doc.getContent();
    jedis.set(key, content); // Populează Cache
}
return content;
Write-Behind / Sync Fallback (Fact Checks și Surse)
•	Implementare: Pipeline-urile din EditorController, FactCheckPersistFunctionRegistryConfig, SyncService.
•	Flux de Salvare (Persistență):
1.	Pipeline-ul de procesare (LookUpPipeline sau similar) încearcă să salveze rezultatul final.
2.	Se utilizează un FunctionRegistry care încearcă secvențial:
	Pasul 1: Salvare directă în DB (dbRepo::save).
	Pasul 2 (Fallback): Dacă DB eșuează (excepție), se apelează cacheRepo::saveInQueue.
3.	Coada Redis: Datele sunt serializate și adăugate într-o listă Redis (ex. sync_list:factcheck).
Snippet Cod (FactCheckPersistFunctionRegistryConfig.java):
Java
return
FunctionRegistry.<FactCheckResponse,FactCheckResponse>builder()
    .addConsumer(dbRepo::save, " ")
     // Încearcă salvare DB
    .addConsumer(cacheRepo::saveInQueue,"
") // Fallback: Salvare în coadă Redis
    .build();
•	Proces Asincron (SyncService):
1.	Un job programat (@Scheduled la fiecare 30s) procesează aceste liste.
2.	Batch Processing: Redenumește lista pentru procesare atomică, deserializează obiectele și le salvează în DB (saveAll).
3.	Dacă salvarea batch eșuează, datele sunt reintroduse în coadă.
Snippet Cod (SyncService.java):
Java
@Scheduled(fixedDelay = 30000)
public void syncDatabase() {
    // ...
    String syncKey =
"sync_list:factcheck";
    String tempKey =
"sync_list:factcheck:processing:" + System.currentTimeMillis();
    
    jedis.rename(syncKey, tempKey); // Mutare
atomică pentru procesare
    List<String> nodes =
jedis.lrange(tempKey, 0, -1);
    
    // Deserializare și salvare batch
    // ...
    factCheckRepo.saveAll(responses);
    jedis.del(tempKey);
}
Căutare Vectorială / Semantică (Fact Checks și Surse)
•	Implementare: ExtractSimilarResponsePipelineConfig, RedisFactCheckRepo, RedisSourceRepo.
•	Flux:
1.	Se generează embeddings (vectori de float) pentru conținut.
2.	Datele sunt stocate în Redis Hashes, iar vectorii sunt indexați.
3.	Pipeline-ul extractSimilarPipeline utilizează FT.SEARCH (RediSearch) pentru a efectua căutări KNN (K-Nearest Neighbors).
Snippet Cod (RedisFactCheckRepo.java):
Java
String queryExpression = "*=>[KNN 1 @embedding
$vector AS vector_score]";
Query q = new Query(queryExpression)
        .addParam("vector",
queryVector)
        .dialect(2) 
       
.returnFields("content", "value",
"vector_score");
 
SearchResult result =
jedis.ftSearch("idx:factcheck", q);
Caching Geospațial (Articole)
•	Implementare: ArticleController.java.
•	Flux:
1.	Locațiile articolelor sunt stocate folosind GEOADD în cheia article:location.
2.	Endpoint-ul geoFeed folosește GEOSEARCH pentru a găsi articole pe o rază de 50km.
Snippet Cod (ArticleController.java):
Java
// Adăugare locație
jedis.geoadd("article:location", latitude,
longitude, feedkey);
 
// Căutare pe rază
GeoSearchParam params = new GeoSearchParam()
    .fromLonLat(longitude, latitude)
    .byRadius(50, GeoUnit.KM);
List<GeoRadiusResponse> responses =
jedis.geosearch("article:location", params);
________________________________________
3. Detalii de Implementare și Funcționalități Core
Structuri de Date Redis Utilizate
Structură Date	Utilizare	Model Cheie	Referință Cod
String	Conținut Document, Versionare	document:{id}, doc:version:{id}	EditorController2
Hash	Entități (Articol, FactCheck, Sursă)	article:{id}, factcheck:{id}, source:{id}	RedisArticleRepo, RedisFactCheckRepo
List	Cozi Sincronizare, Link-uri	sync_list:*, factcheck:{id}:links	SyncService, FunctionRegistry
Sorted Set	Index Geospațial, Structură Doc	article:location, doc:structure:{id}	ArticleController
Vector (Redis Stack)	Embeddings pentru căutare	Stocat binar în Hash, indexat via RediSearch	RedisFactCheckRepo
Export to Sheets
TTL (Time-To-Live) și Eviction
Sistemul folosește TTL-uri explicite pentru a gestiona memoria:
Snippet Cod (RedisArticleRepo.java):
Java
jedis.expire(key, 60 * 30); // 30 minute
Invalidare Cache
•	Update: La actualizarea documentului (receiveUpdate), cheia Redis este suprascrisă (jedis.set).
•	Delete: La ștergerea unei entități, cheia este ștearsă explicit (jedis.del).
Snippet Cod (EditorController2.java):
Java
@DeleteMapping("/api/v1/delete-document/{id}")
public ResponseEntity deleteDocument(@PathVariable String
id){
    String cacheKey = "document:" + id;
    jedis.del(cacheKey); // Ștergere explicită din
cache
    repo.deleteById(id);
    return ResponseEntity.ok().build();
}
________________________________________



