package com.gscapin.myspends.data.remote.earn

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gscapin.myspends.core.DateConverter
import com.gscapin.myspends.core.UUIDConverter
import com.gscapin.myspends.data.model.Earn

@Database(entities = [Earn::class], version = 8, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class)
abstract class EarnDatabase : RoomDatabase() {
    abstract fun earnDao(): EarnDatabaseDao
}