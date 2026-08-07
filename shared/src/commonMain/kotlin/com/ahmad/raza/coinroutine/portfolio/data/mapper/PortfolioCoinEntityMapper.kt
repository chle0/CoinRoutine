package com.ahmad.raza.coinroutine.portfolio.data.mapper

import com.ahmad.raza.coinroutine.core.domain.coin.Coin
import com.ahmad.raza.coinroutine.portfolio.data.local.PortfolioCoinEntity
import com.ahmad.raza.coinroutine.portfolio.domain.PortfolioCoinModel
import kotlin.time.Clock

fun PortfolioCoinEntity.toPortfolioCoinModel(
    currentPrice: Double
): PortfolioCoinModel {
    return PortfolioCoinModel(
        coin = Coin(
            id = coinId,
            name = name,
            symbol = symbol,
            iconUrl = iconUrl
        ),
        performancePercent = ((currentPrice - averagePurchasePrice) / averagePurchasePrice) * 100,
        averagePurchasePrice = averagePurchasePrice,
        ownedAmountInUnit = amountOwned,
        ownedAmountInFiat = amountOwned * currentPrice
    )
}

fun PortfolioCoinModel.toPortfolioCoinEntity(): PortfolioCoinEntity {
    return PortfolioCoinEntity(
        coinId = coin.id,
        name = coin.name,
        symbol = coin.symbol,
        iconUrl = coin.iconUrl,
        amountOwned = ownedAmountInUnit,
        averagePurchasePrice = averagePurchasePrice,
        timestamp = Clock.System.now().toEpochMilliseconds()
    )
}
