package com.gscapin.myspends.data.remote.spend

import androidx.room.*
import com.gscapin.myspends.data.model.Spend
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendDatabaseDao {

    @Query("SELECT * from spends_tbl where month = strftime('%m', 'now')")
    suspend fun getSpends(): List<Spend>

    @Query("SELECT * from spends_tbl where id = :id")
    suspend fun getSpendById(id: String): Spend

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(spend: Spend)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(spend: Spend)

    @Query("DELETE from spends_tbl")
    suspend fun deleteAll()

    @Query("SELECT SUM(amount) from spends_tbl where month = strftime('%m', 'now')")
    suspend fun getTotalAmount(): Double?

    @Query("SELECT SUM(amount) from spends_tbl where month = strftime('%m', 'now')")
    suspend fun getSpendsByCurrentMonth(): Double?

    @Query("SELECT SUM(amount) from spends_tbl where month = strftime('%m', 'now', '-1 month')")
    suspend fun getSpendsByPastMonth(): Double?

    @Query("SELECT SUM(amount) from spends_tbl where month = strftime('%m', 'now', '+1 month')")
    suspend fun getSpendsByNextMonth(): Double?


    @Query("SELECT * from spends_tbl")
    suspend fun getSpendsNextMonth(): List<Spend>

    @Delete
    suspend fun deleteSpend(spend: Spend)
}