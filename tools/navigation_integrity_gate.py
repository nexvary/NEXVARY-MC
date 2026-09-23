from pathlib import Path
root=Path(__file__).resolve().parents[1]
main=(root/"app/src/main/java/com/nexvary/mc/MainActivity.java").read_text()
about=(root/"app/src/main/java/com/nexvary/mc/AboutActivity.java").read_text()
links=(root/"app/src/main/java/com/nexvary/mc/AppLinks.java").read_text()
checks={
"Home": "buildHome()" in main,
"About route": "AboutActivity.class" in main,
"Photo picker": "ACTION_OPEN_DOCUMENT" in main,
"Review": "showReview" in main,
"Clean": "MetadataCleaner.clean" in main,
"Clean More": "clean_more" in main,
"UI Back": "getString(R.string.back)" in main and "finish()" in about,
"Android Back": "onBackPressed" in main,
"Website": "https://nexvary.com/" in links,
"Facebook": "facebook.com/share/14p9krEn5ij" in links,
"Email": "info@nexvary.com" in links,
"YouTube": "youtube.com/@NexvaryInc" in links,
"X": "x.com/Nexvary" in links,
"About links clickable": "ACTION_VIEW" in about,
"Share intents": "ACTION_SEND" in main,
}
failed=[k for k,v in checks.items() if not v]
for k,v in checks.items(): print(("PASS" if v else "FAIL"),k)
print(f"Navigation Integrity Gate: {len(checks)-len(failed)}/{len(checks)}")
raise SystemExit(1 if failed else 0)
