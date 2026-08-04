package com.ahmad.raza.coinroutine.coins.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmad.raza.coinroutine.coins.domain.CoinsListUseCase
import com.ahmad.raza.coinroutine.core.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CoinsListViewModel(
    private val coinsListUseCase: CoinsListUseCase
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
                                formattedPrice = coinItem.price.toString(),
                                formattedChange = coinItem.change.toString(),
                                isPositive = coinItem.change >= 0
                            )
                        })
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        coins = emptyList(),
                        error = null
                    )
                }
            }
        }
    }
}