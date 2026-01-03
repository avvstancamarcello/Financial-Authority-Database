# GUIDA RAPIDA: GESTIONE DATABASE
## Financial Authority Database - Workflow Pratico

---

## 🎯 **CARATTERISTICHE TECNICHE**

### 📊 **Database**
- **108 paesi** con autorità finanziarie
- **Formato:** JSON (59KB) → db.enc cifrato (80KB)
- **Cifratura:** XOR + Base64 con chiave proprietaria
- **Aggiornamenti:** Modificabili senza ri-cifrare

### 📱 **Progressive Web App**
- **Installabile** su desktop e mobile
- **Service Worker v2** con cache offline
- **Manifest.json** completo (icone 192x192, 512x512)
- **Caricamento:** Istantaneo da cache locale
- **Compatibilità:** Chrome, Edge, Brave, Safari, Firefox

### 🌍 **Interfaccia Utente**
- **Bandiere SVG:** flag-icon-css v7.2.3 (106 paesi + 2 emoji regionali)
- **Dimensioni:** 48×36px (proporzione 4:3)
- **Griglie:** 2 uniformi (accesso rapido + lista completa)
- **Multilingua:** IT, EN, FR, DE, ES
- **Ricerca:** Filtro dinamico real-time

### 🔒 **Protezione**
- Cifratura database (opzionale)
- Watermark copyright visibile
- Console warning anti-scraping
- Anti-copy protezione testo
- robots.txt + sitemap.xml

---

## 🚀 **3 MODALITÀ DI LAVORO**

### **Modalità 1: SOLO JSON (Sviluppo Rapido)**
✅ **Quando usarla:** Modifiche frequenti, testing, correzioni veloci  
✅ **Protezione:** Watermark + console warning + anti-copy  
❌ **Limitazione:** Database leggibile in chiaro nel browser

**Procedura:**
```powershell
# 1. Modifica il JSON
code financial_authorities_database.json

# 2. Rimuovi db.enc dalla repo (se presente)
cd "c:\Users\Utente\Financial-Authority-Database"
Remove-Item db.enc

# 3. Commit
git add financial_authorities_database.json
git commit -m "Aggiornato link [paese] e corretto email [autorità]"
git push
```

**Risultato:** Sito carica JSON in chiaro (funziona perfettamente).

---

### **Modalità 2: JSON + CIFRATURA (Produzione)**
✅ **Quando usarla:** Deploy finale, massima protezione  
✅ **Protezione:** Tutto di Modalità 1 + cifratura XOR + Base64  
✅ **Vantaggio:** Database illeggibile senza chiave

**Procedura:**
```powershell
# 1. Modifica il JSON
code financial_authorities_database.json

# 2. Cifra con script automatico
cd "c:\Users\Utente\BancheEuropa"
.\cifra_database.ps1

# 3. Commit entrambi i file
cd "c:\Users\Utente\Financial-Authority-Database"
git add db.enc financial_authorities_database.json
git commit -m "Database aggiornato e cifrato"
git push
```

**Risultato:** Sito carica `db.enc` cifrato, JSON serve come backup.

---

### **Modalità 3: SOLO CIFRATURA (Sicurezza Massima)**
✅ **Quando usarla:** Vuoi nascondere completamente il JSON  
✅ **Protezione:** Massima, nessun file leggibile in chiaro  
⚠️ **Rischio:** Se perdi chiave, perdi accesso ai dati

**Procedura:**
```powershell
# 1. Modifica JSON localmente
code "c:\Users\Utente\BancheEuropa\financial_authorities_database.json"

# 2. Cifra
cd "c:\Users\Utente\BancheEuropa"
.\cifra_database.ps1

# 3. Pubblica SOLO db.enc
cd "c:\Users\Utente\Financial-Authority-Database"
git add db.enc
git commit -m "Database cifrato aggiornato"
git push

# 4. NON committare il JSON in chiaro
# (Tienilo solo localmente in BancheEuropa)
```

**Risultato:** Repository contiene solo `db.enc`, JSON protetto.

---

## 🔑 **LA CHIAVE PROPRIETARIA**

### **Nome:**
```
MarcelloStancaFlorenceIT2026
```

### **Dove si trova:**

**1. Script PowerShell** (`cifra_database.ps1` riga 27):
```powershell
$key = 'MarcelloStancaFlorenceIT2026'
```

**2. JavaScript del sito** (`index.html` riga ~437):
```javascript
const COPYRIGHT_KEY = 'MarcelloStancaFlorenceIT2026';
```

### **Come funziona:**

**Cifratura (PowerShell):**
```
1. Leggi JSON → "{ 'Albania': {...} }"
2. Per ogni carattere:
   - Prendi carattere chiave (rotazione ciclica)
   - XOR bit-a-bit
3. Converti risultato in Base64
4. Salva come db.enc
```

