package com.ahmad.raza.coinroutine.coins.data.mapper

import com.ahmad.raza.coinroutine.coins.data.remote.dto.CoinItem
import com.ahmad.raza.coinroutine.coins.data.remote.dto.CoinPrice
import com.ahmad.raza.coinroutine.coins.domain.model.CoinModel
import com.ahmad.raza.coinroutine.coins.domain.model.PriceModel
import com.ahmad.raza.coinroutine.core.domain.coin.Coin

fun CoinItem.toCoinModel() = CoinModel(
    coin = Coin(
        id = uuid,
        name = name,
        symbol = symbol,
        iconUrl = iconUrl
    ),
    price = price,
    change = change
)

fun CoinPrice.toPriceModel() = PriceModel(
    price = price ?: 0.0,
    timestamp = timestamp
)