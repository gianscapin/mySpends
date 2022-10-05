package com.gscapin.myspends.data.remote.earn

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gscapin.myspends.core.UUIDConverter
import com.gscapin.myspends.data.model.Earn

@Database(entities = [Earn::class], version = 2, exportSchema = false)
@TypeConverters(UUIDConverter::class)
abstract class EarnDatabase: RoomDatabase() {
    abstract fun earnDao(): EarnDatabaseDao
}