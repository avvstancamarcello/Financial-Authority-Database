#!/usr/bin/env python3
"""Prepare lossless PNG candidates without replacing production assets.

Development dependency: Pillow (python3 -m pip install Pillow).
Run: python3 scripts/prepare_orbital_previews.py
Candidates go to a new system temporary directory, printed on startup.
Private chunks (including caBX provenance) are conservatively left untouched.
"""

from io import BytesIO
from pathlib import Path
from tempfile import mkdtemp

from PIL import Image, PngImagePlugin

ROOT = Path(__file__).resolve().parents[1]
FILES = (
    "preview-consob.png",
    "preview-fca.png",
    "preview-FR-AMF.png",
    "preview-DE-BAFIN.png",
    "preview-ES-CNMV.png",
)


def prepare(source: Path, target: Path):
    if source.resolve() == target.resolve():
        raise ValueError("Candidate must not overwrite the source")
    target.unlink(missing_ok=True)
    if not source.is_file():
        print(f"SKIP: {source.name}: missing source")
        return

    with Image.open(source) as original:
        original.load()
        if original.format != "PNG" or original.n_frames != 1:
            print(f"SKIP: {source.name}: not a single-frame PNG")
            return
        if original.private_chunks:
            print(f"SKIP: {source.name}: private/provenance chunks preserved in source")
            return

        metadata = PngImagePlugin.PngInfo()
        for key, value in original.text.items():
            metadata.add_itxt(
                key, value, lang=getattr(value, "lang", ""), tkey=getattr(value, "tkey", "")
            )
        options = {"optimize": True, "pnginfo": metadata}
        for key in ("icc_profile", "dpi", "transparency", "exif"):
            if key in original.info:
                options[key] = original.info[key]
        buffer = BytesIO()
        original.save(buffer, format="PNG", **options)
        candidate = buffer.getvalue()
        with Image.open(BytesIO(candidate)) as checked:
            checked.load()
            if (
                checked.size != original.size
                or checked.mode != original.mode
                or checked.convert("RGBA").tobytes() != original.convert("RGBA").tobytes()
            ):
                raise ValueError(f"Pixel/dimension validation failed: {source.name}")
            if checked.info != original.info:
                print(f"SKIP: {source.name}: metadata could not be preserved")
                return

    before, after = source.stat().st_size, len(candidate)
    saving = 100 * (before - after) / before
    status = "CANDIDATE" if after < before else "UNCHANGED"
    print(f"{status}: {source.name}: {before:,} -> {after:,} bytes ({saving:.2f}% saved)")
    if after < before:
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(candidate)


def main():
    output = Path(mkdtemp(prefix="orbital-preview-candidates-"))
    print(f"Candidate directory: {output}")
    for name in FILES:
        prepare(ROOT / "authority" / name, output / "authority" / name)


if __name__ == "__main__":
    main()
