package com.ahmad.raza.coinroutine

import androidx.compose.ui.window.ComposeUIViewController
import com.ahmad.raza.coinroutine.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }