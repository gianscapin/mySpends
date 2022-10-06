package com.gscapin.myspends.repository.spend

import com.gscapin.myspends.data.model.Spend
import javax.inject.Singleton

@Singleton
interface SpendRepository {

    suspend fun getSpends(): List<Spend>
    suspend fun getSpendById(id: String): Spend
    suspend fun addSpend(spend: Spend)
    suspend fun deleteSpend(spend: Spend)
    suspend fun getTotalAmount(): Double
}