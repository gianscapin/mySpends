package com.gscapin.myspends.data.remote.spend

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gscapin.myspends.core.DateConverter
import com.gscapin.myspends.core.UUIDConverter
import com.gscapin.myspends.data.model.Spend

@Database(entities = [Spend::class], version = 4, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class)
abstract class SpendDatabase : RoomDatabase() {
    abstract fun spendDao(): SpendDatabaseDao
}