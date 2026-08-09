package com.ahmad.raza.coinroutine.trade.presentation.mapper

import com.ahmad.raza.coinroutine.core.domain.coin.Coin
import com.ahmad.raza.coinroutine.trade.presentation.common.UiTradeCoinItem

fun UiTradeCoinItem.toCoin() = Coin(
    id = id, name = name, symbol = symbol, iconUrl = iconUrl
)
