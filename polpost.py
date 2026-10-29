import requests
from bs4 import BeautifulSoup
import json, time
from datetime import datetime
from urllib.parse import urljoin

HEADERS = {"User-Agent": "Mozilla/5.0"}
results = []
base_url = "https://www.commissariatodips.it/"

r = requests.get(base_url, headers=HEADERS, timeout=15)
soup = BeautifulSoup(r.text, 'html.parser')
links = soup.find_all("a")

articoli = []
for a in links:
    title = a.get_text(strip=True)
    href = a.get("href")
    if not title or not href or len(title) < 15:
        continue
    # prendi solo articoli/notizie vere
    if any(k in title.lower() for k in ["truffa", "phishing", "smishing", "cripto", "online", "antiterrorismo"]):
        full_url = urljoin(base_url, href) # <- QUESTO sistema l'errore itarg
        if "commissariatodips.it" in full_url:
            articoli.append((title, full_url))
    if len(articoli) >= 10:
        break

print(f"Trovati {len(articoli)} articoli, scarico il testo...")

for title, full_url in articoli:
    try:
        r2 = requests.get(full_url, headers=HEADERS, timeout=15)
        soup2 = BeautifulSoup(r2.text, 'html.parser')
        paragraphs = soup2.find_all("p")
        body = " ".join([p.get_text(strip=True) for p in paragraphs if len(p.get_text()) > 30][:5])
        
        if len(body) < 20:
            body = title

        results.append({
            "fonte": "Polizia Postale",
            "url": full_url,
            "corpo_testo": body[:2000],
            "argomento": "Truffa",
            "parole_chiave": "truffa, phishing",
            "data_news": datetime.now().isoformat(),
            "lingua": "it"
        })
        print(f"OK: {title[:60]}")
        time.sleep(1)
    except Exception as e:
        print(f"Skip {full_url}: {e}")

with open("truffe_reali.json", "w", encoding="utf-8") as f:
    json.dump(results, f, indent=2, ensure_ascii=False)

print(f"\nFATTO! File salvato con {len(results)} voci complete.")
