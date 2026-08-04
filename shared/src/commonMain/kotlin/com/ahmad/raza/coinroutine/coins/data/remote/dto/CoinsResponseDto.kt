package com.ahmad.raza.coinroutine.coins.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinsResponseDto(
    val data: CoinsListData
)

@Serializable
data class CoinsListData(
    val coins: List<CoinItem>
)

@Serializable
data class CoinItem(
    val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    val price: Double,
    val rank: Int,
    val change: Double,
)