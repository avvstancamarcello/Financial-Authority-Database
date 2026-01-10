## Financial Authority Database

---

## 📝 **STRUTTURA DEL DATABASE**

```json
{
  "Albania": {
    "country_name": "Albania",
    "flag": "🇦🇱",
    "isEU": false,
    "protectionLevel": "medio",
    "notes": "Testo descrittivo breve...",
    "financial_authority": {
      "name": "Autoriteti i Mbikëqyrjes Financiare",
      "abbreviation": "AMF",
      "homepage": "https://amf.gov.al",
      "fraudReportLink": "https://amf.gov.al/raportimi",
      "authorityEmail": "info@amf.gov.al",
      "social_links": {
        "linkedin": "https://linkedin.com/company/amf-albania",
        "twitter": "https://twitter.com/AMF_Albania"
      }
    }
  }
}
```

---

## ✏️ **ESEMPIO 1: Correggere Link Homepage**

**Scenario:** Link CONSOB Italia obsoleto

**PRIMA:**
```json
"Italy": {
  "country_name": "Italy",
  "flag": "🇮🇹",
  "financial_authority": {
    "name": "Commissione Nazionale per le Società e la Borsa",
    "abbreviation": "CONSOB",
    "homepage": "https://www.consob.it/web/consob/home-vecchio",  ← OBSOLETO
    ...
  }
}
```

**DOPO:**
```json
"Italy": {
  "country_name": "Italy",
  "flag": "🇮🇹",
  "financial_authority": {
    "name": "Commissione Nazionale per le Società e la Borsa",
    "abbreviation": "CONSOB",
    "homepage": "https://www.consob.it",  ← CORRETTO
    ...
  }
}
```

**Deploy veloce (NO cifratura):**
```powershell
cd "c:\Users\Utente\Financial-Authority-Database"
Copy-Item "c:\Users\Utente\BancheEuropa\financial_authorities_database.json" .
Remove-Item db.enc
git add financial_authorities_database.json
git commit -m "Fix: Corretto link homepage CONSOB Italia"
git push
```

---

## ➕ **ESEMPIO 2: Aggiungere Link Social Mancante**

**Scenario:** BaFin Germania manca LinkedIn

**PRIMA:**
```json
"Germany": {
  "financial_authority": {
    "name": "Bundesanstalt für Finanzdienstleistungsaufsicht",
    "abbreviation": "BaFin",
    "social_links": {
      "twitter": "https://twitter.com/BaFin_Bund"
    }
  }
}
```

**DOPO:**
```json
"Germany": {
  "financial_authority": {
    "name": "Bundesanstalt für Finanzdienstleistungsaufsicht",
    "abbreviation": "BaFin",
    "social_links": {
      "twitter": "https://twitter.com/BaFin_Bund",
      "linkedin": "https://www.linkedin.com/company/bafin"  ← AGGIUNTO
    }
  }
}
```

---

## 📧 **ESEMPIO 3: Correggere Email Autorità**

**Scenario:** Email FCA UK cambiata

**PRIMA:**
```json
"United Kingdom": {
  "financial_authority": {
    "name": "Financial Conduct Authority",
    "abbreviation": "FCA",
    "authorityEmail": "consumer.queries@fca.org.uk",  ← VECCHIA
    ...
  }
}
```

**DOPO:**
```json
"United Kingdom": {
  "financial_authority": {
    "name": "Financial Conduct Authority",
    "abbreviation": "FCA",
    "authorityEmail": "contact@fca.org.uk",  ← NUOVA
    ...
  }
}
```

---

## 🆕 **ESEMPIO 4: Aggiungere Nuovo Paese**

**Scenario:** Aggiungere Russia al database (109° paese)

**Posizione:** Dopo "Romania", prima di "Saudi Arabia"

**AGGIUNGERE:**
```json
"Russia": {
  "country_name": "Russia",
  "flag": "🇷🇺",
  "isEU": false,
  "protectionLevel": "medio",
  "notes": "La Banca Centrale della Federazione Russa (CBR) supervisiona istituzioni finanziarie, assicurazioni e mercati dei capitali.",
  "financial_authority": {
    "name": "Bank of Russia",
    "abbreviation": "CBR",
    "homepage": "https://www.cbr.ru/eng/",
    "fraudReportLink": "https://www.cbr.ru/reception/",
    "authorityEmail": "info@cbr.ru",
    "social_links": {
      "twitter": "https://twitter.com/centralbank_ru",
      "linkedin": "https://www.linkedin.com/company/bank-of-russia"
    }
  }
},
```

⚠️ **ATTENZIONE:** Non dimenticare la virgola finale!

**Deploy con cifratura:**
```powershell
cd "c:\Users\Utente\BancheEuropa"
.\cifra_database.ps1
cd "c:\Users\Utente\Financial-Authority-Database"
git add db.enc financial_authorities_database.json
git commit -m "Aggiunta Russia - 109 paesi totali"
git push
```

---

## 📝 **ESEMPIO 5: Aggiornare Note Descrittive**

**Scenario:** Espandere descrizione Singapore

**PRIMA:**
```json
"Singapore": {
  "notes": "Singapore ha una regolamentazione finanziaria molto rigida..."
}
```

