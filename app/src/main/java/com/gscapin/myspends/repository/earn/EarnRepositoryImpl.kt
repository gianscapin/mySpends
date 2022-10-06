package com.gscapin.myspends.repository.earn

import com.gscapin.myspends.data.model.Earn
import com.gscapin.myspends.data.remote.earn.EarnDataSource
import javax.inject.Inject

class EarnRepositoryImpl @Inject constructor(private val dataSource: EarnDataSource): EarnRepository {
    override suspend fun getEarns(): List<Earn> = dataSource.getEarns()

    override suspend fun getEarnById(id: String): Earn = dataSource.getEarnById(id)

    override suspend fun addEarn(earn: Earn) = dataSource.addEarn(earn)

    override suspend fun deleteEarn(earn: Earn) = dataSource.deleteEarn(earn)
    override suspend fun getTotalAmount(): Double = dataSource.getTotalAmount()
}