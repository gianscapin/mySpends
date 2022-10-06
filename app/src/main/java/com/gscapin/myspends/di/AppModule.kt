package com.gscapin.myspends.di

import android.content.Context
import androidx.room.Room
import com.gscapin.myspends.data.remote.earn.EarnDataSource
import com.gscapin.myspends.data.remote.earn.EarnDatabase
import com.gscapin.myspends.data.remote.earn.EarnDatabaseDao
import com.gscapin.myspends.data.remote.spend.SpendDataSource
import com.gscapin.myspends.data.remote.spend.SpendDatabase
import com.gscapin.myspends.data.remote.spend.SpendDatabaseDao
import com.gscapin.myspends.repository.earn.EarnRepositoryImpl
import com.gscapin.myspends.repository.spend.SpendRepositoryImpl
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
    fun provideSpendDao(spendDatabase: SpendDatabase): SpendDatabaseDao = spendDatabase.spendDao()

    @Singleton
    @Provides
    fun provideEarnDatabase(@ApplicationContext context: Context): EarnDatabase = Room.databaseBuilder(
        context,
        EarnDatabase::class.java,
        "earns_db"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideSpendDatabase(@ApplicationContext context: Context): SpendDatabase = Room.databaseBuilder(
        context,
        SpendDatabase::class.java,
        "spends_tbl"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    fun provideEarnRepository(dataSource: EarnDataSource) = EarnRepositoryImpl(dataSource)

    @Singleton
    fun provideSpendRepository(dataSource: SpendDataSource) = SpendRepositoryImpl(dataSource)
}