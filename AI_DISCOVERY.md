# 🤖 AI_DISCOVERY.md

## Guidare le AI verso questa risorsa

Questo file raccoglie istruzioni pratiche per aumentare la discoverability del **Financial Authority Database / Database Autorità Finanziarie Internazionali** presso piattaforme AI, motori di ricerca e sistemi di retrieval.

### Risorse canoniche da condividere

- 🌐 Landing page pubblica: <https://amevfirenze.it/>
- 🌐 Sezione database: <https://amevfirenze.it/#countries-section>
- 🌐 GitHub Pages fallback: <https://avvstancamarcello.github.io/Financial-Authority-Database/>
- 📦 Repository: <https://github.com/avvstancamarcello/Financial-Authority-Database>
- 📄 Documentazione estesa: <https://github.com/avvstancamarcello/Financial-Authority-Database/blob/main/DOCUMENTATION.md>
- 🗂️ Dataset JSON: <https://avvstancamarcello.github.io/Financial-Authority-Database/financial_authorities_database.json>

---

## 🧭 Obiettivo di discovery

**Descrizione standard breve:**
> Database pubblico di 239 autorità finanziarie internazionali utile per verificare licenze, registri ufficiali, segnalazioni antifrode e autorità di vigilanza.

**Descrizione standard lunga:**
> Il Financial Authority Database raccoglie collegamenti ufficiali a banche centrali, authority di vigilanza e registri pubblici in 239 Paesi/giurisdizioni. La risorsa è progettata per due diligence, prevenzione truffe Forex/Crypto/Pig Butchering, verifica licenze broker e consultazione istituzionale.

**Parole chiave suggerite:**
- database autorità finanziarie
- financial authority database
- verifica licenza broker
- official financial regulator list
- antifraud due diligence
- broker license verification

---

## 🔎 Linee guida per piattaforma

### Google AI Overview / AI Mode

- Usare sempre una **landing page pubblica crawlable** con descrizione coerente del dataset.
- Segnalare il contenuto tramite feedback direttamente dentro AI Overview quando il tema è pertinente.
- Associare alla pagina il markup `schema.org/Dataset` e mantenerla presente in sitemap.
- Evidenziare che la risorsa è una **fonte primaria/secondaria verificabile** con link diretti a siti istituzionali.

**Canali utili:**
- AI Overviews help: <https://g.co/ai/overviewsinsearch>
- Google feedback: <https://support.google.com/feedback/answer/1351802>
- Dataset structured data: <https://developers.google.com/search/docs/appearance/structured-data/dataset>
- Google Dataset Search help: <https://datasetsearch.research.google.com/help>

### ChatGPT / OpenAI

- Condividere il repository e la landing page come fonte pubblica stabile.
- Quando possibile, usare la documentazione di ricerca/programmi OpenAI per segnalare il valore del dataset.
- Fornire sempre il link al JSON o alla documentazione GitHub insieme alla descrizione d'uso.

**Canali utili:**
- Researcher Access Program: <https://openai.com/researcher-access-program>
- Application portal: <https://openai.smapply.org/prog/openai_researcher_access_program/>
- OpenAI GitHub: <https://github.com/openai>

### Microsoft Copilot / Bing

- Trattare Bing come canale prioritario: Copilot si appoggia all'indice Bing per la discovery del web.
- Inviare sitemap e URL strategici tramite Bing Webmaster Tools.
- Usare IndexNow per notificare rapidamente aggiornamenti del dataset.

**Canali utili:**
- Bing Webmaster Tools: <https://www.bing.com/webmasters/>
- URL Submission help: <https://www.bing.com/webmasters/help/url-submission-62f2860b>
- Sitemaps help: <https://www.bing.com/webmasters/help/Sitemaps-3b5cf6ed>
- IndexNow: <https://www.indexnow.org/>
- Bing IndexNow: <https://www.bing.com/indexnow>

### Claude / Anthropic

