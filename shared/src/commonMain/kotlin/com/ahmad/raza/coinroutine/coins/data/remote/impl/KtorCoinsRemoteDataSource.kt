package com.ahmad.raza.coinroutine.coins.data.remote.impl

import com.ahmad.raza.coinroutine.coins.data.remote.dto.CoinDetailsResponse
import com.ahmad.raza.coinroutine.coins.data.remote.dto.CoinPriceHistoryResponse
import com.ahmad.raza.coinroutine.coins.data.remote.dto.CoinsResponseDto
import com.ahmad.raza.coinroutine.coins.domain.api.CoinsRemoteDataSource
import com.ahmad.raza.coinroutine.core.domain.DataError
import com.ahmad.raza.coinroutine.core.domain.Result
import com.ahmad.raza.coinroutine.core.network.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val BASE_URL = "https://api.coinranking.com/v2"

class KtorCoinsRemoteDataSource(
    private val httpClient: HttpClient
) : CoinsRemoteDataSource {

    override suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/coins")
        }
    }

    override suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponse, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/coin/$coinId/history")
        }
    }

    override suspend fun getCoinById(coinId: String): Result<CoinDetailsResponse, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/coin/$coinId")
        }
    }

}