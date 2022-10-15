package com.gscapin.myspends.data.remote.earn

import com.gscapin.myspends.data.model.Earn
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EarnDataSource @Inject constructor(private val earnDao: EarnDatabaseDao) {

    suspend fun getEarns(): List<Earn> = earnDao.getEarns()
    suspend fun getEarnById(id: String) = earnDao.getEarnById(id = id)
    suspend fun addEarn(earn: Earn) = earnDao.insert(earn)
    suspend fun deleteEarn(earn: Earn) = earnDao.deleteEarn(earn)
    suspend fun getTotalAmount() = earnDao.getTotalAmount()
    suspend fun getEarnsByCurrentMonth(): Double? = earnDao.getEarnsByCurrentMonth()
    suspend fun getEarnsByLastMonth() = earnDao.getEarnsByPastMonth()
}