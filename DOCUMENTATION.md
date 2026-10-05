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

## 9) Pubblicazione AMEV — Verifica prima di pagare

La pagina è preparata per l'hosting statico GitHub Pages esistente: root del
repository, `.nojekyll` e `CNAME` (`www.amevfirenze.it`) invariati. La pubblicazione
iniziale non modificava homepage o service worker; l'aggiornamento autorizzato
aggiunge il collegamento descritto sotto e aggiorna la cache PWA. Nessuna modifica
alle impostazioni Pages; nessun merge.

- Wrapper: [`verifica-prima di pagare/index.html`](verifica-prima%20di%20pagare/index.html).
- Frontend: [`verifica-prima di pagare/amevcheck/index.html`](verifica-prima%20di%20pagare/amevcheck/index.html).
- Rapporto: [`verifica-prima di pagare/amevcheck/rapporto.html`](verifica-prima%20di%20pagare/amevcheck/rapporto.html).
- Risorse: `verifica-prima di pagare/amevcheck/assets/` (intero output compilato,
  compresi i chunk dinamici del renderer del rapporto).

### Origine e aggiornamento riproducibile

La fonte è lo ZIP originale `amev-verifica-prima.zip`, conservato senza modifiche.
Nel checkout corrisponde al blob `9b7ff3fdb0ba69e181b8e64b266377db20e68be8` del
commit `e911e01f07b17f37c7b3a4eacd527e196b5b3c42`, senza differenze.
SHA-256: `cb7091af169921e27d836b9b1a987b3ccd6b4a7a2414cae540b7da01f550eb0b`.

Prerequisiti: Python >= 3.9, Node >= 22.12, Corepack e accesso al registry npm.
Eseguire da qualunque directory, sostituendo il percorso assoluto del checkout:

```sh
python "/home/runner/work/Financial-Authority-Database/Financial-Authority-Database/scripts/build_amev_pages.py"
```

