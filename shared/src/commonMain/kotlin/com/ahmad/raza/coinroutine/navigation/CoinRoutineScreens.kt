package com.ahmad.raza.coinroutine.navigation

import kotlinx.serialization.Serializable

@Serializable
object Coins
@Serializable
object Portfolio
@Serializable
data class Buy(val coinId: String)
@Serializable
data class Sell(val coinId: String)