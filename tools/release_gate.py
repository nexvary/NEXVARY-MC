from pathlib import Path
import xml.etree.ElementTree as ET
root=Path(__file__).resolve().parents[1]
files=list(root.rglob("*"))
for p in files:
 if p.suffix==".xml": ET.parse(p)
manifest=(root/"app/src/main/AndroidManifest.xml").read_text()
gradle=(root/"app/build.gradle").read_text()
bad=["com.google.firebase","play-services","analytics","ads"]
blob="\n".join(p.read_text(errors="ignore") for p in files if p.is_file() and p.stat().st_size<500000)
assert "android.permission.INTERNET" not in manifest
assert 'allowBackup="false"' in manifest
assert 'usesCleartextTraffic="false"' in manifest
assert all(x not in blob.lower() for x in bad)
locales=["values","values-ar","values-tr","values-es","values-de","values-it","values-fr","values-ur","values-fa","values-ru"]
for x in locales: assert (root/f"app/src/main/res/{x}/strings.xml").exists()
assert (root/"LICENSE").exists()
assert "versionCode 6" in gradle and "versionName '1.3.0'" in gradle
print("Release Gate: PASS")
