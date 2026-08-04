package com.ahmad.raza.coinroutine.coins.presentation

import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource

@Stable
data class CoinState(
    val error: StringResource? = null,
    val coins: List<UiCoinListItem> = emptyList()
)