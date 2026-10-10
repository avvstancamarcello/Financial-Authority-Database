# 🔐 GESTIONE DATABASE - GUIDA RAPIDA

## ✅ **LA RISPOSTA BREVE**

**Puoi modificare il JSON SENZA cifrare ogni volta!**

Il sito funziona in 2 modalità:
1. **Con `db.enc`** → Carica database cifrato (massima protezione)
2. **Senza `db.enc`** → Carica `financial_authorities_database.json` (funziona ugualmente!)

---

## 🎯 **CARATTERISTICHE PRINCIPALI**

### 📱 **Progressive Web App (PWA)**
✅ **Installabile** come app su PC, smartphone e tablet  
✅ **Funzionamento offline** dopo la prima visita  
✅ **Service Worker v2** con cache intelligente  
✅ **Manifest.json** completo con icone 192x192 e 512x512  
✅ **Caricamento istantaneo** da cache locale

### 🌍 **Bandiere Internazionali**
✅ **108 bandiere SVG** da flag-icon-css v7.2.3  
✅ **Dimensioni ottimali** 48×36px (proporzione 4:3)  
✅ **Compatibilità universale** desktop e mobile  
✅ **Cached nel Service Worker** per uso offline  
✅ **2 griglie uniformi** (accesso rapido + lista completa)

### 🔒 **Protezione Database**
✅ **Cifratura XOR + Base64** con chiave proprietaria  
✅ **Watermark** con copyright visibile  
✅ **Console warning** anti-scraping  
✅ **Anti-copy** protezione testo selezionato

---

## 🚀 **MODIFICHE VELOCI (Consigliato)**

```powershell
# 1. Modifica il JSON
code financial_authorities_database.json

# 2. Rimuovi db.enc
cd "c:\Users\Utente\Financial-Authority-Database"
Remove-Item db.enc

# 3. Deploy
git add financial_authorities_database.json
git commit -m "Corretto link CONSOB"
git push
```

⏱️ **Tempo:** 2 minuti  
🔒 **Protezione:** Watermark + console warning + anti-copy (OK per uso quotidiano)

---

## 🔐 **DEPLOY CON CIFRATURA (Produzione)**

```powershell
# 1. Modifica JSON
code financial_authorities_database.json

# 2. Cifra automaticamente
cd "c:\Users\Utente\BancheEuropa"
.\cifra_database.ps1

# 3. Deploy
cd "c:\Users\Utente\Financial-Authority-Database"
git add db.enc financial_authorities_database.json
git commit -m "Database aggiornato e cifrato"
git push
```

⏱️ **Tempo:** 5 minuti  
🔒 **Protezione:** MASSIMA (cifratura XOR + tutto il resto)

---

## 🔑 **LA CHIAVE PROPRIETARIA**

### **Nome:**
```
MarcelloStancaFlorenceIT2026
```

### **A cosa serve:**
- **Cifra** il JSON in `db.enc` (illeggibile senza chiave)
- **Decifra** nel browser quando il sito si carica

### **Come funziona:**
1. Script PowerShell cifra JSON con chiave → crea `db.enc`
2. Browser scarica `db.enc` → decifra con stessa chiave → mostra dati

### **È sicura?**
- ✅ Sì per **offuscamento** (scoraggia scraping facile)
- ⚠️ No per **segreti militari** (chiave visibile nel codice JS)
- 🎯 Scopo: rendere difficile copiare il database, non impossibile

### **Dove trovarla:**
- `cifra_database.ps1` riga 27
- `index.html` riga ~437

---

## 📝 **SCRIPT DISPONIBILI**

### **1. `cifra_database.ps1`**
✅ Cifra `financial_authorities_database.json` → crea `db.enc`  
✅ Copia automaticamente in directory deployment  
✅ Mostra statistiche e prossimi passi  

**Uso:**
```powershell
cd "c:\Users\Utente\BancheEuropa"
.\cifra_database.ps1
```

---

### **2. `decifra_database.ps1`**
✅ Decifra `db.enc` → crea `db_decrypted.json` (per verifica)  
✅ Confronta con originale  
✅ Valida JSON  

**Uso:**
```powershell
cd "c:\Users\Utente\BancheEuropa"
.\decifra_database.ps1
# Controlla db_decrypted.json
Remove-Item db_decrypted.json  # Pulisci dopo verifica
```

---

## 📱 **INSTALLAZIONE COME PWA**

### **Su Desktop (Chrome/Edge/Brave):**
1. Apri https://avvstancamarcello.github.io/Financial-Authority-Database/
2. Cerca icona **+** nella barra indirizzi o menu → "Installa"
3. Clicca "Installa"
4. L'app si apre standalone senza browser

### **Su Smartphone:**
- **iOS**: Safari → Condividi → "Aggiungi a Home"
- **Android**: Chrome → Menu → "Installa app"

