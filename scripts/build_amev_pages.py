#!/usr/bin/env python3
import hashlib
import re
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
        text = re.sub(
            r'<img className="hero-art" src="/manus-storage/[^"]+"[^>]* />',
            "", text,
        )
        text = re.sub(
            r'<img src="/manus-storage/[^"]+"[^>]* />',
            '<p className="art-unavailable">Illustrazione non inclusa nello ZIP originale.</p>',
            text,
        )
        path.write_text(text, encoding="utf-8")
    css = work / "client/src/index.css"
    with css.open("a", encoding="utf-8") as output:
        output.write(
            "\n.editorial-visual, .report-hero__visual { background: var(--paper); }\n"
            ".art-unavailable { padding: 2rem; color: var(--ink); font-size: 14px; }\n"
        )
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
