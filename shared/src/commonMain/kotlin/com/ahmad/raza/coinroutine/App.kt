package com.ahmad.raza.coinroutine

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ahmad.raza.coinroutine.coins.presentation.CoinsListScreen
import com.ahmad.raza.coinroutine.navigation.Buy
import com.ahmad.raza.coinroutine.navigation.Sell
import com.ahmad.raza.coinroutine.navigation.Coins
import com.ahmad.raza.coinroutine.navigation.Portfolio
import com.ahmad.raza.coinroutine.portfolio.presentation.PortfolioScreen
import com.ahmad.raza.coinroutine.theme.CoinRoutineTheme
import com.ahmad.raza.coinroutine.trade.presentation.buy.BuyScreen
import com.ahmad.raza.coinroutine.trade.presentation.sell.SellScreen

@Composable
@Preview
fun App() {
    val navController: NavHostController = rememberNavController()

    CoinRoutineTheme {
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = Portfolio
        ) {
            composable<Portfolio> {
                PortfolioScreen(
                    onCoinItemClicked = { coinId ->
                        navController.navigate(Sell(coinId))
                    },
                    onDiscoverCoinsClicked = {
                        navController.navigate(Coins)
                    }
                )
            }

            composable<Coins> {
                CoinsListScreen { coinId ->
                    navController.navigate(Buy(coinId))
                }
            }

            composable<Buy> { navBackStackEntry ->
                val coinId: String = navBackStackEntry.toRoute<Buy>().coinId
                BuyScreen(
                    coinId = coinId,
                    navigateToPortfolio = {
                        navController.navigate(Portfolio) {
                            popUpTo(Portfolio) { inclusive = true }
                        }
                    }
                )
            }
            composable<Sell> { navBackStackEntry ->
                val coinId: String = navBackStackEntry.toRoute<Sell>().coinId
                SellScreen(
                    coinId = coinId,
                    navigateToPortfolio = {
                        navController.navigate(Portfolio) {
                            popUpTo(Portfolio) { inclusive = true }
                        }
                    }
                )
            }

        }

    }
}