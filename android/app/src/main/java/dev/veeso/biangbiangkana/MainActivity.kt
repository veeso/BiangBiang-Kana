package dev.veeso.biangbiangkana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.veeso.biangbiangkana.config.japaneseConfig
import dev.veeso.biangbiangui.ui.BiangBiangRoot

/**
 * Config-only entry point: the BiangBiangUI library renders every screen
 * and owns History, the rate prompt, TTS and the OCR pipeline (including
 * `registerLaunch`). The app supplies only [japaneseConfig] + the
 * Romaji transliterator.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiangBiangRoot(japaneseConfig())
        }
    }
}