**Decifratura (Browser):**
```
1. Scarica db.enc
2. Decodifica Base64
3. Per ogni byte:
   - XOR con carattere chiave (stesso ordine)
4. Ottieni JSON originale
5. Parse e visualizza
```

### **Sicurezza:**
- ✅ Chiave hardcoded in JavaScript → Browser può decifrare
- ⚠️ Chiave visibile nel sorgente → **Non è crittografia militare!**
- ✅ Scopo: **Offuscamento** per scoraggiare scraping facile
- ✅ Combinata con altre protezioni = ottimo deterrente

### **Come cambiare la chiave:**

**Se vuoi usare chiave diversa:**
```powershell
# 1. Modifica cifra_database.ps1 (riga 27)
$key = 'TuaNuovaChiaveSegreta2026'

# 2. Modifica index.html (riga ~437)
const COPYRIGHT_KEY = 'TuaNuovaChiaveSegreta2026';

# 3. Ri-cifra
.\cifra_database.ps1

# 4. Deploy tutto
git add index.html db.enc
git commit -m "Cambiata chiave cifratura"
git push
```

⚠️ **ATTENZIONE:** File vecchi cifrati con chiave vecchia diventano illeggibili!

---

## 📝 **ESEMPI PRATICI**

### **Esempio 1: Correggere link obsoleto Italia**

```powershell
# 1. Apri JSON
code "c:\Users\Utente\BancheEuropa\financial_authorities_database.json"

# 2. Cerca "Italy" e modifica:
"homepage": "https://www.consob.it/web/consob/home-nuovo"
# ↓
"homepage": "https://www.consob.it"

# 3. Salva (Ctrl+S)

# 4. Deploy veloce (SOLO JSON, no cifratura)
cd "c:\Users\Utente\Financial-Authority-Database"
Copy-Item "c:\Users\Utente\BancheEuropa\financial_authorities_database.json" .
Remove-Item db.enc  # Forza uso JSON chiaro
git add financial_authorities_database.json
git commit -m "Corretto link CONSOB Italia"
git push
```

**Tempo:** 2 minuti  
**Protezione:** Watermark + console warning

---

### **Esempio 2: Aggiungere nuovo paese (con cifratura)**

```powershell
# 1. Apri JSON
code "c:\Users\Utente\BancheEuropa\financial_authorities_database.json"

# 2. Aggiungi nuovo oggetto:
"Russia": {
  "country_name": "Russia",
  "flag": "🇷🇺",
  "isEU": false,
  "protectionLevel": "medio",
  ...
}

# 3. Salva

# 4. Cifra
cd "c:\Users\Utente\BancheEuropa"
.\cifra_database.ps1

# 5. Deploy con massima protezione
cd "c:\Users\Utente\Financial-Authority-Database"
git add db.enc financial_authorities_database.json
git commit -m "Aggiunta Russia (109 paesi totali)"
git push
```

**Tempo:** 5 minuti  
**Protezione:** Massima (cifratura XOR)

---

### **Esempio 3: Batch update 10 paesi**

```powershell
# 1. Modifica JSON con editor
code "c:\Users\Utente\BancheEuropa\financial_authorities_database.json"

# Cambi multipli:
# - Aggiorna 5 email
# - Correggi 3 link
# - Aggiungi 2 social_links

# 2. Salva tutto

# 3. Deploy SENZA cifratura (per verificare)
cd "c:\Users\Utente\Financial-Authority-Database"
Copy-Item "c:\Users\Utente\BancheEuropa\financial_authorities_database.json" .
Remove-Item db.enc
git add financial_authorities_database.json
git commit -m "Batch update: 10 paesi aggiornati"
git push

# 4. Testa sul sito live (5 min)

# 5. Se tutto OK, cifra
cd "c:\Users\Utente\BancheEuropa"
.\cifra_database.ps1
Copy-Item db.enc "c:\Users\Utente\Financial-Authority-Database\"
cd "c:\Users\Utente\Financial-Authority-Database"
git add db.enc
git commit -m "Database cifrato post-verifica"
git push
```

**Workflow sicuro:** Test prima, cifra dopo.

---

## 📱 **GESTIONE PWA E SERVICE WORKER**

### **Aggiornamento Service Worker**

Quando modifichi il sito o aggiungi funzionalità:

