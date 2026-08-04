package com.ahmad.raza.coinroutine.coins.domain

import com.ahmad.raza.coinroutine.coins.data.mapper.toCoinModel
import com.ahmad.raza.coinroutine.coins.domain.api.CoinsRemoteDataSource
import com.ahmad.raza.coinroutine.coins.domain.model.CoinModel
import com.ahmad.raza.coinroutine.core.domain.DataError
import com.ahmad.raza.coinroutine.core.domain.Result
import com.ahmad.raza.coinroutine.core.domain.map

class CoinDetailsUseCase(
    private val client: CoinsRemoteDataSource
) {

    suspend fun execute(coinId: String): Result<CoinModel, DataError.Remote> {
        return client.getCoinById(coinId).map { coin ->
            coin.data.coin.toCoinModel()
        }
    }
}