**DOPO:**
```json
"Singapore": {
  "notes": "Singapore ha una regolamentazione finanziaria molto rigida. La Monetary Authority of Singapore (MAS) è riconosciuta come una delle autorità più efficienti al mondo per la supervisione bancaria e la lotta alle frodi finanziarie. Include protezione depositi fino a SGD 75,000 per depositante."
}
```

---

## 🔄 **ESEMPIO 6: Batch Update (10 Paesi)**

**Scenario:** Aggiornare fraudReportLink per paesi UE

**Paesi da modificare:**
- Italia, Francia, Germania, Spagna, Portogallo, Grecia, Belgio, Olanda, Austria, Svezia

**Workflow:**
```powershell
# 1. Apri JSON
code "c:\Users\Utente\BancheEuropa\financial_authorities_database.json"

# 2. Trova ogni paese e modifica:
#    "fraudReportLink": "https://..." (aggiorna URL)

# 3. Salva (Ctrl+S)

# 4. Deploy SENZA cifratura per test
cd "c:\Users\Utente\Financial-Authority-Database"
Copy-Item "c:\Users\Utente\BancheEuropa\financial_authorities_database.json" .
Remove-Item db.enc
git add financial_authorities_database.json
git commit -m "Batch update: fraud links 10 paesi UE"
git push

# 5. Testa sul sito (5 minuti)

# 6. Se OK, cifra
cd "c:\Users\Utente\BancheEuropa"
.\cifra_database.ps1
cd "c:\Users\Utente\Financial-Authority-Database"
git add db.enc
git commit -m "Database cifrato post-verifica batch update"
git push
```

---

## ⚠️ **ERRORI COMUNI DA EVITARE**

### ❌ **Errore 1: Virgola mancante**
```json
"Italy": { ... }   ← Manca virgola qui!
"Japan": { ... }
```
**Fix:**
```json
"Italy": { ... },  ← Aggiungi virgola
"Japan": { ... }
```

---

### ❌ **Errore 2: Virgola finale**
```json
{
  "Italy": { ... },
  "Japan": { ... },  ← Rimuovi questa virgola!
}
```
**Fix:**
```json
{
  "Italy": { ... },
  "Japan": { ... }   ← Nessuna virgola dopo ultimo elemento
}
```

---

### ❌ **Errore 3: Quote doppie non chiuse**
```json
"homepage": "https://www.consob.it  ← Manca " finale
```
**Fix:**
```json
"homepage": "https://www.consob.it"  ← OK
```

---

### ❌ **Errore 4: Carattere speciale non escaped**
```json
"notes": "L'autorità supervisiona..."  ← Apostrofo rompe JSON
```
**Fix 1 (escaped):**
```json
"notes": "L\\'autorità supervisiona..."
```
**Fix 2 (preferibile - usa apice dritto):**
```json
"notes": "L'autorità supervisiona..."  ← ' invece di '
```

---

## 🛠️ **STRUMENTI VALIDAZIONE JSON**

### **Online:**
1. **JSONLint:** https://jsonlint.com/
   - Incolla JSON → Valida
   - Trova errori sintassi

2. **JSON Formatter:** https://jsonformatter.org/
   - Formattazione automatica
   - Highlighting errori

### **VS Code:**
1. Apri JSON in VS Code
2. Errori mostrati con sottolineatura rossa
3. Hover mouse → vedi descrizione errore

### **PowerShell:**
```powershell
# Test rapido validità JSON
$json = Get-Content 'financial_authorities_database.json' -Raw
try {
    $json | ConvertFrom-Json | Out-Null
    Write-Host "✅ JSON valido" -ForegroundColor Green
} catch {
    Write-Host "❌ JSON NON valido: $($_.Exception.Message)" -ForegroundColor Red
}
```

---

## 📋 **CHECKLIST PRIMA DI COMMIT**

- [ ] JSON formattato correttamente (indentazione)
- [ ] Nessun errore di sintassi (virgole, quote)
- [ ] Link testati (clic per verificare funzionano)
- [ ] Email valide (formato corretto)
- [ ] Note senza caratteri strani (solo UTF-8)
- [ ] Validazione JSON passata (JSONLint o VS Code)
- [ ] File salvato (Ctrl+S)

**Se tutto OK:**
```powershell
# Test locale
cd "c:\Users\Utente\BancheEuropa"
$json = Get-Content 'financial_authorities_database.json' -Raw | ConvertFrom-Json
Write-Host "Paesi totali: $(($json.PSObject.Properties | Measure-Object).Count)"

# Deploy
cd "c:\Users\Utente\Financial-Authority-Database"
Copy-Item "c:\Users\Utente\BancheEuropa\financial_authorities_database.json" .
git add financial_authorities_database.json
git commit -m "Descrizione modifiche"
git push
```

---

## 🎯 **BEST PRACTICES**

1. **Modifiche piccole e frequenti** > Batch giganti
2. **Test sempre** prima di cifrare
3. **Commit message descrittivi**: "Fix link CONSOB" > "update"
4. **Backup locale** prima di modifiche grandi
5. **Cifra solo per deploy produzione**, non ogni modifica

---

**© 2026 Avvocato Marcello Stanca - Firenze, Italy**
