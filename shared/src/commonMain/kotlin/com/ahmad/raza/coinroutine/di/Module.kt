package com.ahmad.raza.coinroutine.di

import com.ahmad.raza.coinroutine.coins.data.remote.impl.KtorCoinsRemoteDataSource
import com.ahmad.raza.coinroutine.coins.domain.CoinDetailsUseCase
import com.ahmad.raza.coinroutine.coins.domain.CoinPriceHistoryUseCase
import com.ahmad.raza.coinroutine.coins.domain.CoinsListUseCase
import com.ahmad.raza.coinroutine.coins.domain.api.CoinsRemoteDataSource
import com.ahmad.raza.coinroutine.coins.presentation.CoinsListViewModel
import com.ahmad.raza.coinroutine.core.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)
    modules(
        sharedModules, platformModule
    )
}

expect val platformModule: Module

val sharedModules = module {
    single<HttpClient> { HttpClientFactory.create(get()) }
    viewModel { CoinsListViewModel(get(), get()) }
    singleOf(::CoinsListUseCase)
    singleOf(::KtorCoinsRemoteDataSource).bind<CoinsRemoteDataSource>()
    singleOf(::CoinDetailsUseCase)
    singleOf(::CoinPriceHistoryUseCase)

}