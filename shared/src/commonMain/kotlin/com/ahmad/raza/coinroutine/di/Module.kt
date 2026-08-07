package com.ahmad.raza.coinroutine.di

import androidx.room.RoomDatabase
import com.ahmad.raza.coinroutine.coins.data.remote.impl.KtorCoinsRemoteDataSource
import com.ahmad.raza.coinroutine.coins.domain.CoinDetailsUseCase
import com.ahmad.raza.coinroutine.coins.domain.CoinPriceHistoryUseCase
import com.ahmad.raza.coinroutine.coins.domain.CoinsListUseCase
import com.ahmad.raza.coinroutine.coins.domain.api.CoinsRemoteDataSource
import com.ahmad.raza.coinroutine.coins.presentation.CoinsListViewModel
import com.ahmad.raza.coinroutine.core.database.portfolio_db.PortfolioDatabase
import com.ahmad.raza.coinroutine.core.database.portfolio_db.getPortfolioDatabase
import com.ahmad.raza.coinroutine.core.network.HttpClientFactory
import com.ahmad.raza.coinroutine.portfolio.data.PortfolioRepositoryImpl
import com.ahmad.raza.coinroutine.portfolio.domain.PortfolioRepository
import com.ahmad.raza.coinroutine.portfolio.presentation.PortfolioViewModel
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
    single {
        getPortfolioDatabase(get<RoomDatabase.Builder<PortfolioDatabase>>())
    }
    single { get<PortfolioDatabase>().portfolioDao() }
    single { get<PortfolioDatabase>().balanceDao() }
    viewModel { PortfolioViewModel(get()) }
    singleOf(::PortfolioRepositoryImpl).bind<PortfolioRepository>()
    viewModel { CoinsListViewModel(get(), get()) }
    singleOf(::CoinsListUseCase)
    singleOf(::KtorCoinsRemoteDataSource).bind<CoinsRemoteDataSource>()
    singleOf(::CoinDetailsUseCase)
    singleOf(::CoinPriceHistoryUseCase)

}