package com.ahmad.raza.coinroutine.portfolio.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface BalanceDao {

    @Query("SELECT cashBalance FROM UserBalanceEntity where id = 1")
    suspend fun getCashBalance(): Double?

    @Upsert
    suspend fun insertBalance(userBalanceEntity: UserBalanceEntity)

    @Query("UPDATE UserBalanceEntity SET cashBalance = :newBalance WHERE id = 1")
    suspend fun updateCahBalance(newBalance: Double)
}