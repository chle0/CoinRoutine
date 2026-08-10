package com.ahmad.raza.coinroutine.portfolio.presentation

import app.cash.turbine.test
import com.ahmad.raza.coinroutine.core.domain.DataError
import com.ahmad.raza.coinroutine.core.utils.formatFiat
import com.ahmad.raza.coinroutine.core.utils.toUiText
import com.ahmad.raza.coinroutine.portfolio.data.FakePortfolioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PortfolioViewModelTest {

    private lateinit var viewModel: PortfolioViewModel
    private lateinit var portfolioRepository: FakePortfolioRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        portfolioRepository = FakePortfolioRepository()
        viewModel = PortfolioViewModel(
            portfolioRepository = portfolioRepository,
            coroutinesDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun stateAndPortfolioCoinsAreProperlyCombined() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.coins.isEmpty())

            val portfolioCoin = FakePortfolioRepository.portfolioCoin
            portfolioRepository.savePortfolioCoin(portfolioCoin)

            awaitItem()
            val updatedItem = awaitItem()
            assertTrue(updatedItem.coins.isNotEmpty())

            assertFalse(updatedItem.isLoading)
            assertEquals(
                FakePortfolioRepository.portfolioCoin.coin.id,
                updatedItem.coins.first().id
            )

        }
    }

    @Test
    fun portfolioValueUpdatesWhenACoinIsAdded() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals(initialState.portfolioValue, formatFiat(15000.0))


            val portfolioCoin = FakePortfolioRepository.portfolioCoin.copy(
                ownedAmountInUnit = 50.0,
                ownedAmountInFiat = 1000.0
            )
            portfolioRepository.savePortfolioCoin(portfolioCoin)
            val updatedState = awaitItem()
            assertEquals(formatFiat(16000.0), updatedState.portfolioValue)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadingStateAndErrorMessageUpdateOnFailure() = runTest {
        portfolioRepository.simulateError()

        viewModel.state.test {
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(DataError.Remote.SERVER.toUiText(), errorState.error)
        }
    }
}