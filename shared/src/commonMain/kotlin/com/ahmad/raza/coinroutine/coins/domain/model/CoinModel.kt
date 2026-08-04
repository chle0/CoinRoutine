package com.ahmad.raza.coinroutine.coins.domain.model

import com.ahmad.raza.coinroutine.core.domain.coin.Coin

data class CoinModel(
    val coin: Coin,
    val price: Double,
    val change: Double
)