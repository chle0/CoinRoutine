package com.ahmad.raza.coinroutine.core.domain

import com.ahmad.raza.coinroutine.shared.BuildKonfig

actual object ApiKeyProvider {
    actual val apiKey: String
        get() = BuildKonfig.API_KEY
}