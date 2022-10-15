package com.gscapin.myspends.data.remote.spend

import com.gscapin.myspends.data.model.Spend
import javax.inject.Inject

class SpendDataSource @Inject constructor(private val spendDao: SpendDatabaseDao) {
    suspend fun getSpends(): List<Spend> = spendDao.getSpends()
    suspend fun getSpendById(id: String) = spendDao.getSpendById(id = id)
    suspend fun addSpend(spend: Spend) = spendDao.insert(spend)
    suspend fun deleteSpend(spend: Spend) = spendDao.deleteSpend(spend)
    suspend fun getTotalAmount() = spendDao.getTotalAmount()
    suspend fun getSpendsCurrentMonth(): Double? = spendDao.getSpendsByCurrentMonth()
    suspend fun getSpendsLastMonth(): Double? = spendDao.getSpendsByPastMonth()
}