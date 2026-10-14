# 🔍 Guida Registrazione Motori di Ricerca

## Stato Attuale
| Motore | Stato | File Verifica |
|--------|-------|---------------|
| ✅ Google | Verificato | `googlea8b1c78253883a0a.html` |
| ⏳ Bing | Da registrare | `BingSiteAuth.xml` |
| ⏳ Yandex | Da registrare | `yandex_verification.html` |

---

## 1️⃣ BING WEBMASTER TOOLS (Copre anche Yahoo, DuckDuckGo, Ecosia)

### Link: https://www.bing.com/webmasters

### Passaggi:
1. **Accedi** con account Microsoft (Outlook, Hotmail, ecc.)
2. **Aggiungi sito**: `https://www.amevfirenze.it/`
3. **Metodo verifica**: Scegli "XML File"
4. **Codice**: Bing ti darà un codice alfanumerico
5. **Modifica** `BingSiteAuth.xml`:
   - Apri il file
   - Sostituisci `YOUR_BING_VERIFICATION_CODE` con il codice ricevuto
6. **Carica** il file nella root del sito (GitHub Pages)
7. **Verifica** cliccando il pulsante su Bing
8. **Invia Sitemap** in **Sitemaps → Submit Sitemap**:
   - Principale: `https://www.amevfirenze.it/sitemap.xml`
   - Financial Authority Database: `https://www.amevfirenze.it/sitemap-financial-authorities.xml`
9. **Dopo la pubblicazione**, controlla che la sitemap dedicata risponda con HTTP 200 e XML valido; verifica in Bing Webmaster Tools gli errori di scansione e lo stato dei singoli URL.

### Sitemap dedicata alle autorità finanziarie

- Usa gli URL canonici realmente presenti: indice `/?lang=en` e pagine sotto `/authority/`. Gli slug proposti come `/consob-orbital-webapp-italia` e `/financial-authority-database` non corrispondono a pagine di questo repository e non vanno inviati.
- Include CONSOB, CNMV, FCA, AMF, BaFin, FMA, SEC, IOSCO, ECMA, ESMA, BCEAO e le micropagine esistenti di Etiopia e Polinesia francese.
- È dichiarata in `robots.txt` ed è utilizzabile anche da altri motori; non sostituisce la sitemap principale né quella multilingua.
- Mantieni gli URL sincronizzati con i canonical delle pagine e con `sitemap.xml` quando aggiungi, rinomini o rimuovi pagine authority.
- `priority` e `changefreq` sono indicazioni facoltative, non comandi: non forzano la scansione, l'indicizzazione o il posizionamento. `daily` e `weekly` rappresentano frequenze suggerite, non aggiornamenti verificati. Non è indicato `lastmod` senza una data attendibile di modifica del contenuto.
- Non sono garantiti tempi di 24–72 ore né l'indicizzazione di tutte le pagine. La presenza su Google e l'assenza su Bing vanno verificate nei rispettivi strumenti webmaster; una sitemap da sola non dimostra la causa di eventuali esclusioni.
- IndexNow è un meccanismo separato di notifica degli URL aggiornati: questa modifica non genera chiavi né invia notifiche IndexNow.

### Vantaggi:
- ✅ Una registrazione = 4 motori (Bing, Yahoo, DuckDuckGo, Ecosia)
- ✅ ~8-10% del traffico di ricerca globale
- ✅ Più alto in USA, UK, Germania
- ✅ Strumenti SEO gratuiti simili a Google Search Console

---

## 2️⃣ YANDEX WEBMASTER (Russia e Est Europa)

### Link: https://webmaster.yandex.com

### Passaggi:
1. **Crea account** Yandex (gratuito)
2. **Aggiungi sito**: `https://tutelatruffe.it`
3. **Metodo verifica**: HTML file
4. **Rinomina** `yandex_verification.html` con il nome esatto fornito
5. **Carica** nella root del sito
6. **Verifica** su Yandex
7. **Invia Sitemap**: `https://tutelatruffe.it/sitemap.xml`

### Vantaggi:
- ✅ Dominante in Russia (~55% market share)
- ✅ Forte in Bielorussia, Kazakistan, Ucraina
- ✅ Utile per traffico dall'Est Europa

---

## 3️⃣ BAIDU (Solo se vuoi mercato cinese)

### Link: https://ziyuan.baidu.com

⚠️ **Nota**: Richiede:
- Numero di telefono cinese
- Sito in cinese
- Server in Cina (preferibile)

👉 **Consiglio**: Salta per ora, a meno che non pianifichi espansione in Cina.

---

## 📊 Quote di Mercato Globali (2025-2026)

```
Google        ████████████████████████████████████░░░  88%
Bing          ████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   6%
Yandex        ██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   3%
Yahoo         █░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   2%
DuckDuckGo    █░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   1%
```

---

## ✅ Checklist Post-Registrazione

### Per ogni motore:
- [ ] Sito verificato
- [ ] Sitemap inviata
- [ ] Nessun errore di crawling
- [ ] Pagine indicizzate visibili

### File da caricare su GitHub Pages:
- [ ] `BingSiteAuth.xml` (con codice inserito)
- [ ] File Yandex rinominato (opzionale)

---

## 🔗 Link Utili

| Strumento | URL |
|-----------|-----|
| Google Search Console | https://search.google.com/search-console |
| Bing Webmaster Tools | https://www.bing.com/webmasters |
| Yandex Webmaster | https://webmaster.yandex.com |
| DuckDuckGo | Usa indice Bing (nessuna registrazione) |
| Ecosia | Usa indice Bing (nessuna registrazione) |

---

## 📝 Note

- **robots.txt**: Già configurato correttamente, permette tutti i crawler
- **sitemap.xml**: Già presente e aggiornata
- **hreflang**: Già implementati per versioni multilingua

*Ultimo aggiornamento: Gennaio 2026*
