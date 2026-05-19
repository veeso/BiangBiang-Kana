package dev.veeso.biangbiangkana.config

import dev.veeso.biangbiangkana.services.RomajiTransliterator
import dev.veeso.biangbiangui.config.BiangBiangConfig
import dev.veeso.biangbiangui.config.Branding
import dev.veeso.biangbiangui.config.FeatureFlags
import dev.veeso.biangbiangui.config.LanguageProfile
import dev.veeso.biangbiangui.config.LanguageVariant
import dev.veeso.biangbiangui.config.OcrRecognizer

/**
 * The complete [BiangBiangConfig] for BiangBiang Kana: one Japanese
 * [LanguageProfile] with a single variant. The library renders every
 * screen from this data. Logo asset names are the app's `drawable`
 * resource names (`logo`, `logo_button_ico`).
 */
fun japaneseConfig(): BiangBiangConfig {
    val romaji = RomajiTransliterator()
    return BiangBiangConfig(
        branding = Branding(
            appName = "BiangBiang Kana",
            accentColorHex = "#BC002D",
            logoAssetName = "logo",
            buttonLogoAssetName = "logo_button_ico",
            githubRepo = "veeso/BiangBiang-Kana",
            supportEmail = "info@veeso.dev",
            appStoreId = "6770998277",
            playStoreId = "dev.veeso.biangbiangkana",
        ),
        languages = listOf(
            LanguageProfile(
                id = "japanese",
                displayName = "Japanese",
                // Hiragana, Katakana, Katakana phonetic extensions and
                // CJK Unified Ideographs (Kanji).
                scriptRanges = listOf(
                    0x3040u..0x309Fu,
                    0x30A0u..0x30FFu,
                    0x31F0u..0x31FFu,
                    0x4E00u..0x9FFFu,
                ),
                ocrRecognizer = OcrRecognizer.JAPANESE,
                variants = listOf(
                    LanguageVariant(
                        id = "japanese",
                        displayName = "Japanese",
                        transliterator = romaji,
                        ttsLanguageCode = "ja",
                        translatable = true,
                    ),
                ),
            ),
        ),
        extraSettings = emptyList(),
        plugins = emptyList(),
        features = FeatureFlags(),
        strings = mapOf(
            "inputTitle" to "Japanese",
            "outputTitle" to "Romaji",
            "appSubtitle" to "Convert Japanese to Romaji",
        ),
    )
}
