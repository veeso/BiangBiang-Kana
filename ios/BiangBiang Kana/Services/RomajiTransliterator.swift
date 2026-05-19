//
//  RomajiTransliterator.swift
//  BiangBiang Kana
//
//  Japanese romanisation. The library's `TextProcessingEngine` owns span
//  detection, passthrough and spacing; this only romanises one isolated
//  Japanese script span. `CFStringTokenizer` with a Japanese locale yields
//  the Latin transcription (Hepburn-style romaji) for each word, resolving
//  Kanji readings as well as Kana — native to Foundation, no dependency.
//

import BiangBiangUI
import Foundation

struct RomajiTransliterator: Transliterator {
    func transliterate(_ scriptSpan: String) -> String {
        let text = scriptSpan as CFString
        let fullRange = CFRangeMake(0, CFStringGetLength(text))
        let locale = Locale(identifier: "ja") as CFLocale
        let tokenizer = CFStringTokenizerCreate(
            kCFAllocatorDefault,
            text,
            fullRange,
            kCFStringTokenizerUnitWordBoundary,
            locale
        )

        var result = ""
        var tokenType = CFStringTokenizerAdvanceToNextToken(tokenizer)
        while tokenType != [] {
            let tokenRange = CFStringTokenizerGetCurrentTokenRange(tokenizer)
            let surface = CFStringCreateWithSubstring(kCFAllocatorDefault, text, tokenRange) as String? ?? ""
            if let latin = CFStringTokenizerCopyCurrentTokenAttribute(
                tokenizer,
                kCFStringTokenizerAttributeLatinTranscription
            ) as? String, !latin.isEmpty {
                if !result.isEmpty { result += " " }
                result += latin
            } else {
                result += surface
            }
            tokenType = CFStringTokenizerAdvanceToNextToken(tokenizer)
        }

        return result.isEmpty ? scriptSpan : result
    }
}
