//
//  TransliteratorParityTests.swift
//  BiangBiang Kana
//
//  Span-level checks for the Romaji transliterator. The library's
//  `TextProcessingEngine` owns span detection / passthrough / spacing, so
//  these assert romanisation of an already-isolated Japanese script span
//  only.
//

@testable import BiangBiang_Kana
import Testing

struct RomajiTransliteratorTests {
    let t = RomajiTransliterator()

    @Test func asciiPassthrough() {
        #expect(t.transliterate("hello") == "hello")
    }

    @Test func kanaProducesLatin() {
        let out = t.transliterate("こんにちは")
        #expect(!out.isEmpty)
        // No CJK / Kana scalars remain in the output.
        #expect(out.unicodeScalars.allSatisfy { $0.value < 0x3000 })
    }

    @Test func kanjiProducesLatin() {
        let out = t.transliterate("日本語")
        #expect(!out.isEmpty)
        #expect(out.unicodeScalars.allSatisfy { $0.value < 0x3000 })
    }
}
