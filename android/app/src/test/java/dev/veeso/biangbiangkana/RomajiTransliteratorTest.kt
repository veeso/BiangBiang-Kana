package dev.veeso.biangbiangkana

import dev.veeso.biangbiangkana.services.KatakanaRomaji
import dev.veeso.biangbiangkana.services.RomajiTransliterator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * `KatakanaRomaji` is deterministic and tested exactly. `RomajiTransliterator`
 * goes through Kuromoji, whose segmentation we don't pin — only that ASCII
 * passes through and Japanese input yields Latin output.
 */
class KatakanaRomajiTest {
    @Test fun word() = assertEquals("nihon", KatakanaRomaji.convert("ニホン"))

    @Test fun longVowel() = assertEquals("raamen", KatakanaRomaji.convert("ラーメン"))

    @Test fun sokuon() = assertEquals("gakkou", KatakanaRomaji.convert("ガッコウ"))

    @Test fun digraph() = assertEquals("toukyou", KatakanaRomaji.convert("トウキョウ"))

    @Test fun sokuonBeforeChi() = assertEquals("matcha", KatakanaRomaji.convert("マッチャ"))
}

class RomajiTransliteratorTest {
    private val t = RomajiTransliterator()

    @Test fun asciiPassthrough() = assertEquals("hello", t.transliterate("hello"))

    @Test fun japaneseProducesLatin() {
        val out = t.transliterate("日本語")
        assertTrue(out.isNotEmpty())
        assertTrue(out.all { it.code < 0x3000 })
    }

    /** Out-of-vocabulary Katakana compounds carry no Kuromoji reading;
     *  their surface must still be romanised, not passed through raw. */
    @Test fun unknownKatakanaCompound() {
        val out = t.transliterate("明太マヨフライドポテト")
        assertTrue("untransliterated katakana in [$out]", out.all { it.code < 0x3000 })
    }
}
