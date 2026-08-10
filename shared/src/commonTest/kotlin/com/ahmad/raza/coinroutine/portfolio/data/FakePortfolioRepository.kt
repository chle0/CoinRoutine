package com.ahmad.raza.coinroutine.portfolio.data

import com.ahmad.raza.coinroutine.core.domain.DataError
import com.ahmad.raza.coinroutine.core.domain.EmptyResult
import com.ahmad.raza.coinroutine.core.domain.Result
import com.ahmad.raza.coinroutine.core.domain.coin.Coin
import com.ahmad.raza.coinroutine.portfolio.domain.PortfolioCoinModel
import com.ahmad.raza.coinroutine.portfolio.domain.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePortfolioRepository : PortfolioRepository {

    private val _data = MutableStateFlow<Result<List<PortfolioCoinModel>, DataError.Remote>>(
        Result.Success(emptyList())
    )

    private val _cashBalance = MutableStateFlow(cashBalance)
    private val _portfolioValue = MutableStateFlow(portfolioValue)
    private val _listOfCoins = mutableListOf<PortfolioCoinModel>()

    override suspend fun initializeBalance() {
        //no-option
    }

    override fun allPortfolioCoinsFlow(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>> {
        return _data.asStateFlow()
    }

    override suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote> {
        return Result.Success(portfolioCoin)
    }

    override suspend fun savePortfolioCoin(portfolioCoinModel: PortfolioCoinModel): EmptyResult<DataError.Local> {
        _listOfCoins.add(portfolioCoinModel)
        _portfolioValue.value = _listOfCoins.sumOf { it.ownedAmountInFiat }
        _data.value = Result.Success(_listOfCoins)
        return Result.Success(Unit)
    }

    override suspend fun removeCoinFromPortfolio(coinId: String) {
        _data.update { Result.Success(emptyList()) }
    }

    override fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>> {
        return _portfolioValue.map { Result.Success(it) }
    }

    override fun totalBalanceFlow(): Flow<Result<Double, DataError.Remote>> {
        return _cashBalance.combine(_portfolioValue) { cashBalance, portfolioValue ->
            cashBalance + portfolioValue
        }.map { Result.Success(it) }
    }

    override fun cashBalanceFlow(): Flow<Double> {
        return _cashBalance.asStateFlow()
    }

    override suspend fun updateCashBalance(newBalance: Double) {
        _cashBalance.value = newBalance
    }

    fun simulateError() {
        _data.value = Result.Error(DataError.Remote.SERVER)
    }

    companion object {
        val fakeCoin = Coin(
            id = "fakeId",
            name = "Fake Coin",
            symbol = "Fake",
            iconUrl = "https://fake.url/fake.png"
        )

        val portfolioCoin = PortfolioCoinModel(
            coin = fakeCoin,
            performancePercent = 10.0,
            averagePurchasePrice = 1000.0,
            ownedAmountInUnit = 10.0,
            ownedAmountInFiat = 10.0
        )
        val cashBalance = 15000.0
        val portfolioValue = 0.0
    }
}