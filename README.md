# SEKTOR Android v38 — kompilacja APK przez GitHub Actions

Projekt zawiera **oryginalne pliki aplikacji SEKTOR v38** (HTML, CSS, JS i grafiki) w `app/src/main/assets/www/` oraz kontener Android WebView.

## Jak zbudować bez komputera
1. W telefonie otwórz GitHub i utwórz **prywatne repozytorium** (np. `sektor-android`).
2. Prześlij **zawartość tego ZIP-a** do głównego katalogu repozytorium, zachowując strukturę folderów, w tym ukryty `.github/workflows/build-apk.yml`. Możesz użyć mobilnej przeglądarki w trybie komputerowym lub importu ZIP przez narzędzie GitHub obsługujące katalogi.
3. Wejdź do **Actions → Build SEKTOR APK → Run workflow**. Workflow uruchamia się także przy push do gałęzi `main`.
4. Po zielonym zakończeniu otwórz run i pobierz artefakt `SEKTOR-v38-debug-APK` (ZIP). Rozpakuj i zainstaluj `app-debug.apk` na Androidzie. Android może wymagać zgody na instalację z tego źródła.

## Uwagi
- To **debug APK**, podpisany automatycznie kluczem debug na runnerze GitHub. Nie jest przeznaczony do publikacji w Google Play.
- Aplikacja działa offline. `localStorage` wewnątrz WebView jest **oddzielny** od pamięci przeglądarki Brave/Chrome; dotychczasowe zadania nie przejdą automatycznie.
- Nie przeprowadzono rzeczywistej kompilacji APK ani testów na urządzeniu w tym środowisku. GitHub Actions przeprowadzi pierwszą kompilację.
- WebView może wymagać dodatkowego kodu natywnego dla importu/eksportu plików, systemowego wyboru plików i powiadomień. Te funkcje nie są tutaj obiecane jako działające.
- Dla stabilnych aktualizacji i dystrybucji przygotuj później **release signing key** i build release; debug buildy z różnych uruchomień mogą wymagać odinstalowania poprzedniej wersji (co usuwa lokalne dane).
