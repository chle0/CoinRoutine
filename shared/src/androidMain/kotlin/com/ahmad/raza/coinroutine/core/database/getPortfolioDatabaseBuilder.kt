package com.ahmad.raza.coinroutine.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmad.raza.coinroutine.core.database.portfolio_db.PortfolioDatabase

fun getPortfolioDatabaseBuilder(context: Context): RoomDatabase.Builder<PortfolioDatabase> {
    val dbFile = context.getDatabasePath("portfolio.db")
    return Room.databaseBuilder<PortfolioDatabase>(
        context, dbFile.absolutePath
    )
}