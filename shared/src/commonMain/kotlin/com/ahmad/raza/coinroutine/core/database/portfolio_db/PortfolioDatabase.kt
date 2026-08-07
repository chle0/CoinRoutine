package com.ahmad.raza.coinroutine.core.database.portfolio_db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmad.raza.coinroutine.portfolio.data.local.BalanceDao
import com.ahmad.raza.coinroutine.portfolio.data.local.PortfolioCoinEntity
import com.ahmad.raza.coinroutine.portfolio.data.local.PortfolioDao
import com.ahmad.raza.coinroutine.portfolio.data.local.UserBalanceEntity

@Database(entities = [PortfolioCoinEntity::class, UserBalanceEntity::class], version = 2)
@ConstructedBy(PortfolioDatabaseFactory::class)
abstract class PortfolioDatabase : RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao
    abstract fun balanceDao(): BalanceDao
}