# SEKTOR B — Galactic Command (Android APK)

Samodzielny projekt Gradle (Java 17, Android Gradle Plugin 8.7.3, Gradle 8.10.2).
Zawiera pliki WebView SEKTOR B, bez koniecznosci pobierania zewnetrznych grafik.

## Budowanie przez GitHub Actions z telefonu
1. Utworz NOWE repozytorium, np. SEKTOR-B, aby nie nadpisac SEKTOR A.
2. Wgraj zawartosc ZIP do katalogu glownego repozytorium (foldery `app`, `.github` i pliki Gradle).
3. W GitHub przejdz do Actions -> Build SEKTOR B APK -> Run workflow.
   Workflow uruchamia sie tez po push na `main`.
4. Po zielonym build pobierz artifact `SEKTOR-B-debug-APK` i rozpakuj ZIP.
5. Zainstaluj `app-debug.apk` na telefonie.

## Wazne
- Nazwa aplikacji: SEKTOR B; identyfikator: pl.sektor.galacticcommand.
- Wersja: B-1.0.0; zmien `versionCode` i `versionName` w `app/build.gradle` przy aktualizacji.
- Nie nadpisuje SEKTOR A: ma inny `applicationId` i oddzielny klucz localStorage.
- Eksport JSON: systemowe okno wyboru miejsca zapisu (Android Storage Access Framework).
- Import JSON: systemowy wybor pliku.
- Dziala offline, bez uprawnienia INTERNET.
- Jest to APK debug, do uzytku osobistego/testowego, nie podpisany kluczem release.

## Poprawka B-1.0.1
Naprawiono przewijanie listy zadan w WebView: mapa uzywa flex-start zamiast flex-end.

B-1.0.6: short task lists stay anchored to the bottom; long lists remain scrollable.
