package dev.veeso.biangbiangkana.services

import com.atilika.kuromoji.ipadic.Tokenizer
import dev.veeso.biangbiangui.protocols.Transliterator

/**
 * Japanese romanisation. The library's
 * [dev.veeso.biangbiangui.services.TextProcessingEngine] owns span
 * detection, passthrough and spacing; this only romanises one isolated
 * Japanese script span. Kuromoji (IPADIC) tokenises the span and yields a
 * Katakana reading per token — resolving Kanji readings as well as Kana —
 * which [KatakanaRomaji] converts to Hepburn romaji. Kuromoji is an
 * app-only dependency, never referenced by the library.
 */
class RomajiTransliterator : Transliterator {

    private val tokenizer: Tokenizer by lazy { Tokenizer() }

    override fun transliterate(scriptSpan: String): String {
        val parts = tokenizer.tokenize(scriptSpan).map { token ->
            val reading = token.reading
            if (reading.isNullOrEmpty() || reading == "*") {
                token.surface
            } else {
                KatakanaRomaji.convert(reading)
            }
        }
        return parts.joinToString(" ").trim()
    }
}

/** Hepburn Katakana → romaji converter. */
internal object KatakanaRomaji {

    private val digraphs: Map<String, String> = mapOf(
        "キャ" to "kya", "キュ" to "kyu", "キョ" to "kyo",
        "シャ" to "sha", "シュ" to "shu", "ショ" to "sho",
        "チャ" to "cha", "チュ" to "chu", "チョ" to "cho",
        "ニャ" to "nya", "ニュ" to "nyu", "ニョ" to "nyo",
        "ヒャ" to "hya", "ヒュ" to "hyu", "ヒョ" to "hyo",
        "ミャ" to "mya", "ミュ" to "myu", "ミョ" to "myo",
        "リャ" to "rya", "リュ" to "ryu", "リョ" to "ryo",
        "ギャ" to "gya", "ギュ" to "gyu", "ギョ" to "gyo",
        "ジャ" to "ja", "ジュ" to "ju", "ジョ" to "jo",
        "ビャ" to "bya", "ビュ" to "byu", "ビョ" to "byo",
        "ピャ" to "pya", "ピュ" to "pyu", "ピョ" to "pyo",
        "ファ" to "fa", "フィ" to "fi", "フェ" to "fe", "フォ" to "fo",
        "ティ" to "ti", "ディ" to "di", "トゥ" to "tu", "ドゥ" to "du",
        "ウィ" to "wi", "ウェ" to "we", "ウォ" to "wo",
        "ヴァ" to "va", "ヴィ" to "vi", "ヴェ" to "ve", "ヴォ" to "vo",
    )

    private val monographs: Map<Char, String> = mapOf(
        'ア' to "a", 'イ' to "i", 'ウ' to "u", 'エ' to "e", 'オ' to "o",
        'カ' to "ka", 'キ' to "ki", 'ク' to "ku", 'ケ' to "ke", 'コ' to "ko",
        'サ' to "sa", 'シ' to "shi", 'ス' to "su", 'セ' to "se", 'ソ' to "so",
        'タ' to "ta", 'チ' to "chi", 'ツ' to "tsu", 'テ' to "te", 'ト' to "to",
        'ナ' to "na", 'ニ' to "ni", 'ヌ' to "nu", 'ネ' to "ne", 'ノ' to "no",
        'ハ' to "ha", 'ヒ' to "hi", 'フ' to "fu", 'ヘ' to "he", 'ホ' to "ho",
        'マ' to "ma", 'ミ' to "mi", 'ム' to "mu", 'メ' to "me", 'モ' to "mo",
        'ヤ' to "ya", 'ユ' to "yu", 'ヨ' to "yo",
        'ラ' to "ra", 'リ' to "ri", 'ル' to "ru", 'レ' to "re", 'ロ' to "ro",
        'ワ' to "wa", 'ヰ' to "i", 'ヱ' to "e", 'ヲ' to "o", 'ン' to "n",
        'ガ' to "ga", 'ギ' to "gi", 'グ' to "gu", 'ゲ' to "ge", 'ゴ' to "go",
        'ザ' to "za", 'ジ' to "ji", 'ズ' to "zu", 'ゼ' to "ze", 'ゾ' to "zo",
        'ダ' to "da", 'ヂ' to "ji", 'ヅ' to "zu", 'デ' to "de", 'ド' to "do",
        'バ' to "ba", 'ビ' to "bi", 'ブ' to "bu", 'ベ' to "be", 'ボ' to "bo",
        'パ' to "pa", 'ピ' to "pi", 'プ' to "pu", 'ペ' to "pe", 'ポ' to "po",
        'ヴ' to "vu", 'ァ' to "a", 'ィ' to "i", 'ゥ' to "u", 'ェ' to "e", 'ォ' to "o",
        'ャ' to "ya", 'ュ' to "yu", 'ョ' to "yo", 'ッ' to "",
    )

    fun convert(katakana: String): String {
        val sb = StringBuilder()
        var i = 0
        var pendingSokuon = false
        while (i < katakana.length) {
            val ch = katakana[i]

            // Long vowel mark: repeat the previous vowel.
            if (ch == 'ー') {
                val last = sb.lastOrNull()
                if (last != null && last in "aeiou") sb.append(last)
                i++
                continue
            }

            // Sokuon (small tsu): geminate the next consonant.
            if (ch == 'ッ') {
                pendingSokuon = true
                i++
                continue
            }

            // Try a two-character digraph first.
            var romaji: String? = null
            var consumed = 1
            if (i + 1 < katakana.length) {
                digraphs[katakana.substring(i, i + 2)]?.let {
                    romaji = it
                    consumed = 2
                }
            }
            val syllable = romaji ?: (monographs[ch] ?: ch.toString())

            var out = syllable
            if (pendingSokuon && out.isNotEmpty()) {
                val head = if (out.startsWith("ch")) "t" else out.first().toString()
                out = head + out
                pendingSokuon = false
            }
            sb.append(out)
            i += consumed
        }
        return sb.toString()
    }
}
