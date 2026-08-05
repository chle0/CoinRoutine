package com.ahmad.raza.coinroutine.coins.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmad.raza.coinroutine.coins.domain.CoinPriceHistoryUseCase
import com.ahmad.raza.coinroutine.coins.domain.CoinsListUseCase
import com.ahmad.raza.coinroutine.core.domain.Result
import com.ahmad.raza.coinroutine.core.utils.formatFiat
import com.ahmad.raza.coinroutine.core.utils.formatPercentage
import com.ahmad.raza.coinroutine.core.utils.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinsListViewModel(
    private val coinsListUseCase: CoinsListUseCase,
    private val coinPriceHistoryUseCase: CoinPriceHistoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoinState())
    val state = _state.onStart {
        getAllCoins()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CoinState()
    )

    private suspend fun getAllCoins() {
        when (val coinResponse = coinsListUseCase.execute()) {
            is Result.Success -> {
                _state.update {
                    CoinState(
                        coins = coinResponse.data.map { coinItem ->
                            UiCoinListItem(
                                id = coinItem.coin.id,
                                name = coinItem.coin.name,
                                symbol = coinItem.coin.symbol,
                                iconUrl = coinItem.coin.iconUrl,
                                formattedPrice = formatFiat(coinItem.price),
                                formattedChange = formatPercentage(coinItem.change),
                                isPositive = coinItem.change >= 0
                            )
                        })
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        coins = emptyList(), error = coinResponse.error.toUiText()
                    )
                }
            }
        }
    }

    fun onCoinLongPressed(coinId: String) {
        _state.update {
            it.copy(
                chartState = CoinChartState(
                    sparkLine = emptyList(), isLoading = true
                )
            )
        }

        viewModelScope.launch {
            when (val priceHistory = coinPriceHistoryUseCase.execute(coinId)) {
                is Result.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            chartState = CoinChartState(
                                sparkLine = priceHistory.data.sortedBy { it.timestamp }
                                    .map { it.price },
                                isLoading = false,
                                coinName = _state.value.coins.find { it.id == coinId }?.name.orEmpty()
                            )
                        )
                    }
                }

                is Result.Error -> {
                    _state.update { currentState ->
                        currentState.copy(
                            chartState = CoinChartState(
                                sparkLine = emptyList(), isLoading = false, coinName = ""
                            )
                        )
                    }
                }
            }
        }
    }

    fun onDismissChart() {
        _state.update {
            it.copy(chartState = null)
        }
    }
}