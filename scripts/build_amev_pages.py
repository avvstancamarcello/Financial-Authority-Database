#!/usr/bin/env python3
import hashlib
import shutil
import stat
import subprocess
import tempfile
import zipfile
from pathlib import Path, PurePosixPath

ROOT = Path(__file__).resolve().parents[1]
ARCHIVE = ROOT / "amev-verifica-prima.zip"
DESTINATION = ROOT / "verifica-prima di pagare" / "amevcheck"
ARCHIVE_SHA256 = "cb7091af169921e27d836b9b1a987b3ccd6b4a7a2414cae540b7da01f550eb0b"
PNPM = ["corepack", "pnpm@10.34.5"]


def extract_archive(archive, work):
    with zipfile.ZipFile(archive) as source:
        seen = set()
        entries = source.infolist()
        if len(entries) > 10000 or sum(entry.file_size for entry in entries) > 100_000_000:
            raise ValueError("ZIP exceeds extraction limits")
        for entry in entries:
            name = entry.filename
            path = PurePosixPath(name)
            mode = stat.S_IFMT(entry.external_attr >> 16)
            if (
                not name
                or "\\" in name
                or ":" in name
                or path.is_absolute()
                or ".." in path.parts
                or mode not in (0, stat.S_IFREG, stat.S_IFDIR)
                or str(path) in seen
                or not (work / name).resolve().is_relative_to(work.resolve())
            ):
                raise ValueError(f"Unsafe or duplicate ZIP entry: {name}")
            seen.add(str(path))
        for entry in entries:
            target = work / entry.filename
            if entry.is_dir():
                target.mkdir(parents=True, exist_ok=True)
            else:
                target.parent.mkdir(parents=True, exist_ok=True)
                with target.open("xb") as output, source.open(entry) as data:
                    shutil.copyfileobj(data, output)


def replace_once(path, old, new):
    text = path.read_text(encoding="utf-8")
    if text.count(old) != 1:
        raise ValueError(f"Source changed; review adaptation in {path.name}: {old}")
    path.write_text(text.replace(old, new), encoding="utf-8")


