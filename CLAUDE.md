# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

BiangBiang Kana is a dual-platform native mobile app (iOS + Android) that converts Japanese text (Kanji and Kana) to Romaji and translates Japanese text. It supports OCR from images and a live camera feed. The iOS and Android apps are **fully separate implementations** with no shared code — not a cross-platform framework.

Both apps are intentionally thin: the shared **BiangBiangUI** library renders every screen and owns navigation, History, the rate prompt, TTS, translation and the OCR pipeline. Each app supplies only a `BiangBiangConfig` (branding + one Japanese `LanguageProfile`) and the Romaji `Transliterator`.

## Build & Development Commands

### iOS (Swift + SwiftUI)

- **Open project:** `open "ios/BiangBiang Kana.xcodeproj"`
- **Build:** Xcode ⌘B or `xcodebuild -project "ios/BiangBiang Kana.xcodeproj" -scheme "BiangBiang Kana" build`
- **Run tests:** `xcodebuild test -project "ios/BiangBiang Kana.xcodeproj" -scheme "BiangBiang Kana" -destination 'platform=iOS Simulator,name=iPhone 16'`
- **Format code:** `swiftformat ./ios` (requires `brew install swiftformat`). **Run this whenever iOS code is modified.**

### Android (Kotlin + Jetpack Compose)

- **JDK (macOS):** Gradle requires a JDK. If no system JDK is installed, use the one bundled with Android Studio: `export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"` and prepend `$JAVA_HOME/bin` to `PATH` before invoking `./gradlew`.
- **Build debug:** `cd android && ./gradlew assembleDebug`
- **Run unit tests:** `cd android && ./gradlew test`
- **Run a single test class:** `cd android && ./gradlew test --tests "dev.veeso.biangbiangkana.KatakanaRomajiTest"`
- **Lint:** `cd android && ./gradlew lintDebug`

### Website (Tailwind CSS v3)

- **Build CSS:** `npx tailwindcss@3 -i ./site/input.css -o ./site/output.css --minify`. **Run this whenever website content under `site/` is modified.**

## Architecture

### iOS (`ios/BiangBiang Kana/`)

Config-only app over BiangBiangUI.

- **BiangBiang_KanaApp.swift** — `@main` entry point; hands `JapaneseConfig.japaneseConfig` to `BiangBiangRootView`.
- **Config/JapaneseConfig.swift** — the complete `BiangBiangConfig`: branding (accent `#BC002D`), one Japanese `LanguageProfile` (Hiragana/Katakana/Kanji script ranges, `.japanese` OCR recognizer, `ja-JP` TTS) with a single variant.
- **Services/RomajiTransliterator.swift** — Japanese→Romaji for one isolated script span via `CFStringTokenizer` Latin transcription (resolves Kanji readings as well as Kana). No third-party dependency.

Tests use Swift Testing (`@Test`) in `ios/BiangBiang KanaTests/`.

### Android (`android/app/src/main/java/dev/veeso/biangbiangkana/`)

Config-only app over BiangBiangUI.

- **MainActivity.kt** — entry point; passes `japaneseConfig()` to `BiangBiangRoot`.
- **config/JapaneseConfig.kt** — the complete `BiangBiangConfig`, mirroring the iOS profile (`OcrRecognizer.JAPANESE`, `ja-JP` TTS).
- **services/RomajiTransliterator.kt** — Japanese→Romaji via Kuromoji (IPADIC) tokenisation; the per-token Katakana reading is converted to Hepburn romaji by `KatakanaRomaji`. Kuromoji is an app-only dependency, never referenced by the library.

Dependency versions are managed in `android/gradle/libs.versions.toml`. Tests use JUnit 4 in `android/app/src/test/`.

### Span model (both platforms)

BiangBiangUI's `TextProcessingEngine` owns script-span detection, passthrough and spacing. The app `Transliterator` only romanises one already-isolated Japanese span. OCR (Japanese, native to ML Kit / Vision) and translation are owned by the library.

### UI Framework — BiangBiangUI

Both apps depend on the shared **BiangBiangUI** library (<https://github.com/veeso/BiangBiangUI>) for essentially the entire user interface — screens, navigation, components and theming. Treat it as a hard dependency: nearly every view in both apps relies on it, so changes to its API ripple across the whole UI layer.

- **iOS:** Swift Package Manager dependency declared in `ios/BiangBiang Kana.xcodeproj/project.pbxproj` (pinned in `Package.resolved`).
- **Android:** version `biangbiangui` in `android/gradle/libs.versions.toml`, consumed as `libs.biangbiang.ui`.

## Platform Configuration

- **iOS:** Min deployment target uses the iOS 15+ Translation API. Info.plist at `ios/BiangBiang-Kana-Info.plist`. Bundle identifier `dev.veeso.BiangBiang-Kana` (App Store ID still to be assigned).
- **Android:** `minSdk 26`, `targetSdk 36`, `compileSdk 36`, Java 11. Manifest requests CAMERA permission. Application ID and package: `dev.veeso.biangbiangkana`.