```powershell
# 1. Aggiorna versione cache in service-worker.js
code "c:\Users\Utente\BancheEuropa\service-worker.js"

# Cambia riga 1:
const CACHE_NAME = 'financial-authority-v2';
# ↓
const CACHE_NAME = 'financial-authority-v3';

# 2. Aggiungi nuove risorse da cachare (se necessario)
const urlsToCache = [
  '/Financial-Authority-Database/',
  '/Financial-Authority-Database/index.html',
  '/Financial-Authority-Database/financial_authorities_database.json',
  '/Financial-Authority-Database/icon-192.png',
  '/Financial-Authority-Database/icon-512.png',
  'https://cdn.jsdelivr.net/gh/lipis/flag-icons@7.2.3/css/flag-icons.min.css'
];

# 3. Deploy
cd "c:\Users\Utente\Financial-Authority-Database"
Copy-Item "c:\Users\Utente\BancheEuropa\service-worker.js" .
git add service-worker.js
git commit -m "Update: Service worker v3 con nuove risorse"
git push
```

### **Quando aggiornare versione:**
- ✅ Modifiche a index.html
- ✅ Aggiornamenti al database JSON/db.enc
- ✅ Nuove librerie CDN
- ✅ Modifiche grafiche/CSS
- ❌ Solo correzioni testo (non necessario)

### **Testing PWA:**

```powershell
# 1. Installa PWA localmente
# Apri http://localhost:8000 (o serve statico)
# Chrome → Menu → Installa

# 2. Verifica funzionamento offline
# DevTools (F12) → Network → Offline
# Ricarica pagina → Deve funzionare

# 3. Controlla cache
# DevTools → Application → Cache Storage
# Verifica 'financial-authority-v2' contiene file
```

---

## 🌍 **MANUTENZIONE BANDIERE**

### **Le bandiere usano flag-icon-css v7.2.3:**

**Struttura HTML generata:**
```html
<!-- Bandiere nazionali -->
<span class="flag-icon-wrapper">
  <span class="fi fi-it"></span>  <!-- Italia -->
</span>

<!-- Regioni (CEMAC, ECSRC) -->
<span class="flag-icon-wrapper">🌍</span>
```

### **Aggiunta nuovi paesi:**

1. **Aggiungi al JSON** con codice ISO corretto:
```json
"Nuovo Paese": {
  "country_name": "Nuovo Paese",
  "flag": "🇳🇵",  // Emoji (non usata ma mantenuta)
  ...
}
```

2. **Aggiungi mapping codice** in `index.html` (funzione `getCountryCode`):
```javascript
const codes = {
  'Albania': 'AL',
  ...
  'Nuovo Paese': 'NP',  // ← Aggiungi qui
  ...
};
```

3. **La bandiera apparirà automaticamente** se flag-icon-css supporta il codice ISO.

### **Se bandiera non si vede:**
- Verifica codice ISO corretto (deve essere lowercase in HTML: `fi-it` non `fi-IT`)
- Controlla che flag-icon-css supporti il paese
- Per territori speciali usa emoji nel fallback

---

## ⚠️ **DOMANDE FREQUENTI**

### **Q: Devo cifrare dopo OGNI modifica?**
**A:** NO! Cifra solo quando vuoi massima protezione. Per modifiche quotidiane usa JSON chiaro.

### **Q: Il sito funziona senza db.enc?**
**A:** SÌ! Carica automaticamente `financial_authorities_database.json` come fallback.

### **Q: Posso avere SOLO db.enc (no JSON)?**
**A:** SÌ! Togli JSON dalla repo, ma conservalo localmente in `BancheEuropa` per modifiche future.

### **Q: Se cambio chiave, cosa succede?**
**A:** File vecchi cifrati diventano illeggibili. Ri-cifra tutto con nuova chiave.

### **Q: Quanto è sicura la cifratura?**
**A:** È **offuscamento**, non crittografia forte. Chiave visibile nel sorgente JS. Scopo: scoraggiare scraping automatico, non proteggere segreti militari.

### **Q: Posso automatizzare cifratura dopo ogni commit?**
**A:** SÌ! Con Git hooks. Ti serve?

---

## 🎯 **RACCOMANDAZIONE**

**Workflow ideale:**

1. **Sviluppo:** Modifica JSON, deploy senza cifratura (veloce)
2. **Test:** Verifica modifiche sul sito live
3. **Produzione:** Cifra e deploy finale
4. **Manutenzione:** Ripeti ciclo

**Frequenza cifratura consigliata:**
- 🔄 Modifiche minori (1-2 paesi): NO cifratura
- 🔄 Batch update (5+ paesi): Cifra dopo verifica
- 🔄 Aggiunta nuovi paesi: Cifra sempre
- 🔄 Rilascio mensile: Cifra sempre

---

## 📧 **SUPPORTO**

**Problemi con gli script?**
```powershell
# Verifica esecuzione script
Get-ExecutionPolicy

# Se "Restricted", esegui:
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

**Errori di decifratura?**
- Verifica chiave identica in PowerShell e JavaScript
- Controlla che `db.enc` non sia corrotto
- Usa `.\decifra_database.ps1` per test

---

**© 2026 Avvocato Marcello Stanca - Firenze, Italy**
