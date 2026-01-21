from fastapi import FastAPI
from sentence_transformers import SentenceTransformer
from detoxify import Detoxify
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware
from fastapi import Response
import trafilatura
from deep_translator import GoogleTranslator
import logging
from transformers import pipeline

logger = logging.getLogger("fastapi")
logger.setLevel(logging.INFO)

logger.info("Aplicatia a pornit")

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000","http://localhost:8080"],
    allow_methods=["*"],
    allow_headers=["*"],
)


class SearchContent(BaseModel):
    content: str

class UrlBatch(BaseModel):
    batch: list
    
class ArticleDto(BaseModel):
    url: str
    content: str

class ArticleBatch(BaseModel):
    articles: list[ArticleDto]

embedding_model = SentenceTransformer('sentence-transformers/all-MiniLM-L6-v2')
tox_original = Detoxify("original")

nli = pipeline(
        "text-classification",
        model="facebook/bart-large-mnli"
    )

class EmbeddingResponse(BaseModel):
    embeddings:list[float]

@app.post("/api/py-service/text-embedding")
def embedding_service(data: SearchContent, res:Response):
    embeddings = embedding_model.encode(data.content)
    return EmbeddingResponse(embeddings=embeddings.tolist())



@app.post("/api/py-service/toxic-validation")
def validation_service(data: SearchContent, res:Response ):
    
    results= tox_original.predict(data.content)
    
    return normalize_result(results) 

@app.post("/api/py-service/extract-data-from-url")
def extractData(data:list[str], res:Response):
    
    articleList= []
    
    for url in data:
        
        logger.info(url)
        downloaded=trafilatura.fetch_url(url)
        data=trafilatura.extract(downloaded,output_format="txt")

        if data is not None:
        
            article=  ArticleDto(url=url,content=data)
        
            articleList.append(article)
        
    return ArticleBatch(articles=articleList)
    


        

@app.post("/api/py-service/translate")
def translate(data:SearchContent, res:Response):
    translated = GoogleTranslator(source='auto', target='en').translate(data.content)
    return translated

class ToxicityRaport(BaseModel):
    toxicity:float
    severe_toxicity:float
    obscene:float
    threat:float
    insult:float 
    identity_attack:float


def normalize_result(data):

    toxicity= float(data['toxicity'])
    severe_toxicity=float(data['severe_toxicity'])
    obscene= float(data['obscene'])
    threat=float(data['threat'])
    insult=float(data['insult'])
    identity_attack=float(data['identity_attack'])
    
    return ToxicityRaport(toxicity=toxicity
                        ,severe_toxicity=severe_toxicity
                        ,obscene=obscene
                        ,threat=threat
                        ,insult=insult
                        ,identity_attack=identity_attack)
    


class NliVerificationDto(BaseModel):
    
    premise:str
    hypothesis:str

@app.post('/api/py-service/nli-check')
def verify(data:NliVerificationDto):

    premise = data.premise
    hypothesis = data.hypothesis

    result = nli(f"{premise} </s></s> {hypothesis}")
    
    return result[0]['label']
    
    