Lo script verifica l'hash, valida **tutte** le entry prima di estrarle in una nuova
directory temporanea (rifiuta traversal, nomi duplicati e link; non sovrascrive
file durante l'estrazione), applica gli adattamenti statici e usa:

```sh
corepack pnpm@10.34.5 install --frozen-lockfile --ignore-scripts --config.manage-package-manager-versions=false
corepack pnpm@10.34.5 run check
corepack pnpm@10.34.5 exec vite build
```

Il progetto dichiara pnpm 10.4.1: la sola versione dell'installer è sostituita
con 10.34.5 per gli advisory di sicurezza su pnpm. `package.json`, lockfile,
versioni dell'app e patch wouter restano invariati. Gli script del manifest
sono stati ispezionati; non ci sono lifecycle di progetto. Gli script delle
dipendenze non vengono eseguiti. Non usare `pnpm build`: compila anche Express.
Non avviare `dev`/`preview` per la pubblicazione; Vite 7.1.9 dello ZIP ha advisory
relativi al server di sviluppo, non al sito statico prodotto.

La configurazione mirata `scripts/amev-pages.vite.config.ts` sostituisce quella
Manus **solo nella directory temporanea**: plugin React/Tailwind, base `./`,
due entry HTML, output reale `dist/public`. Lo script sostituisce **solo**
`verifica-prima di pagare/amevcheck/`, dopo build e type-check riusciti, copiando
tutto `dist/public`; non modificare manualmente quel contenuto generato.
I sorgenti React estratti, `node_modules`, `.env`, server e log rimangono fuori
dal commit. Il testo editoriale mantenuto in `scripts/amev-report.txt` sostituisce
il corpo del dossier nella directory temporanea; i 31 riferimenti dello ZIP
sono conservati. Per aggiornare “Analisi completa”, modificare quel testo e
ricostruire, non i bundle generati.
Con un nuovo ZIP, ispezionare prima manifest, lockfile, script e adattamenti;
solo dopo aggiornare l'hash nello script. Un hash diverso interrompe la build.

### Adattamenti e limiti

- Wrapper minimo basato sul contenitore iframe fullscreen orbital del sito:
  sfondo scuro, iframe con titolo accessibile, altezza dinamica mobile e scroll
  interno, barra con ritorno al database e apertura diretta. Nessuna aggiunta
  alle allowlist orbital della homepage, dedicate alle autorità.
- Link interni relativi e routing sotto la directory corrente; `rapporto.html`
  è una vera entry statica, riapribile/ricaricabile senza fallback Express.
  Le ancore del dossier e della checklist restano utilizzabili.
- Le immagini richieste dal sorgente ZIP sono sostituite con i file forniti
  dall'utente e committati nella root al commit `b7a32f3`:
  `globo-terrestre.png` (732×681) per la hero e `bussola-e-cristallo.png`
  (611×788) per la sezione editoriale e il rapporto. Lo script conserva gli
  originali e li copia in `amevcheck/images/`, collegandoli con percorsi relativi.
  Non servono `/manus-storage/`, credenziali Forge o servizi esterni per le immagini.
- Nessun collector/debug Manus, plugin runtime Manus o proxy nel build pubblico.
  I componenti template OAuth/Map non sono importati dall'app e non sono inclusi
  nel bundle; nessuna credenziale o variabile ambiente necessaria alla guida.
- Guida, checklist e dossier sono informativi: nessuna verifica live di imprese,
  IBAN o carte, nessuna certificazione del pagamento. Il dossier è rielaborato
  il 5 ottobre 2026 come elenco degli elementi di affidabilità verificabili
  della guida attuale, non di criticità storiche da correggere. È allineato
  anche il breve richiamo editoriale della landing. Fonti e contesto dell'analisi
  originaria del 28 settembre 2026 restano distinti dalla revisione editoriale;
  non si dichiarano risolti difetti tecnici non rivalidati né approvazioni Google/AI.
  I siti dei registri si aprono all'esterno e dipendono dalla loro disponibilità.
  Google Fonts resta un servizio esterno con fallback CSS locale.

### Verifica locale

```sh
python -m http.server 8000 --bind 127.0.0.1 --directory "/home/runner/work/Financial-Authority-Database/Financial-Authority-Database"
```

Aprire `http://127.0.0.1:8000/test-amev-pages.html` per i controlli browser
automatici e `http://127.0.0.1:8000/verifica-prima%20di%20pagare/index.html`
per il wrapper. Il test copre entry compilate, link/risorse, assenza debug/proxy,
rapporto, disclaimer, checklist/reset, menu mobile e overflow a 1000/375 px.
Verificare anche console/rete, navigazione guida–rapporto–ancore, ricaricamento
diretto, desktop/mobile e un mount locale `/Financial-Authority-Database/`.
La base relativa supporta dominio personalizzato e sottopercorso di progetto.
L'integrazione non implica che la pagina sia già pubblicata: serve la revisione
e il successivo deploy Pages.

Risultati della prima integrazione (prima del ripristino immagini): due build e type-check riusciti, 47 controlli browser
passati alla root e 47 sotto `/Financial-Authority-Database/`; flussi manuali
guida/rapporto/refresh/ancore/apertura diretta riusciti a 1440 e 390 px, senza
errori JavaScript né HTTP locali. Tutti i 398 asset rispondono HTTP 200 anche
sotto il percorso di progetto; wrapper e rapporto funzionano con il service
worker esistente attivo. Passati anche 9 controlli di estrazione ostile,
`git diff --check` sui file non generati, il validatore dati esistente e la
scansione segreti su tutti i file aggiunti/modificati. Nessuna richiesta
backend/debug osservata.
Il servizio browser integrato non era disponibile: verifiche eseguite con
Chromium locale e Playwright temporaneo fuori dal repository. Google Fonts non
era raggiungibile per DNS nell'ambiente di prova: verificati i font di fallback,
non il rendering con i font remoti. I registri esterni non sono stati rivalidati.
Vite segnala alcuni chunk oltre 500 kB; nessun errore di build. Gli altri test
HTML del sito non riguardano questa nuova pagina e non sono stati eseguiti.
Il diff completo segnala 36 whitespace negli asset delle dipendenze compilate,
conservati senza riscritture manuali. La revisione mirata dei sorgenti non ha
rilevato problemi significativi. La validazione parallela automatica è fallita
due volte leggendo il grande diff (`git diff`: timeout/SIGPIPE), prima di
restituire un risultato CodeQL; la CLI CodeQL non è disponibile localmente.
La scansione CodeQL rimane quindi da eseguire in un ambiente funzionante.

Aggiornamento del 5 ottobre 2026: build e TypeScript riusciti con le PNG
locali e il dossier rielaborato; 66 controlli browser passati alla root e 66
sotto `/Financial-Authority-Database/`, compresi caricamento immagini, 31 fonti,
assenza di endorsement AI, disclaimer e deep link ai pagamenti. Verificati
hero e rapporto a 1440/390 px, ricaricamento e ritorno alla guida; nessun errore
JavaScript o HTTP locale. Le PNG pubblicate sono byte-identiche agli originali;
i 398 asset compilati rispondono HTTP 200 al percorso di progetto. Passati
validatore dati e diff-check dei sorgenti. Google Fonts resta irraggiungibile
nel sandbox; verificati i fallback, non i font remoti.
Scansione segreti riuscita su tutti i 58 file nuovi/modificati della revisione;
revisione mirata senza problemi significativi. Anche il tentativo di validazione
parallela di questo aggiornamento si è interrotto sul diff compilato
(`timeout/SIGPIPE`), senza produrre un risultato CodeQL.

## 📎 Nota finale

Accesso alla guida: nell'«Indice Rapido – Naviga il Database» della homepage,
subito dopo «Cerca Autorità», il pulsante «Verifica prima di pagare» apre
`./verifica-prima%20di%20pagare/index.html` nella stessa scheda. L'etichetta
inglese è «Check before paying»; le altre lingue usano il fallback inglese
esistente. Guida e rapporto restano in italiano, senza una traduzione inglese
editoriale. La barra del wrapper presenta una nota sulla funzione Traduci del
browser (se disponibile), sul menu desktop/mobile e sulle possibili imprecisioni
della traduzione automatica. Non si caricano servizi di traduzione esterni.
La cache PWA passa a `financial-authority-v18` per aggiornare la homepage.

Verifiche dell'aggiornamento: `test-amev-pages.html` supera 70 controlli alla
radice e 70 sotto `/Financial-Authority-Database/`, senza errori JavaScript o
risposte HTTP locali di errore. Provati inoltre etichette italiana/inglese e
fallback francese, clic dalla homepage, nota visibile e iframe a 1440×900 e
390×844, senza overflow orizzontale. Passano `git diff --check`,
`node --check service-worker.js` e `python scripts/validate_international_data.py`
(25 istituzioni, 239 autorità). Non è necessaria una nuova build del frontend:
gli asset compilati non cambiano.

Validazione automatica di questo aggiornamento: la revisione integrata non è
disponibile per un errore del modello; CodeQL salta l'analisi JavaScript perché
il database è troppo grande. Non sono quindi esiti positivi di analisi.
Una revisione mirata alternativa non ha rilevato problemi significativi;
la scansione dei cinque file modificati non ha rilevato segreti.

Questa documentazione è pensata per uso operativo immediato, consultazione istituzionale e migliore indicizzazione semantica da parte dei motori di ricerca e dei sistemi AI.

**© Financial-Authority-Database — Avv. Marcello Stanca**
