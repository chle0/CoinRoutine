package com.ahmad.raza.coinroutine.portfolio.domain

import com.ahmad.raza.coinroutine.core.domain.DataError
import com.ahmad.raza.coinroutine.core.domain.EmptyResult
import com.ahmad.raza.coinroutine.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    suspend fun initializeBalance()
    fun allPortfolioCoinsFlow(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>>
    suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote>
    suspend fun savePortfolioCoin(portfolioCoinModel: PortfolioCoinModel): EmptyResult<DataError.Local>
    suspend fun removeCoinFromPortfolio(coinId: String)

    fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>>
    fun totalBalanceFlow(): Flow<Result<Double, DataError.Remote>>
    fun cashBalanceFlow(): Flow<Double>
    suspend fun updateCashBalance(newBalance: Double)
}