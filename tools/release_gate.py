from pathlib import Path
import xml.etree.ElementTree as ET

root=Path(__file__).resolve().parents[1]
files=list(root.rglob("*"))

for p in files:
    if p.suffix==".xml":
        ET.parse(p)

manifest=(root/"app/src/main/AndroidManifest.xml").read_text()
gradle=(root/"app/build.gradle").read_text()

# Scan application/build configuration only; do not scan gate scripts themselves,
# because the deny-list is intentionally present in this file.
scan_paths=[
    root/"app",
    root/"build.gradle",
    root/"settings.gradle",
    root/"gradle.properties",
]
parts=[]
for base in scan_paths:
    if base.is_file():
        parts.append(base.read_text(errors="ignore"))
    elif base.exists():
        for p in base.rglob("*"):
            if p.is_file() and p.stat().st_size < 500000:
                parts.append(p.read_text(errors="ignore"))
blob="\n".join(parts).lower()

assert "android.permission.internet" not in manifest.lower()
assert 'allowbackup="false"' in manifest.lower()
assert 'usescleartexttraffic="false"' in manifest.lower()

for forbidden in ("com.google.firebase","play-services","com.google.android.gms.ads","firebase-analytics"):
    assert forbidden not in blob, f"Forbidden dependency marker: {forbidden}"

locales=["values","values-ar","values-tr","values-es","values-de","values-it","values-fr","values-ur","values-fa","values-ru"]
for x in locales:
    assert (root/f"app/src/main/res/{x}/strings.xml").exists(), f"Missing locale: {x}"

assert (root/"LICENSE").exists()
assert "versionCode 6" in gradle
assert "versionName '1.3.0'" in gradle
print("Release Gate: PASS")
