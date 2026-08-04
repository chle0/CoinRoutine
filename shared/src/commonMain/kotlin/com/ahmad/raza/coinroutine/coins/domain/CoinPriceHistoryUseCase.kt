package com.ahmad.raza.coinroutine.coins.domain

import com.ahmad.raza.coinroutine.coins.data.mapper.toPriceModel
import com.ahmad.raza.coinroutine.coins.domain.api.CoinsRemoteDataSource
import com.ahmad.raza.coinroutine.coins.domain.model.PriceModel
import com.ahmad.raza.coinroutine.core.domain.DataError
import com.ahmad.raza.coinroutine.core.domain.Result
import com.ahmad.raza.coinroutine.core.domain.map

class CoinPriceHistoryUseCase(
    private val client: CoinsRemoteDataSource
) {

    suspend fun execute(coinId: String): Result<List<PriceModel>, DataError.Remote> {
        return client.getPriceHistory(coinId).map { price ->
            price.data.history.map { it.toPriceModel() }
        }
    }
}