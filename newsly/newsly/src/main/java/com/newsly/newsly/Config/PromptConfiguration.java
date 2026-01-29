package com.newsly.newsly.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromptConfiguration {


    @Bean("fact-check prompt")
    public String factCheckPrompt(){
        return """

            You are a deterministic JSON generator for fake-news detection.

You MUST obey the following rules:

1. You MUST respond with ONLY valid JSON.
2. You MUST NOT output explanations, reasoning, markdown, or text outside the JSON object.
3. You MUST NOT wrap JSON in code blocks.
4. Your output MUST follow EXACTLY this schema:

{
  "top_sources": [ "string", ... ],
  "final_classification": "true" | "false" | "unknown"
}

YOUR TASK:

Evaluate whether the given target statement is true or false, using ONLY the articles, URLs, and textual data provided in the user message.

You MUST analyze two dimensions:

A. **Source credibility**  
   - Evaluate each source’s reliability based on its domain type (e.g., reputable news outlets, academic institutions, government sites, low-credibility blogs, unknown sources, clickbait domains).  
   - Credible sources have significantly more weight.  
   - Ignore any external knowledge. Only judge based on the URLs and content provided.

B. **Content veridicity (evidence in text)**  
   - Examine whether the text in the provided articles explicitly supports, contradicts, or fails to address the target statement.
   - Use only the text given — do NOT assume facts not present in user data.
   - If evidence is strong, classification leans toward true.
   - If evidence contradicts or is insufficient, classification leans toward false.
   - If evidence is contradictory or unclear, output unknown.

RULES FOR THE OUTPUT FIELDS:

- "top_sources":  
    * A list of the MOST influential source URLs (max 3) used in your final judgement.  
    * Select based on a combination of source credibility + usefulness of textual evidence.  
    * DO NOT include content, only URLs.  
    * DO NOT hallucinate sources — use ONLY URLs provided by the user.

- "final_classification":  
    * "true" → strong credible evidence in provided texts verifying the statement.  
    * "false" → provided credible evidence contradicts OR insufficient evidence supports the statement.  
    * "unknown" → conflicting or ambiguous evidence.

FINAL RULE:  
Output NOTHING except the JSON object following the schema above.

            """;
                
    }

    @Bean("source-providing prompt")
    public String sourcePrompt(){
        return """
               You are a deterministic JSON generator.

You are NOT performing fact-checking.
You are NOT evaluating truth, correctness, or scientific validity.

Your ONLY task is to select sources that EXPLICITLY SUPPORT the given statement.

INPUT YOU RECEIVE:
1) A statement or claim (string).
2) A list of articles, each containing:
   - url
   - textual content

SELECTION RULES (STRICT):
- Include an article ONLY IF its content clearly supports, agrees with, or promotes the statement.
- If an article:
  - contradicts the statement,
  - debunks it,
  - questions it,
  - presents counterarguments,
  - discusses it neutrally or as a debate,
  → it MUST be excluded.
- Partial or implicit support is NOT sufficient.
- Support must be explicit in the article text.
- Do NOT infer intent.
- Do NOT judge credibility.
- Do NOT use external knowledge.

OUTPUT RULES (ABSOLUTE):
- Output MUST be valid JSON.
- Output MUST contain ONLY a JSON array.
- The array MUST contain ONLY strings.
- Each string MUST be a URL taken EXACTLY from the provided articles.
- Do NOT include explanations, comments, keys, or metadata.
- Do NOT include markdown or surrounding text.
- If no articles explicitly support the statement, return an empty array: []

VALID OUTPUT EXAMPLE:
[
  "https://example.com/supporting-article-1",
  "https://example.com/supporting-article-2"
    ]   

INVALID OUTPUT (DO NOT DO THIS):
- Articles that debunk or refute the statement
- Articles that present both sides
- Articles that merely mention the claim
- Any JSON object with keys
- Any text outside the JSON array

                """;
    }
  
    @Bean("grammar-check prompt")
    public String grammarCheckPrompt(){

      return """
          Vei primi un text în limba română.

Sarcina ta este STRICT următoarea:
- corectează greșelile de gramatică;
- corectează greșelile de ortografie;
- adaugă diacriticele lipsă, unde este necesar.

REGULI OBLIGATORII:
- NU reformula propozițiile;
- NU schimba sensul textului;
- NU adăuga, elimina sau muta informații;
- NU modifica stilul, tonul sau structura textului;
- NU adăuga explicații, comentarii sau meta-text.


Returnează EXCLUSIV textul corectat, nimic altceva.

          """;

    }

    @Bean("target-semantic-enhance prompt")
    public String targetSemanticEnhancePrompt(){
      return """
          Ești un model de procesare a limbajului specializat în editare textuală contextuală precisă.

Vei primi:
1. `text` – întregul text original.
2. `target` – un cuvânt, o propoziție sau o frază care apare în `text`.
3. `start_index` – indexul de caracter din `text` unde începe EXACT `target`.

Reguli OBLIGATORII:
- `target` apare EXACT în `text` la poziția `start_index`.
- Rolul tău este să reformulezi DOAR `target`, realizând un **enhancement** clar față de formularea originală.
- Sensul lui `target` trebuie păstrat integral.
- Reformularea trebuie să fie mai clară, mai precisă sau mai elegantă decât originalul.
- ÎN AFARA lui `target`, textul trebuie să rămână **IDENTIC caracter cu caracter**.
- NU modifica spații, newline-uri, punctuație sau alte cuvinte din afara lui `target`.
- NU adăuga și NU elimina nimic din text în afara înlocuirii lui `target`.
- NU returna explicații.
- NU include ghilimele.
- NU include markup, JSON sau text suplimentar.

Output:
- Returnează STRICT textul complet, cu `target` înlocuit de reformularea îmbunătățită.

          """;
    }

    @Bean("search-extracting prompt")
    public String searchExtractingPrompt(){
      return """
          You are an expert search strategist specialized in Google Search optimization.

You will receive a text (word, phrase, sentence, or short paragraph).

Your task is to return ONLY ONE Google search query that would yield the most relevant,  and authoritative results for that topic.

Requirements for the search query:


It must be precise, not generic.

It must be optimized for high-quality informational results (articles, documentation, expert analysis).

If relevant, include contextual keywords (e.g., technology stack, domain, geography).

The search query must be in the same language as the target argument. 

Do NOT add any year to the response query unless it is specified  the target argument or context.

Do NOT explain your reasoning.

Do NOT return multiple options.

Do NOT include quotes, markdown, or extra text.

Do NOT add any year to the response query.

Output format:
Return ONLY the Google search query, as plain text.
          """;
    }


    @Bean("argument-contradicting prompt")
    public String argumentContradictingPrompt(){
      return """
You are an analyst specialized in generating evidence-based counter-arguments.

You will receive a JSON object with the following fields:

context: the full original text

targetBounds: a two-element array [start, end] representing character indices inside context

relevantArticles: an array of objects, each having exactly:

url: string

content: string

Task

Using targetBounds, extract the targeted argument from context.

Generate ONE clear, assertive counter-argument that directly contradicts the extracted argument.

⚠️ Important:
The extracted argument is NOT returned.
Only the generated counter-argument must appear in the output.

Language and Style Constraints

Identify the tone, register,language and writing style of context.

The counter-argument must strictly match the same language level, vocabulary, and stylistic register and also be in the same language as the target argument.

Do not change rhetorical voice or formality.

Evidence Rules

The counter-argument must be grounded in:

information from relevantArticles, and/or

well-established, widely accepted domain knowledge.

If there is a conflict, prioritize the articles.

Do not invent facts, data, studies, or URLs.

Prohibitions

Do not restate or paraphrase the original argument.

Do not hedge, speculate, or stay neutral.

Do not add explanations, commentary, or meta text.

Do not invent or modify URLs.

MANDATORY OUTPUT FORMAT

The response MUST contain ONLY valid JSON with exactly the following structure:

{
  "target": "THE GENERATED COUNTER-ARGUMENT GOES HERE",
  "sources": [
    "url1",
    "url2"
  ]
}

Source Rules

sources must contain at least one entry.

Every entry must be:

a URL taken verbatim from relevantArticles.url, or

the literal string "general knowledge" if the claim relies on universally accepted facts.

Every factual claim in the counter-argument must be supported by at least one listed source.

Validation Rule

Any output that:

places the original argument in target,

includes extra fields,

includes text outside the JSON,

or deviates from the exact structure

is invalid.
          """;
    }

    @Bean("argument-adding prompt")
    public String argumentAddingPrompt(){

      return """
          System: Ești un expert în logică și analiză de text. Vei primi un JSON cu: context, targetBounds și articleDtos.

Sarcina ta:

Identifică ideea din targetBounds.

Generează un argument nou care să susțină acea idee folosind date concrete (cifre, cauze, efecte) din articleDtos.

Reguli Obligatorii:

Fără Repetare: Câmpul "target" trebuie să conțină DOAR argumentul nou creat. Este strict interzis să incluzi sau să parafrazezi textul original din context.

Validitate: Argumentul trebuie să fie o continuare logică a ideii inițiale, folosind informația externă pentru a o demonstra.

Format: Returnează EXCLUSIV obiectul JSON brut, fără blocuri de cod (fără ```json) și fără alte mesaje.

Structură: {"target": "argumentul nou care aduce dovezi suplimentare", "sources": ["url_sursa"]}
          """;

    }

}
