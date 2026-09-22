#!/usr/bin/env python3
"""Generate app icons for all desktop/web platforms from the source SVG.

Inputs:  demoApp/desktopApp/appIcons/shadcn-multiplatform-logo.svg
Outputs (checked into the repo so gradle builds don't need python):
  demoApp/desktopApp/appIcons/generated/*.png         (png family)
  demoApp/desktopApp/appIcons/LinuxIcon.png           (512, for deb/rpm packaging)
  demoApp/desktopApp/appIcons/WindowsIcon.ico         (multi-size ico)
  demoApp/desktopApp/appIcons/MacosIcon.icns          (png-based icns)
  demoApp/desktopApp/src/main/resources/icons/*.png   (runtime window icon + linux desktop entry)
  demoApp/webApp/src/commonMain/resources/favicon-*.png
"""
import os
import struct
import subprocess
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parent.parent
APP_ICONS = ROOT / "demoApp" / "desktopApp" / "appIcons"
SOURCE_SVG = APP_ICONS / "shadcn-multiplatform-logo.svg"
SQUARE_SVG = APP_ICONS / "shadcn-multiplatform-square.svg"
RESOURCES = ROOT / "demoApp" / "desktopApp" / "src" / "main" / "resources" / "icons"
WEB = ROOT / "demoApp" / "webApp" / "src" / "commonMain" / "resources"

PNG_SIZES = [16, 24, 32, 40, 48, 64, 96, 128, 192, 256, 512, 1024]
ICO_SIZES = [16, 24, 32, 48, 64, 128, 256]
# icns chunk types: name -> pixel size (PNG-compressed chunks)
ICNS_SIZES = {
    "ic07": 128,
    "ic08": 256,
    "ic09": 512,
    "ic10": 1024,
    "ic11": 32,
    "ic12": 64,
    "ic13": 256,
    "ic14": 512,
}


def normalize_svg() -> None:
    """The square 64x64 SVG is the maintained source (logo centered, junk stripped);
    regenerate it only if the master re-exports the logo."""
    if SQUARE_SVG.exists():
        print(f"square svg found -> {SQUARE_SVG}")
        return
    src = SOURCE_SVG.read_text(encoding="utf-8")
    # strip inkscape/sodipodi namespaces blocks and attrs we do not need
    junk_markers = ["<sodipodi:namedview", "</sodipodi:namedview>", "<inkscape:grid", "</inkscape:grid>"]
    # drop junk elements (non-recursive simple scan over their blocks)
    for open_tag, close_tag in [
        ("<sodipodi:namedview", "</sodipodi:namedview>"),
        ("<inkscape:grid", "</inkscape:grid>"),
    ]:
        while True:
            i = src.find(open_tag)
            if i == -1:
                break
            j = src.find(close_tag, i)
            src = src[:i] + src[j + len(close_tag):]
    # drop hidden path and out-of-canvas ellipses/layer2
    # remove the hidden path2 element entirely
    i = src.find('id="path2"')
    if i != -1:
        start = src.rfind("<path", 0, i)
        end = src.find("/>", i) + 2
        src = src[:start] + src[end:]
    # remove layer2 group with out-of-canvas ellipses
    i = src.find('id="layer2"')
    if i != -1:
        start = src.rfind("<g", 0, i)
        end = src.find("</g>", i) + 4
        src = src[:start] + src[end:]
    # remove the trailing out-of-canvas ellipse
    i = src.find('id="path19"')
    if i != -1:
        start = src.rfind("<ellipse", 0, i)
        end = src.find("/>", i) + 2
        src = src[:start] + src[end:]
    # square canvas: 64x64, center the 56x64 content
    src = src.replace('width="56"\n   height="64"\n   viewBox="0 0 56 64"',
                      'width="64"\n   height="64"\n   viewBox="0 0 64 64"')
    src = src.replace('<rect\n         width="56"', '<rect\n         x="-4"\n         width="64"', 1)
    src = src.replace('clip-path="url(#clip0_657_7666)"',
                      'transform="translate(4,0)" clip-path="url(#clip0_657_7666)"')
    SQUARE_SVG.write_text(src, encoding="utf-8")
    print(f"normalized svg -> {SQUARE_SVG}")


def render_png(size: int, out: Path) -> None:
    subprocess.run(
        ["inkscape", str(SQUARE_SVG), "--export-type=png",
         f"--export-width={size}", f"--export-height={size}",
         f"--export-filename={out}"],
        check=True, capture_output=True,
    )


def write_ico(pngs: dict, out: Path) -> None:
    # ICO container: ICONDIR + ICONDIRENTRY per image + image blobs (PNG allowed for 256 and smaller on XP+)
    count = len(pngs)
    header = struct.pack("<HHH", 0, 1, count)
    entries = b""
    blobs = []
    offset = 6 + 16 * count
    for size in sorted(pngs):
        data = pngs[size]
        w = 0 if size >= 256 else size
        h = 0 if size >= 256 else size
        entries += struct.pack(
            "<BBBBHHII", w, h, 0, 0, 1, 32, len(data), offset,
        )
        blobs.append(data)
        offset += len(data)
    out.write_bytes(header + entries + b"".join(blobs))


def write_icns(pngs: dict, out: Path) -> None:
    chunks = b""
    for ctype, size in ICNS_SIZES.items():
        png = pngs.get(size)
        if png is None:
            continue
        chunks += ctype.encode("ascii") + struct.pack(">I", len(png) + 8) + png
    out.write_bytes(b"icns" + struct.pack(">I", len(chunks) + 8) + chunks)


def main() -> None:
    print("normalizing source svg...")
    normalize_svg()

    out_dir = APP_ICONS / "generated"
    out_dir.mkdir(parents=True, exist_ok=True)
    RESOURCES.mkdir(parents=True, exist_ok=True)
    (WEB / "favicon").mkdir(parents=True, exist_ok=True)

    pngs: dict[int, Path] = {}
    for size in PNG_SIZES:
        out = out_dir / f"icon-{size}.png"
        render_png(size, out)
        pngs[size] = out
        print(f"png {size}x{size} -> {out}")

    # packaging placeholders referenced by desktopApp/build.gradle.kts
    (APP_ICONS / "LinuxIcon.png").write_bytes(pngs[512].read_bytes())
    print("linux packaging icon -> LinuxIcon.png")

    write_ico({s: pngs[s].read_bytes() for s in ICO_SIZES}, APP_ICONS / "WindowsIcon.ico")
    print("windows ico -> WindowsIcon.ico")

    write_icns({s: pngs[s].read_bytes() for s in PNG_SIZES}, APP_ICONS / "MacosIcon.icns")
    print("macos icns -> MacosIcon.icns")

    # runtime resources: window icon (taskbar) + linux temporary desktop entry
    # full hicolor size family requested by the master + scalable svg
    hicolor_sizes = [16, 24, 32, 40, 48, 64, 96, 128, 256, 512]
    for size in hicolor_sizes:
        (RESOURCES / f"icon-{size}.png").write_bytes(pngs[size].read_bytes())
    (RESOURCES / "icon-scalable.svg").write_text(SQUARE_SVG.read_text(encoding="utf-8"))
    print("runtime resources -> resources/icons/")

    # web favicon
    for size in (32, 192, 512):
        (WEB / "favicon" / f"favicon-{size}.png").write_bytes(pngs[size].read_bytes())
    (WEB / "favicon" / "favicon.svg").write_text(SQUARE_SVG.read_text(encoding="utf-8"))
    print("web favicon -> webApp resources")

    print("done.")


if __name__ == "__main__":
    main()
