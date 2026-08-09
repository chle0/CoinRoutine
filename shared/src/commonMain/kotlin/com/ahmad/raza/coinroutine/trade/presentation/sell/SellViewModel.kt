package com.ahmad.raza.coinroutine.trade.presentation.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmad.raza.coinroutine.coins.domain.CoinDetailsUseCase
import com.ahmad.raza.coinroutine.core.domain.Result
import com.ahmad.raza.coinroutine.core.utils.formatFiat
import com.ahmad.raza.coinroutine.core.utils.toUiText
import com.ahmad.raza.coinroutine.portfolio.domain.PortfolioRepository
import com.ahmad.raza.coinroutine.trade.domain.SellCoinsUseCase
import com.ahmad.raza.coinroutine.trade.presentation.buy.BuyEvents
import com.ahmad.raza.coinroutine.trade.presentation.common.TradeState
import com.ahmad.raza.coinroutine.trade.presentation.common.UiTradeCoinItem
import com.ahmad.raza.coinroutine.trade.presentation.mapper.toCoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SellViewModel(
    private val coinDetailsUseCase: CoinDetailsUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val sellCoinsUseCase: SellCoinsUseCase,
    private val coinId: String
) : ViewModel() {
    private val _amount = MutableStateFlow("")
    private val _state = MutableStateFlow(TradeState())

    val state = combine(
        _state, _amount
    ) { state, amount ->

        state.copy(
            amount = amount
        )
    }.onStart {
        when (val portfolioCoinResponse = portfolioRepository.getPortfolioCoin(coinId)) {
            is Result.Success -> {
                portfolioCoinResponse.data?.ownedAmountInUnit?.let {
                    getCoinDetails(it)
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false, error = portfolioCoinResponse.error.toUiText()
                    )
                }
            }
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), TradeState(isLoading = true)
    )


    private val _events = Channel<SellEvents>(capacity = Channel.BUFFERED)
    val events = _events.receiveAsFlow()
    fun onAmountChanged(amount: String) {
        _amount.value = amount
    }

    private suspend fun getCoinDetails(ownedAmountInUnit: Double) {
        when (val coinResponse = coinDetailsUseCase.execute(coinId)) {
            is Result.Success -> {
                val availableAmountInFiat = ownedAmountInUnit * coinResponse.data.price
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id = coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price
                        ), availableAmount = "Available: ${formatFiat(availableAmountInFiat)}"
                    )
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false, error = coinResponse.error.toUiText()
                    )
                }
            }
        }
    }

    fun onSellClicked() {
        val tradeCoin = _state.value.coin ?: return
        viewModelScope.launch {
            val sellCoinResponse = sellCoinsUseCase.sellCoin(
                coin = tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price
            )

            when (sellCoinResponse) {
                is Result.Success -> {
                    _events.send(SellEvents.SellSuccess)
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = sellCoinResponse.error.toUiText()
                        )
                    }
                }
            }
        }
    }
}

sealed interface SellEvents {
    data object SellSuccess : SellEvents
}