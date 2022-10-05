package com.gscapin.myspends.di

import android.content.Context
import androidx.room.Room
import com.gscapin.myspends.data.remote.earn.EarnDatabase
import com.gscapin.myspends.data.remote.earn.EarnDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideEarnDao(earnDatabase: EarnDatabase): EarnDatabaseDao = earnDatabase.earnDao()

    @Singleton
    @Provides
    fun provideEarnDatabase(@ApplicationContext context: Context): EarnDatabase = Room.databaseBuilder(
        context,
        EarnDatabase::class.java,
        "earns_db"
    ).fallbackToDestructiveMigration().build()
}