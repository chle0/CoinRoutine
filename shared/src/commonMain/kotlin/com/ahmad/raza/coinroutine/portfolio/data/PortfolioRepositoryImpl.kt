package com.ahmad.raza.coinroutine.portfolio.data

import androidx.sqlite.SQLiteException
import com.ahmad.raza.coinroutine.coins.domain.api.CoinsRemoteDataSource
import com.ahmad.raza.coinroutine.core.domain.DataError
import com.ahmad.raza.coinroutine.core.domain.EmptyResult
import com.ahmad.raza.coinroutine.core.domain.Result
import com.ahmad.raza.coinroutine.core.domain.onError
import com.ahmad.raza.coinroutine.core.domain.onSuccess
import com.ahmad.raza.coinroutine.portfolio.data.local.BalanceDao
import com.ahmad.raza.coinroutine.portfolio.data.local.PortfolioDao
import com.ahmad.raza.coinroutine.portfolio.data.local.UserBalanceEntity
import com.ahmad.raza.coinroutine.portfolio.data.mapper.toPortfolioCoinEntity
import com.ahmad.raza.coinroutine.portfolio.data.mapper.toPortfolioCoinModel
import com.ahmad.raza.coinroutine.portfolio.domain.PortfolioCoinModel
import com.ahmad.raza.coinroutine.portfolio.domain.PortfolioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class PortfolioRepositoryImpl(
    private val portfolioDao: PortfolioDao,
    private val balanceDao: BalanceDao,
    private val coinsRemoteDataSource: CoinsRemoteDataSource
) : PortfolioRepository {

    override suspend fun initializeBalance() {
        val currentBalance = balanceDao.getCashBalance()
        if (currentBalance == null) {
            balanceDao.insertBalance(
                UserBalanceEntity(
                    cashBalance = 15000.0
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun allPortfolioCoinsFlow(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>> {
        return portfolioDao.getAllOwnedCoins().flatMapLatest { portfolioCoinEntities ->
            if (portfolioCoinEntities.isEmpty()) {
                flow {
                    emit(Result.Success(emptyList<PortfolioCoinModel>()))
                }
            } else {
                flow {
                    coinsRemoteDataSource.getListOfCoins().onError { error ->
                        emit(Result.Error(error))
                    }.onSuccess { coinsDto ->
                        val portfolioCoins =
                            portfolioCoinEntities.mapNotNull { portfolioCoinEntity ->
                                val coin =
                                    coinsDto.data.coins.find { it.uuid == portfolioCoinEntity.coinId }
                                coin?.let {
                                    portfolioCoinEntity.toPortfolioCoinModel(it.price)
                                }
                            }
                        emit(Result.Success(portfolioCoins))
                    }
                }
            }
        }.catch {
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }
    }

    override suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote> {
        coinsRemoteDataSource.getCoinById(coinId).onError { error ->
            return Result.Error(error)
        }.onSuccess { coinDto ->
            val portfolioCoinEntity = portfolioDao.getCoinById(coinId)
            return if (portfolioCoinEntity != null) {
                Result.Success(portfolioCoinEntity.toPortfolioCoinModel(coinDto.data.coin.price))
            } else {
                Result.Success(null)
            }
        }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun savePortfolioCoin(portfolioCoinModel: PortfolioCoinModel): EmptyResult<DataError.Local> {
        try {
            portfolioDao.insert(portfolioCoinModel.toPortfolioCoinEntity())
            return Result.Success(Unit)
        } catch (e: SQLiteException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun removeCoinFromPortfolio(coinId: String) {
        portfolioDao.deletePortfolioItem(coinId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>> {
        return portfolioDao.getAllOwnedCoins().flatMapLatest { portfolioCoinEntities ->
            if (portfolioCoinEntities.isEmpty()) {
                flow { emit(Result.Success(0.0)) }
            } else {
                flow {
                    val apiResult = coinsRemoteDataSource.getListOfCoins()
                    apiResult.onError { error ->
                        emit(Result.Error(error))
                    }.onSuccess { coinsDto ->
                        val totalValue = portfolioCoinEntities.sumOf { ownedCoin ->
                            val coinPrice =
                                coinsDto.data.coins.find { it.uuid == ownedCoin.coinId }?.price
                                    ?: 0.0
                            ownedCoin.amountOwned * coinPrice
                        }
                        emit(Result.Success(totalValue))
                    }
                }
            }.catch {
                emit(Result.Error(DataError.Remote.UNKNOWN))
            }
        }
    }

    override fun totalBalanceFlow(): Flow<Result<Double, DataError.Remote>> {
        return combine(
            cashBalanceFlow(), calculateTotalPortfolioValue()
        ) { cashBalance, portfolioResult ->
            when (portfolioResult) {
                is Result.Success -> {
                    Result.Success(cashBalance + portfolioResult.data)
                }

                is Result.Error -> {
                    Result.Error(portfolioResult.error)
                }
            }

        }
    }

    override fun cashBalanceFlow(): Flow<Double> {
        return flow {
            emit(balanceDao.getCashBalance() ?: 15000.0)
        }
    }

    override suspend fun updateCashBalance(newBalance: Double) {
        balanceDao.updateCahBalance(newBalance)
    }

}