def adapt_frontend(work):
    replace_once(
        work / "client/index.html", '    <meta charset="UTF-8" />',
        '    <meta charset="UTF-8" />\n    <link rel="icon" href="data:," />',
    )
    app = work / "client/src/App.tsx"
    replace_once(app, 'import { Route, Switch }', 'import { Router, Route, Switch }')
    replace_once(
        app, "          <Switch>",
        '          <Router base={new URL(".", window.location.href).pathname.replace(/\\/$/, "")}>\n'
        "          <Switch>",
    )
    replace_once(
        app, '            <Route path="/" component={Home} />',
        '            <Route path="/" component={Home} />\n'
        '            <Route path="/index.html" component={Home} />',
    )
    replace_once(app, 'path="/rapporto"', 'path="/rapporto.html"')
    replace_once(app, "          </Switch>", "          </Switch>\n          </Router>")
    for name in ("pages/Home.tsx", "pages/Report.tsx", "components/SiteChrome.tsx"):
        path = work / "client/src" / name
        text = path.read_text(encoding="utf-8")
        text = text.replace('"/rapporto', '"./rapporto.html')
        text = text.replace('"/#', '"./#').replace('href="/"', 'href="./"')
        text = text.replace(
            "/manus-storage/amev-globe-hero_c132435c.png",
            "./images/globo-terrestre.png",
        ).replace(
            "/manus-storage/amev-verification-still_c41cf1a7.png",
            "./images/bussola-e-cristallo.png",
        )
        path.write_text(text, encoding="utf-8")
    home = work / "client/src/pages/Home.tsx"
    replace_once(
        home,
        "La valutazione ha trovato alcune cose da correggere prima di promuovere AMEV come strumento antifrode: la dicitura «129» rimasta in una pagina, percorsi di installazione PWA che rispondono 404 e link senza data di verifica per singolo record.",
        "Questa guida rende espliciti gli elementi del suo metodo: collegamenti alle fonti ufficiali, una checklist preventiva e una distinzione chiara tra informazioni orientative e verifiche che spettano alle autorità o alla propria banca.",
    )
    for old, new in (
        ("Datate ogni collegamento e pubblicate un changelog.",
         "Fonti collegate e contesto temporale dichiarato."),
        ("Distinguete autorità, registri, warning e segnalazioni.",
         "Autorità, registri e avvisi distinti per funzione."),
        ("Evitate etichette come «più completo» o «sito ufficiale» se non dimostrabili.",
         "Nessuna promessa di primato o certificazione di sicurezza."),
    ):
        replace_once(home, old, new)
    report = work / "client/src/pages/Report.tsx"
    for old, new in (
        ("DOSSIER / ANALISI COMPARATIVA", "DOSSIER / CRITERI DI AFFIDABILITÀ"),
        ("Le fonti, i limiti,<br /><em>le possibilità.</em>",
         "Le fonti, il metodo,<br /><em>i confini.</em>"),
        ("L’analisi completa della directory AMEV, dei repertori analoghi e dell’uso corretto come strumento di orientamento antitruffa.",
         "Gli elementi di affidabilità adottati nella guida AMEV: fonti verificabili, percorso preventivo e limiti dichiarati, per lettori e sistemi di analisi automatica."),
        ("<Clock3 size={16} /> 28 settembre 2026",
         "<Clock3 size={16} /> Revisione editoriale: 5 ottobre 2026"),
        ("Le fonti originali sono collegate direttamente. Le informazioni del dossier riflettono la verifica svolta il 28 settembre 2026; controlla lo stato attuale sui siti ufficiali.",
         "Riferimenti conservati dall’analisi progettuale del 28 settembre 2026. La revisione editoriale del 5 ottobre 2026 descrive la guida attuale e non costituisce una nuova verifica dei siti esterni: consulta sempre lo stato aggiornato presso la fonte ufficiale."),
    ):
        replace_once(report, old, new)
    content = work / "client/src/content/amev-report.md"
    original = content.read_text(encoding="utf-8")
    _, references = original.split("## References", 1)
    revised = (ROOT / "scripts/amev-report.txt").read_text(encoding="utf-8")
    content.write_text(revised + "\n## References" + references, encoding="utf-8")
    images = work / "client/public/images"
    images.mkdir()
    for name in ("globo-terrestre.png", "bussola-e-cristallo.png"):
        source = ROOT / name
        if source.read_bytes()[:8] != b"\x89PNG\r\n\x1a\n":
            raise ValueError(f"Not a PNG image: {source}")
        shutil.copyfile(source, images / name)
    shutil.rmtree(work / "client/public/__manus__")
    (work / "client/public/.gitkeep").unlink()
    shutil.copyfile(work / "client/index.html", work / "client/rapporto.html")
    shutil.copyfile(ROOT / "scripts/amev-pages.vite.config.ts", work / "vite.config.ts")


def main():
    if hashlib.sha256(ARCHIVE.read_bytes()).hexdigest() != ARCHIVE_SHA256:
        raise ValueError("ZIP changed: inspect sources, scripts and lockfile before updating the hash")
    with tempfile.TemporaryDirectory(prefix="amev-pages-") as directory:
        work = Path(directory)
        extract_archive(ARCHIVE, work)
        adapt_frontend(work)
        subprocess.run(
            PNPM + ["install", "--frozen-lockfile", "--ignore-scripts",
                    "--config.manage-package-manager-versions=false"],
            cwd=work, check=True,
        )
        subprocess.run(PNPM + ["run", "check"], cwd=work, check=True)
        subprocess.run(PNPM + ["exec", "vite", "build"], cwd=work, check=True)
        output = work / "dist/public"
        if not (output / "index.html").is_file() or not (output / "rapporto.html").is_file():
            raise ValueError("Missing frontend entry points")
        if DESTINATION.is_symlink() or DESTINATION.parent.is_symlink():
            raise ValueError("Refusing to replace a symlink destination or parent")
        if DESTINATION.exists():
            shutil.rmtree(DESTINATION)
        DESTINATION.parent.mkdir(parents=True, exist_ok=True)
        shutil.copytree(output, DESTINATION)
        print(f"Frontend copied to {DESTINATION}; original ZIP unchanged")


if __name__ == "__main__":
    main()