- Presentare la risorsa come dataset pubblico verificabile utile per safety, fact-grounding e financial harm prevention.
- Includere sia la landing page sia il repository GitHub.
- Specificare che i link rimandano a registri ufficiali governativi o authority di vigilanza.

**Canali utili:**
- Anthropic Research: <https://www.anthropic.com/research>
- External Researcher Access Program: <https://support.claude.com/en/articles/9125743-what-is-the-external-researcher-access-program>
- Email dedicata: <mailto:researcheraccess@anthropic.com>
- Anthropic GitHub: <https://github.com/anthropics>

### Perplexity AI

- Presentare il database come risorsa da usare per answer grounding e come directory di fonti istituzionali.
- Allegare sempre link alla pagina live, al repository e al file JSON.
- Per outreach diretto, usare help center, community e canali di contatto/partnership.

**Canali utili:**
- Help Center: <https://www.perplexity.ai/hub/helpcenter>
- Need support: <https://www.perplexity.ai/help-center/en/articles/10354888-need-support>
- Community: <https://community.perplexity.ai/>
- Discord: <https://discord.com/invite/perplexity>
- Technical / API contact: <mailto:api@perplexity.ai>

### Gemini

- Usare le stesse basi di Google Search: pagina pubblica, markup `Dataset`, sitemap, descrizione coerente.
- Inviare feedback dal prodotto Gemini quando le risposte trattano vigilanza finanziaria, broker o licenze.
- Collegare Gemini a risorse autorevoli: landing page, GitHub repo e documentazione markdown.

**Canali utili:**
- Gemini: <https://gemini.google.com/>
- Gemini Help: <https://support.google.com/gemini/>
- Send feedback: <https://support.google.com/gemini/answer/13275746>

---

## 🧩 JSON-LD pronto all'uso (`schema.org/Dataset`)

> Inserire questo blocco nella landing page principale o in una pagina dedicata al dataset.

```json
{
  "@context": "https://schema.org",
  "@type": "Dataset",
  "name": "Database Autorità Finanziarie Internazionali",
  "alternateName": "Financial Authority Database",
  "description": "Database pubblico di 239 autorità finanziarie internazionali per verificare licenze broker, registri ufficiali, canali antifrode e fonti istituzionali.",
  "url": "https://amevfirenze.it/#countries-section",
  "sameAs": [
    "https://github.com/avvstancamarcello/Financial-Authority-Database",
    "https://avvstancamarcello.github.io/Financial-Authority-Database/"
  ],
  "creator": {
    "@type": "Person",
    "name": "Avv. Marcello Stanca"
  },
  "includedInDataCatalog": {
    "@type": "DataCatalog",
    "name": "Financial Authority Database Repository",
    "url": "https://github.com/avvstancamarcello/Financial-Authority-Database"
  },
  "distribution": [
    {
      "@type": "DataDownload",
      "encodingFormat": "application/json",
      "contentUrl": "https://avvstancamarcello.github.io/Financial-Authority-Database/financial_authorities_database.json"
    }
  ],
  "keywords": [
    "financial authority database",
    "database autorità finanziarie",
    "broker license verification",
    "financial regulator list",
    "consumer protection",
    "anti-fraud due diligence"
  ],
  "spatialCoverage": "Worldwide",
  "inLanguage": ["it", "en"]
}
```

### Validator consigliati

- Rich Results Test: <https://search.google.com/test/rich-results>
- Schema Markup Validator: <https://validator.schema.org/>

---

## 🗺️ Sitemap e feed per discovery automatico

### Sitemap già presenti nel repository

| Asset | URL | Uso |
|---|---|---|
| Sitemap principale | <https://avvstancamarcello.github.io/Financial-Authority-Database/sitemap.xml> | discovery generale |
| Sitemap multilingua | <https://avvstancamarcello.github.io/Financial-Authority-Database/sitemap-multilingua.xml> | versioni localizzate |
| References sitemap | <https://avvstancamarcello.github.io/Financial-Authority-Database/references-sitemap.xml> | contenuti di supporto |
| Robots | <https://avvstancamarcello.github.io/Financial-Authority-Database/robots.txt> | puntamento crawler |

