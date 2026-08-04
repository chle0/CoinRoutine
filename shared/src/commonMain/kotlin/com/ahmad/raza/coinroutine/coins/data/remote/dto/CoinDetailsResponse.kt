package com.ahmad.raza.coinroutine.coins.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailsResponse(
    val data: CoinResponse
)

@Serializable
data class CoinResponse(
    val coin: CoinItem
)
