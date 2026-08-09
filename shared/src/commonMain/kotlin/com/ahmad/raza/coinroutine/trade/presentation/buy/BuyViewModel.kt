package com.ahmad.raza.coinroutine.trade.presentation.buy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmad.raza.coinroutine.coins.domain.CoinDetailsUseCase
import com.ahmad.raza.coinroutine.core.domain.Result
import com.ahmad.raza.coinroutine.core.utils.formatFiat
import com.ahmad.raza.coinroutine.core.utils.toUiText
import com.ahmad.raza.coinroutine.portfolio.domain.PortfolioRepository
import com.ahmad.raza.coinroutine.trade.domain.BuyCoinsUseCase
import com.ahmad.raza.coinroutine.trade.presentation.common.TradeState
import com.ahmad.raza.coinroutine.trade.presentation.common.UiTradeCoinItem
import com.ahmad.raza.coinroutine.trade.presentation.mapper.toCoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BuyViewModel(
    private val coinDetailsUseCase: CoinDetailsUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val buyCoinsUseCase: BuyCoinsUseCase,
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
        val balance = portfolioRepository.cashBalanceFlow().first()
        getCoinDetails(balance)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), TradeState(isLoading = true)
    )

    private val _events = Channel<BuyEvents>(capacity = Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private suspend fun getCoinDetails(balance: Double) {
        when (val coinResponse = coinDetailsUseCase.execute(coinId)) {
            is Result.Success -> {
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id = coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price
                        ), availableAmount = "Available: ${formatFiat(balance)}"
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

    fun onAmountChanged(amount: String) {
        _amount.value = amount
    }

    fun onBuyClicked() {
        val tradeCoin = state.value.coin ?: return
        viewModelScope.launch {
            val buyCoinResponse = buyCoinsUseCase.buyCoins(
                coin = tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price
            )
            when (buyCoinResponse) {
                is Result.Success -> {
                    _events.send(BuyEvents.BuySuccess)
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false, error = buyCoinResponse.error.toUiText()
                        )
                    }
                }
            }
        }
    }
}

sealed interface BuyEvents {
    data object BuySuccess : BuyEvents
}