# 🌍 COUNTRY_DOMAINS - Strategia Multi-Dominio Internazionale

## Struttura della Strategia

Ogni sottocartella contiene il template per un dominio nazionale specifico.

| Paese | Dominio Suggerito | Lingua | Autorità Locale | Popolazione |
|-------|-------------------|--------|-----------------|-------------|
| 🇫🇷 Francia | tutelatruffe.fr / protectionfraude.fr | Francese | AMF | 68M |
| 🇩🇪 Germania | tutelatruffe.de / betrugschutz.de | Tedesco | BaFin | 84M |
| 🇵🇱 Polonia | tutelatruffe.pl / ochronaoszustwa.pl | Polacco | KNF | 38M |
| 🇬🇧 Regno Unito | tutelatruffe.co.uk / scamprotection.co.uk | Inglese | FCA | 67M |
| 🇪🇬 Egitto | tutelatruffe.eg / حمايةالاحتيال.eg | Arabo | FRA Egypt | 104M |

## Checklist per ogni dominio

- [ ] Registrare dominio ccTLD
- [ ] Configurare hosting/CDN locale
- [ ] Aggiungere Google Search Console per ogni dominio
- [ ] Configurare Google AdSense separato o stesso account
- [ ] Verificare hreflang tags cross-domain
- [ ] Testare velocità locale (PageSpeed per paese)

## Struttura hreflang (da inserire in TUTTI i siti)

```html
<link rel="alternate" hreflang="it" href="https://tutelatruffe.it/" />
<link rel="alternate" hreflang="fr" href="https://tutelatruffe.fr/" />
<link rel="alternate" hreflang="de" href="https://tutelatruffe.de/" />
<link rel="alternate" hreflang="pl" href="https://tutelatruffe.pl/" />
<link rel="alternate" hreflang="en-GB" href="https://tutelatruffe.co.uk/" />
<link rel="alternate" hreflang="ar-EG" href="https://tutelatruffe.eg/" />
<link rel="alternate" hreflang="x-default" href="https://tutelatruffe.it/" />
```

## Differenziazione Contenuti (Anti-Duplicate)

Ogni template nazionale include:
1. **Hero con autorità LOCALE** in primo piano
2. **Ordinamento database** con paese locale in cima
3. **Meta description localizzata** unica
4. **Titolo SEO** con nome paese
5. **Sezione notizie locali** (placeholder per feed RSS)
6. **Footer con riferimenti legali locali**

## Costi Stimati Annuali

| Voce | 5 Domini | 20 Domini | 50 Domini |
|------|----------|-----------|-----------|
| Domini ccTLD | €75-150 | €300-600 | €750-1500 |
| Hosting CDN | €60-120 | €200-400 | €400-800 |
| **Totale** | **€135-270** | **€500-1000** | **€1150-2300** |

## ROI Potenziale

Con 1000 visitatori/mese per dominio e CPC medio €0.10:
- 5 domini: 5000 visite → €50-150/mese
- 20 domini: 20000 visite → €200-600/mese
- 50 domini: 50000 visite → €500-1500/mese

---
© 2026 Avv. Marcello Stanca - Strategia Multi-Dominio
