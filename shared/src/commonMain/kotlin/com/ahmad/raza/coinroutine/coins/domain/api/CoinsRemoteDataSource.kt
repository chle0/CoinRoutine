package com.ahmad.raza.coinroutine.coins.domain.api

import com.ahmad.raza.coinroutine.coins.data.remote.dto.CoinDetailsResponse
import com.ahmad.raza.coinroutine.coins.data.remote.dto.CoinPriceHistoryResponse
import com.ahmad.raza.coinroutine.coins.data.remote.dto.CoinsResponseDto
import com.ahmad.raza.coinroutine.core.domain.DataError
import com.ahmad.raza.coinroutine.core.domain.Result

interface CoinsRemoteDataSource {

    suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote>

    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponse, DataError.Remote>

    suspend fun getCoinById(coinId: String): Result<CoinDetailsResponse, DataError.Remote>
}