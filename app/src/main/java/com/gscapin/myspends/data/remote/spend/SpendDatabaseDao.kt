package com.gscapin.myspends.data.remote.spend

import androidx.room.*
import com.gscapin.myspends.data.model.Spend
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendDatabaseDao {

    @Query("SELECT * from spends_tbl")
    suspend fun getSpends(): List<Spend>

    @Query("SELECT * from spends_tbl where id = :id")
    suspend fun getSpendById(id: String): Spend

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(spend: Spend)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(spend: Spend)

    @Query("DELETE from spends_tbl")
    suspend fun deleteAll()

    @Query("SELECT SUM(amount) from spends_tbl")
    suspend fun getTotalAmount(): Double

    @Delete
    suspend fun deleteSpend(spend: Spend)
}