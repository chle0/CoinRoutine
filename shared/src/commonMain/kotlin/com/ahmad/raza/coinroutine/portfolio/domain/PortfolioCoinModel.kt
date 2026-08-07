package com.ahmad.raza.coinroutine.portfolio.domain

import com.ahmad.raza.coinroutine.core.domain.coin.Coin

data class PortfolioCoinModel(
    val coin: Coin,
    val performancePercent: Double,
    val averagePurchasePrice: Double,
    val ownedAmountInUnit: Double,
    val ownedAmountInFiat: Double
)
