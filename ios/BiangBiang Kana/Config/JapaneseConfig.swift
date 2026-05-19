//
//  JapaneseConfig.swift
//  BiangBiang Kana
//
//  The complete `BiangBiangConfig` for BiangBiang Kana: one Japanese
//  `LanguageProfile` with a single variant. The library renders every
//  screen from this data.
//

import BiangBiangUI
internal import CoreFoundation

enum JapaneseConfig {
    @MainActor
    static let japaneseConfig: BiangBiangConfig = .init(
        branding: Branding(
            appName: "BiangBiang Kana",
            accentColorHex: "#BC002D",
            logoAssetName: "logo",
            buttonLogoAssetName: "android_button_ico",
            githubRepo: "veeso/BiangBiang-Kana",
            supportEmail: "info@veeso.dev",
            appStoreId: "6754869174",
            playStoreId: "dev.veeso.biangbiangkana"
        ),
        languages: [
            LanguageProfile(
                id: "japanese",
                displayName: "Japanese",
                // Hiragana, Katakana, Katakana phonetic extensions and
                // CJK Unified Ideographs (Kanji).
                scriptRanges: [
                    0x3040 ... 0x309F,
                    0x30A0 ... 0x30FF,
                    0x31F0 ... 0x31FF,
                    0x4E00 ... 0x9FFF,
                ],
                ocrRecognizer: .japanese,
                variants: [
                    LanguageVariant(
                        id: "japanese",
                        displayName: "Japanese",
                        transliterator: RomajiTransliterator(),
                        ttsLanguageCode: "ja-JP",
                        translatable: true
                    ),
                ]
            ),
        ],
        extraSettings: [],
        plugins: [],
        features: FeatureFlags(),
        strings: [
            "inputTitle": "Japanese",
            "outputTitle": "Romaji",
            "appSubtitle": "Convert Japanese to Romaji",
        ],
        minimumOcrScaleFactor: 0.7,
    )
}
