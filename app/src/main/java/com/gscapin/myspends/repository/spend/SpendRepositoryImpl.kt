package com.gscapin.myspends.repository.spend

import com.gscapin.myspends.data.model.Spend
import com.gscapin.myspends.data.remote.spend.SpendDataSource
import javax.inject.Inject

class SpendRepositoryImpl @Inject constructor(private val dataSource: SpendDataSource): SpendRepository {
    override suspend fun getSpends(): List<Spend> = dataSource.getSpends()

    override suspend fun getSpendById(id: String): Spend = dataSource.getSpendById(id)

    override suspend fun addSpend(spend: Spend) = dataSource.addSpend(spend)

    override suspend fun deleteSpend(spend: Spend) = dataSource.deleteSpend(spend)
    override suspend fun getTotalAmount(): Double? = dataSource.getTotalAmount()
    override suspend fun getSpendsCurrentMonth(): Double? = dataSource.getSpendsCurrentMonth()
    override suspend fun getSpendsLastMonth(): Double? = dataSource.getSpendsLastMonth()
    override suspend fun getSpendsNextMonth(): Double? = dataSource.getSpendsNextMonth()

}