package com.ahmad.raza.coinroutine.core.database.portfolio_db

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object PortfolioDatabaseFactory : RoomDatabaseConstructor<PortfolioDatabase>

fun getPortfolioDatabase(
    builder: RoomDatabase.Builder<PortfolioDatabase>
): PortfolioDatabase {
    return builder
        //.addMigrations(MIGRATIONS)
        //.fallbackToDestructiveMigrationOnDowngrade()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

}
