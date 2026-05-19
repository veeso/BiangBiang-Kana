//
//  BiangBiang_KanaApp.swift
//  BiangBiang Kana
//
//  Created by christian visintin on 31/10/25.
//
//  Config-only entry point: the BiangBiangUI library renders every screen
//  and owns History, the rate prompt, TTS and the OCR pipeline. The app
//  supplies only `JapaneseConfig` + the Romaji transliterator.
//

import BiangBiangUI
import SwiftUI

@main
struct BiangBiang_KanaApp: App {
    var body: some Scene {
        WindowGroup {
            BiangBiangRootView(config: JapaneseConfig.japaneseConfig)
        }
    }
}
