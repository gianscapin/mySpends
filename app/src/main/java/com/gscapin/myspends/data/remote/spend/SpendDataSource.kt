package com.gscapin.myspends.data.remote.spend

import android.util.Log
import com.gscapin.myspends.data.model.Spend
import java.util.*
import javax.inject.Inject

class SpendDataSource @Inject constructor(private val spendDao: SpendDatabaseDao) {
    suspend fun getSpends(): List<Spend> = spendDao.getSpends()
    suspend fun getSpendById(id: String) = spendDao.getSpendById(id = id)
    suspend fun addSpend(spend: Spend) = spendDao.insert(spend)
    suspend fun deleteSpend(spend: Spend) = spendDao.deleteSpend(spend)
    suspend fun getTotalAmount() = spendDao.getTotalAmount()
    suspend fun getSpendsCurrentMonth(): Double? = spendDao.getSpendsByCurrentMonth()
    suspend fun getSpendsLastMonth(): Double? {

        val month = Calendar.getInstance().get(Calendar.MONTH)
        return spendDao.getSpendsByPastMonth()
    }
    suspend fun getSpendsNextMonth(): Double? {

//        val month: Int = Calendar.getInstance().get(Calendar.MONTH) + 2
//
//        Log.d("month", (month-1).toString())
//        Log.d("next month", month.toString())
//
//        val result = spendDao.getSpendsNextMonth()
//
//        Log.d("result", result.toString())
//
//        var totalAmount: Double = 0.0
//
//        result.forEach { spend ->
//            if(spend.amount == month.toDouble()){
//                totalAmount += spend.amount
//            }
//        }
//
//        Log.d("result amount", totalAmount.toString())


        return spendDao.getSpendsByNextMonth()
    }
}