package com.ahmad.raza.coinroutine

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ahmad.raza.coinroutine.coins.presentation.CoinsListScreen
import com.ahmad.raza.coinroutine.portfolio.presentation.PortfolioScreen
import com.ahmad.raza.coinroutine.theme.CoinRoutineTheme

@Composable
@Preview
fun App() {
    CoinRoutineTheme {
        //CoinsListScreen { }
        PortfolioScreen(
            onCoinItemClicked = {},
            onDiscoverCoinsClicked = {}
        )
    }
}