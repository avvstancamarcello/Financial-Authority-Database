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