### **Vantaggi PWA:**
✅ Icona su desktop/home screen  
✅ Funziona offline dopo prima visita  
✅ Caricamento istantaneo (cache)  
✅ Aggiornamenti automatici in background  
✅ Esperienza app nativa

---

## 🔧 **AGGIORNAMENTO SERVICE WORKER**

### News pubbliche della homepage

La homepage canonica `/index.html` conserva il contenitore CONSOB/FCA/AMF e
collega `/news.html`. Entrambe le pagine leggono l'esportazione pubblica
`https://raw.githubusercontent.com/avvstancamarcello/Airtable-news/main/news.json`
tramite `scripts/news-feed.js`, senza credenziali né richieste dirette ad Airtable.
La data è il massimo `published_at` valido dei record con `status: "published"`,
considerando tutte le traduzioni, visualizzato nella lingua corrente in UTC.
Non è la data di caricamento. Le notizie sono raggruppate per `news_id`, ordinate
dalla più recente e selezionate nella lingua richiesta; se manca, viene usato
inglese, poi italiano, poi una traduzione disponibile, con avviso esplicito.
Feed vuoti o non disponibili non mostrano date né contenuti di esempio.

Il feed viene rivalidato a ogni caricamento (`cache: "no-cache"`), non aggiornato
in tempo reale. Il service worker non intercetta questo JSON; conserva invece
il controller locale e la pagina News nella shell. Il feed resta non disponibile
offline. Il flag di ogni card proviene dal campo `flag` del record selezionato,
senza mapping frontend o dipendenze dal JSON delle Autorità; valori mancanti o
non emoji usano 🏳️. L'esportazione pubblica deve includere il campo formula
`NEWS_SIMPLE.flag` affinché siano mostrate le bandiere editoriali.
Incrementare la versione cache quando cambia.
Le regressioni browser sono `/test-website-news.html`, `/test-app-news.html`
(copertura PR #170) e `/test-pwa.html`, servite con `python3 -m http.server 8000`.

`/APP/index.html` è una variante attiva di riferimento e attualmente non contiene
una voce News; la pagina app già integrata è `/APP/News-format-file-APP.html`,
che rimane invariata. Nel progetto Android, `CountriesScreen.kt` apre
`https://www.amevfirenze.it/news.html` tramite `Intent.ACTION_VIEW` in
`MainScaffold.kt`: queste modifiche web non richiedono una ricompilazione APK.
Non cambiano asset Android, URL nativi, firma o versioni e non pubblicano release.
Verificare separatamente il deployment pubblico dopo il merge; i test locali
non certificano la pubblicazione né il funzionamento su dispositivi fisici.

### Visione panoramica bandiere — pubblicazione

I record possono specificare `flagImage`, un percorso locale `flags/nome.svg`
(solo lettere minuscole, cifre, trattini e underscore nel nome).
La griglia principale usa questo SVG decorativo mantenendo il nome accessibile
del pulsante e il fallback preesistente se l'immagine non viene caricata.
Québec usa `flags/quebec.svg`, CSA federale `flags/canada.svg`; l'emoji `flag`
resta disponibile per gli altri renderer. Mantenere sincronizzati i quattro
JSON: root, `APP/`, `DEPLOY_REGISTER/` e asset Android.

Il selettore permanente sopra `#flagsGrid` offre **Compatta / Dettagliata**.
Senza una scelta valida salvata, il default è compatto fino a 768px e dettagliato
oltre 768px; si adatta alla larghezza solo finché l'utente non sceglie.
Il dialogo si ripresenta a ogni caricamento completo durante la campagna,
anche a chi ha già salvato una preferenza. Dopo la scadenza viene chiesto solo
a chi non ha una preferenza valida. Lingua, hash e rendering non lo riaprono.

**Al momento della pubblicazione**, aggiornare `campaignStart` nello script
`#flag-view-bootstrap` di `index.html` con l'istante UTC effettivo di disponibilità
(formato ISO, ad esempio `2026-10-09T12:00:00Z`), mai anteriore al 9 ottobre 2026.
`campaignEnd` è calcolato automaticamente come inizio + 30 giorni: l'inizio è
inclusivo e la fine esclusiva. Una pubblicazione successiva estende quindi la
campagna oltre l'8 novembre 2026. Non usare la prima visita del singolo utente
né una data ricalcolata a ogni caricamento. Se la pubblicazione viene rinviata,
aggiornare di nuovo l'inizio **prima** di pubblicare, per garantire 30 giorni reali.
La data nel codice è una configurazione di rilascio, non una prova di deployment.

Solo il checkbox **Ricorda questa scelta su questo dispositivo** abilita il
salvataggio: chiave `financial-authority-flag-view`, versione 1, modalità validata
e `remember: true`. Deselezionarlo rimuove solo questa preferenza; i dati
legacy/non validi non costituiscono consenso. Con storage bloccato la scelta
resta attiva nella pagina corrente. Chiudere/Escape non inventa né salva una scelta.

`/` e `/index.html` condividono lo storage sullo **stesso origin** e il service
worker aggiorna entrambe le copie HTML con strategia network-first.
`www` e non-`www`, protocolli o domini differenti hanno storage separati:
il sito non trasferisce preferenze tra origin e non controlla “Sito desktop”
del browser. `CNAME` indica `www.amevfirenze.it`; non si deduce da questi file
quale redirect o copia sia effettivamente servita in produzione.

Regressioni locali: servire la root con `python3 -m http.server 8000` e aprire
`http://localhost:8000/test-flag-view.html`. Le fixture isolano script esterni,
storage e orologio; controllano layout, consenso, campagne e lifecycle.
Non certificano il rendering su un dispositivo fisico né un deployment.

Quando modifichi il sito, aggiorna versione cache:

**File:** `service-worker.js`
```javascript
const CACHE_NAME = 'financial-authority-v2'; // ← Incrementa versione
```

Dopo modifica:
```powershell
cd "c:\Users\Utente\Financial-Authority-Database"
git add service-worker.js
git commit -m "Update: Service worker v3"
git push
```

Gli utenti riceveranno aggiornamento automatico al prossimo reload.

---

## 💡 **ESEMPI PRATICI**

### **Correggere 1 link obsoleto:**
```powershell
# Modifica JSON → Save
# Deploy SENZA cifratura (veloce)
cd "c:\Users\Utente\Financial-Authority-Database"
Copy-Item "c:\Users\Utente\BancheEuropa\financial_authorities_database.json" .
Remove-Item db.enc
git add financial_authorities_database.json
git commit -m "Fix link CONSOB"
git push
```

### **Aggiungere nuovo paese:**
```powershell
# Modifica JSON → Aggiungi Russia → Save
# Cifra + Deploy
cd "c:\Users\Utente\BancheEuropa"
.\cifra_database.ps1
cd "c:\Users\Utente\Financial-Authority-Database"
git add db.enc financial_authorities_database.json
git commit -m "Aggiunta Russia (109 paesi)"
git push
```

---

## ⚠️ **FAQ VELOCE**

**Q: Devo cifrare dopo ogni modifica?**  
A: NO! Solo quando vuoi massima protezione.

**Q: Il sito funziona senza db.enc?**  
A: SÌ! Usa JSON come fallback.

**Q: Quanto spesso cifrare?**  
A: Modifiche minori = no cifratura. Batch update o nuovi paesi = cifra.

---

## 📂 **DOCUMENTI COMPLETI**

- [WORKFLOW_DATABASE.md](WORKFLOW_DATABASE.md) - Guida dettagliata 3 modalità
- [COPYRIGHT_SEO_REPORT.md](COPYRIGHT_SEO_REPORT.md) - Report protezioni implementate
- [GUIDA_VERIFICA_SEO.md](GUIDA_VERIFICA_SEO.md) - Test e checklist

---

**© 2026 Avvocato Marcello Stanca - Firenze, Italy**

---

## 📜 Best Practice "Marcello Stanca" (Estratto)

La progettazione di questa web page e del database è stata guidata
dal framework di regole **"Best Practice Copyright Marcello Stanca"**,
che definisce il metodo di lavoro tra Utente e AI.

Principi chiave applicati:

- **Visione d'Insieme** – architettura logica definita prima del codice operativo.
- **Memoria del Successo** – soluzioni funzionanti riusate e non riscritte da zero.
- **Comunicazione Specifica** – spiegazione puntuale di ogni modifica al codice.
- **Sicurezza Nativa & Validazione Input** – attenzione a dati esterni e flusso on-chain.
- **Testing Multi-Scenario** – modifiche introdotte per piccoli passi, con verifica.
- **Perseveranza Metodica** – evoluzione incrementale dell'interfaccia senza rotture.

Per una descrizione formale, le regole sono state anche codificate
in **Prolog** nel file `README.md` del workspace, usando il predicato
`ms_rule/6`. Esempio di rappresentazione:

```prolog
% Regola 1 – Visione d'Insieme
ms_rule(r1, 1, architettura_visione,
	'Visione d\'Insieme',
	architettura,
	'Definizione dell\'architettura logica prima della stesura del codice.').

% Regola 3 – Memoria del Successo
ms_rule(r3, 3, architettura_visione,
	'Memoria del Successo',
	memoria_successo,
	'Archiviare le soluzioni funzionanti per creare una base di conoscenza solida.').
```

Questa codifica consente, in futuro, di collegare strumenti automatici
di verifica (linting semantico, agent AI, validatori) alle stesse regole
di metodo che hanno guidato lo sviluppo di questa pagina.
