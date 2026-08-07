package com.ahmad.raza.coinroutine.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmad.raza.coinroutine.core.database.portfolio_db.PortfolioDatabase
import platform.Foundation.NSHomeDirectory

fun getPortfolioDatabaseBuilder(): RoomDatabase.Builder<PortfolioDatabase> {
    val dbFile = NSHomeDirectory() + "/portfolio.db"
    return Room.databaseBuilder<PortfolioDatabase>(
        dbFile
    )
}