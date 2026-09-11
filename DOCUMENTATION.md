# 📘 DOCUMENTATION.md — Database Autorità Finanziarie Internazionali

> **Guida operativa ufficiale** del repository **Financial-Authority-Database**.

---

## 1) 🌍 Introduzione e Overview

Il **Database Autorità Finanziarie Internazionali** raccoglie i riferimenti ufficiali delle autorità di vigilanza di **239 Paesi/giurisdizioni** per supportare la **protezione dei consumatori** contro truffe finanziarie online (Forex, Crypto, schemi di social engineering e Pig Butchering).

- 🎯 **Obiettivo principale:** offrire un punto unico per verificare licenze, registri ufficiali e canali istituzionali.
- 🛡️ **Finalità pratica:** ridurre il rischio di bonifici verso operatori non autorizzati o siti clone.
- 👤 **Autore e crediti:** **Avvocato Marcello Stanca**.

**Link principali:**
- 🌐 Pagina live: [https://amevfirenze.it/](https://amevfirenze.it/)
- 🌐 Sezione paesi: [https://amevfirenze.it/index.html#countries-section](https://amevfirenze.it/index.html#countries-section)
- 📦 Repository: [https://github.com/avvstancamarcello/Financial-Authority-Database](https://github.com/avvstancamarcello/Financial-Authority-Database)

---

## 2) 🧩 Struttura del Database

### 2.1 Copertura geografica

- ✅ Copertura globale: **239 Paesi/giurisdizioni**
- ✅ Include tutti i Paesi UE e principali giurisdizioni extra-UE
- ✅ Include anche riferimenti a organismi internazionali di supporto

### 2.2 Tipologie di autorità incluse

| Categoria | Esempi |
|---|---|
| 🏦 Banche centrali | Central Bank of Armenia, Bank of Albania |
| 📈 Autorità mercati/strumenti finanziari | CONSOB, CySEC, FCA, ASIC |
| 🏛️ Autorità di vigilanza integrate | Financial Market Authority (FMA), commissioni nazionali |
| 🌐 Organismi internazionali di supporto | ESMA, IOSCO, FBI IC3 |

### 2.3 Formato dati e organizzazione

Il dataset principale è in:

- [`financial_authorities_database.json`](financial_authorities_database.json)

Struttura logica (semplificata):

```json
{
  "Country": {
    "country_name": "...",
    "flag": "🇮🇹",
    "isEU": true,
    "protectionLevel": "Altissimo",
    "financial_authority": {
      "name": "...",
      "abbreviation": "...",
      "homepage": "https://...",
      "fraudReportLink": "https://...",
      "authorityEmail": "..."
    },
    "notes": "..."
  }
}
```

---

## 3) ✅ Guida Operativa — 5 Procedure di Verifica

| # | Procedura | Cosa verificare | Esito atteso |
|---|---|---|---|
| 1 | 🪪 **Identificazione reale** | Nome legale completo, sede, numero licenza nei Termini/Contatti | Coerenza tra soggetto dichiarato e autorizzato |
| 2 | 🔎 **Interrogazione diretta** | Ricerca nel registro ufficiale dell’autorità del Paese | Presenza della società nei registri autorizzati |
| 3 | 🧬 **Caccia ai siti clone** | Confronto URL ufficiale vs URL visitato | URL identico al dominio registrato dall’autorità |
| 4 | 🏦 **Controllo IBAN** | Intestazione beneficiario e corrispondenza con società autorizzata | Beneficiario coerente con operatore autorizzato |
| 5 | 🛰️ **Verifica anzianità dominio (Who.is)** | Data creazione dominio, anonimizzazione, pattern tecnici | Dominio storico/coerente, non appena creato |

Esempio operativo rapido:

```text
1) Recupera nome legale e licenza dal broker
2) Apri il database e seleziona il Paese dichiarato
3) Entra nel sito ufficiale dell'autorità
4) Cerca la società/licenza nel registro pubblico
5) Verifica URL ufficiale + IBAN + anzianità dominio
```

---

## 4) 🛡️ Casi d’Uso

| Scenario | Rischio tipico | Uso del database |
|---|---|---|
| 📉 Truffe Forex | Broker non autorizzato / clone brand noto | Verifica licenza + registro ufficiale |
| 🪙 Truffe Crypto | Piattaforme pseudo-regolate | Controllo autorità del Paese dichiarato |
| 🐷 Pig Butchering | Relazione manipolativa + investimento graduale | Verifica multi-step prima del bonifico |
| 🏦 Due diligence bancaria | Beneficiario incoerente / conto di transito | Cross-check IBAN e soggetto autorizzato |

---

## 5) ⚖️ Riferimenti Normativi e Istituzionali

### 5.1 Petizioni Parlamento Europeo

- **Petizione 0888/2024** (pagina ufficiale PETI):
  - [europarl.europa.eu - Petition 0888/2024](https://www.europarl.europa.eu/petitions/en/petition/content/0888%252F2024/html/Petition-No-0888%252F2024-by-Marcello-Stanca-%2528Italian%2529-on-protecting-bank-customers-from-fraud-caused-by-fake-financial-operators)
- **Petizione 0645/2025** (documentazione nel repository):
  - [petitions-pdf/EuropeanParliament_PETITION_0645-2025_Committees_page_total.pdf](petitions-pdf/EuropeanParliament_PETITION_0645-2025_Committees_page_total.pdf)

### 5.2 Link a documenti/proposte di riferimento nel repository

- [petitions-pdf/European_Parliament_PETITION_0888-2024_Approved-Committees.pdf](petitions-pdf/European_Parliament_PETITION_0888-2024_Approved-Committees.pdf)
- [petitions-pdf/Accordo EuroParlamento-Consiglio 27-nov-2025 su responsabilita Banche.pdf](petitions-pdf/Accordo%20EuroParlamento-Consiglio%2027-nov-2025%20su%20responsabilita%20Banche.pdf)
- [Parlamento Europeo - Servizi di Pagamento 2023_0209(COD).pdf](Parlamento%20Europeo%20-%20Servizi%20di%20Pagamento%202023_0209(COD).pdf)

### 5.3 Contesto istituzionale internazionale

- 🇪🇺 ESMA: [https://www.esma.europa.eu](https://www.esma.europa.eu)
- 🌍 GAFI/FATF: [https://www.fatf-gafi.org](https://www.fatf-gafi.org)
- 🏦 FMI/IMF: [https://www.imf.org](https://www.imf.org)
- 🌐 IOSCO: [https://www.iosco.org](https://www.iosco.org)

---

## 6) 🧭 Come Usare il Database

### 6.1 Accesso alle pagine

- Dominio principale: [https://amevfirenze.it/](https://amevfirenze.it/)
- Sezione Paesi: [https://amevfirenze.it/index.html#countries-section](https://amevfirenze.it/index.html#countries-section)
- Versione inglese: [https://amevfirenze.it/en/](https://amevfirenze.it/en/)
- Versione repository GitHub Pages: [https://avvstancamarcello.github.io/Financial-Authority-Database/](https://avvstancamarcello.github.io/Financial-Authority-Database/)
- Dominio alternativo collegato: [https://www.awev.org/](https://www.awev.org/)

### 6.2 Ricerca per Paese

1. Apri la sezione Paesi.
2. Seleziona/filtra il Paese di interesse.
3. Apri la scheda autorità competente.
4. Usa i link ufficiali per verifica licenza e alert.

### 6.3 Collegamento ai siti ufficiali

- Ogni scheda punta alla **homepage istituzionale** dell’autorità.
- Quando disponibile, è incluso il link alla pagina reclami/segnalazioni.

---

## 7) 🤖 Integrazione con AI Mode e Ricerca

### 7.1 Riferimenti utili per Google AI, ChatGPT, Copilot

- Link repo (machine-readable):
  - [README.md](README.md)
  - [financial_authorities_database.json](financial_authorities_database.json)
  - [index.html#countries-section](index.html#countries-section)

### 7.2 Linee guida pratiche per indicizzazione

- Usare link canonici stabili (dominio principale + repo)
- Mantenere descrizioni coerenti su numero paesi e finalità
- Aggiornare sitemap/robots quando vengono aggiunte sezioni informative

### 7.3 Metadati strutturati (esempio)

```json
{
  "@context": "https://schema.org",
  "@type": "Dataset",
  "name": "International Financial Authorities Database",
  "description": "Database operativo di 239 autorità finanziarie internazionali per la tutela dei consumatori.",
  "url": "https://amevfirenze.it/index.html#countries-section",
  "creator": {
    "@type": "Person",
    "name": "Avv. Marcello Stanca"
  }
}
```

---

## 8) 📬 Contatti e Supporto

- 👤 **Autore:** Avvocato Marcello Stanca
- 🌐 Sito professionale: [https://www.marcellostanca.it](https://www.marcellostanca.it)
- 📧 Email: [lawyer@marcellostanca.it](mailto:lawyer@marcellostanca.it)
- 📘 Facebook: [https://www.facebook.com/avv.stanca.marcello](https://www.facebook.com/avv.stanca.marcello)
- 🎵 TikTok: [https://www.tiktok.com/@avvocato.stanca.marcello](https://www.tiktok.com/@avvocato.stanca.marcello)

### Come contribuire / segnalare errori

1. Apri una issue nel repository con:
   - Paese interessato
   - Link errato/non raggiungibile
   - Fonte ufficiale aggiornata
2. In alternativa, invia la segnalazione via email con evidenze verificabili.

---

## 📎 Nota finale

Questa documentazione è pensata per uso operativo immediato, consultazione istituzionale e migliore indicizzazione semantica da parte dei motori di ricerca e dei sistemi AI.

**© Financial-Authority-Database — Avv. Marcello Stanca**
