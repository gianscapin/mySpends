package com.gscapin.myspends.data.remote.earn

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gscapin.myspends.data.model.Earn
import kotlinx.coroutines.flow.Flow

@Dao
interface EarnDatabaseDao {

    @Query("SELECT * from earns_tbl")
    suspend fun getEarns(): List<Earn>

    @Query("SELECT * from earns_tbl where id = :id")
    suspend fun getEarnById(id: String): Earn

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(earn: Earn)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(earn: Earn)

    @Query("SELECT SUM(amount) from earns_tbl")
    suspend fun getTotalAmount(): Double?

    @Query("DELETE from earns_tbl")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteEarn(earn: Earn)
}