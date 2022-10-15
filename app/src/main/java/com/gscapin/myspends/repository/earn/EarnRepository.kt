package com.gscapin.myspends.repository.earn

import com.gscapin.myspends.data.model.Earn
import javax.inject.Singleton

@Singleton
interface EarnRepository {

    suspend fun getEarns(): List<Earn>
    suspend fun getEarnById(id: String): Earn
    suspend fun addEarn(earn: Earn)
    suspend fun deleteEarn(earn: Earn)
    suspend fun getTotalAmount(): Double?
    suspend fun getEarnsByCurrentMonth(): Double?
    suspend fun getEarnsByLastMonth(): Double?
}