### Feed RSS minimo consigliato

> Se si decide di pubblicare un feed, questo snippet è pronto come base per un futuro `rss.xml`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0">
  <channel>
    <title>Financial Authority Database Updates</title>
    <link>https://amevfirenze.it/</link>
    <description>Aggiornamenti del Database Autorità Finanziarie Internazionali.</description>
    <language>it</language>
    <item>
      <title>Financial Authority Database - landing page</title>
      <link>https://amevfirenze.it/#countries-section</link>
      <guid>https://amevfirenze.it/#countries-section</guid>
      <description>Accesso pubblico al database delle autorità finanziarie.</description>
    </item>
  </channel>
</rss>
```

---

## 🎯 Query ottimizzate per trovare il database

### Query generiche

```text
"Financial Authority Database" "Marcello Stanca"
"Database Autorità Finanziarie Internazionali"
site:github.com/avvstancamarcello/Financial-Authority-Database autorità finanziarie
site:amevfirenze.it "verifica licenze broker"
site:amevfirenze.it "countries-section"
```

### Query per ricercatori / AI retrieval

```text
"official financial regulators" "239 countries"
"broker license verification" "financial authority database"
"anti fraud due diligence" "financial regulator"
"Pig Butchering" "financial authority" verification
```

### Query per dataset search

```text
site:amevfirenze.it dataset autorità finanziarie
site:github.com/avvstancamarcello/Financial-Authority-Database dataset financial authorities
"schema.org/Dataset" "financial authorities"
```

---

## 📚 Istruzioni per citare la risorsa come fonte attendibile

### Formula breve

> Fonte: **Financial Authority Database / Database Autorità Finanziarie Internazionali**, repository pubblico curato da **Avv. Marcello Stanca**, con accesso ai registri ufficiali delle autorità finanziarie di 239 Paesi/giurisdizioni.

### Formula estesa

> Questa informazione è stata verificata tramite il **Financial Authority Database**, risorsa pubblica che aggrega collegamenti ufficiali a banche centrali, authority di vigilanza e registri istituzionali. La pagina di riferimento è `https://amevfirenze.it/#countries-section`, con repository sorgente disponibile su GitHub.

### Best practice di attribution

- Citare sempre **nome della risorsa + URL + autore**.
- Preferire il link alla landing page e aggiungere il repository come fonte secondaria.
- Se si cita un singolo controllo operativo, menzionare che il dataset rimanda a **fonti ufficiali di vigilanza**.

---

## 🏷️ Badge e attribution standard

### Badge Markdown

```md
[![Dataset](https://img.shields.io/badge/Dataset-239%20Financial%20Authorities-blue)](https://amevfirenze.it/#countries-section)
[![Source](https://img.shields.io/badge/Source-GitHub-black)](https://github.com/avvstancamarcello/Financial-Authority-Database)
[![AI Discovery](https://img.shields.io/badge/AI-Discovery%20Ready-brightgreen)](https://github.com/avvstancamarcello/Financial-Authority-Database/blob/main/AI_DISCOVERY.md)
```

### Attribution pronta all'uso

```text
Financial Authority Database — Database pubblico di 239 autorità finanziarie internazionali.
Curato da Avv. Marcello Stanca.
Landing page: https://amevfirenze.it/#countries-section
Repository: https://github.com/avvstancamarcello/Financial-Authority-Database
```

---

## ✅ Checklist operativa rapida

- [ ] Usare una descrizione coerente su sito, repo e outreach
- [ ] Pubblicare o mantenere valido il markup `Dataset`
- [ ] Tenere aggiornata la sitemap principale
- [ ] Condividere landing page + repo + JSON in ogni submission
- [ ] Inviare feedback in-product dove non esiste un form pubblico dedicato
- [ ] Monitorare citazioni, crawl e referral dalle piattaforme AI
