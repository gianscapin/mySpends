package com.gscapin.myspends.presentation.earn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.gscapin.myspends.data.model.Earn
import com.gscapin.myspends.repository.earn.EarnRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import com.gscapin.myspends.core.Result
import com.gscapin.myspends.data.model.Spend
import com.gscapin.myspends.repository.spend.SpendRepository
import com.gscapin.myspends.repository.spend.SpendRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EarnSpendViewModel @Inject constructor(
    private val repositoryEarn: EarnRepositoryImpl,
    private val repositorySpend: SpendRepositoryImpl
) : ViewModel() {

    private val earnsState = MutableStateFlow<Result<List<Earn>>>(Result.Loading())
    private val spendsState = MutableStateFlow<Result<List<Spend>>>(Result.Loading())
    private val earnsCurrentMonth = MutableStateFlow<Double>(0.0)
    private val earnsLastMonth = MutableStateFlow<Double>(0.0)
    private val spendsCurrentMonth = MutableStateFlow<Double>(0.0)
    private val spendsLastMonth = MutableStateFlow<Double>(0.0)
    private val totalAmountEarnsState = MutableStateFlow<Double>(0.0)
    private val totalAmountSpendsState = MutableStateFlow<Double>(0.0)
    private val totalAmount = MutableStateFlow<Double>(0.0)

    fun addEarn(earn: Earn) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repositoryEarn.addEarn(earn)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun deleteEarn(earn: Earn) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repositoryEarn.deleteEarn(earn)))
            getEarns()
            getAmountEarns()
            calculateTotalAmount()
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun addSpend(spend: Spend) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repositorySpend.addSpend(spend)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun deleteSpend(spend: Spend) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repositorySpend.deleteSpend(spend)))
            getSpends()
            getAmountSpends()
            calculateTotalAmount()
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun getEarns() = viewModelScope.launch {
        kotlin.runCatching {
            earnsState.value = Result.Success(repositoryEarn.getEarns())
        }.onFailure {
            earnsState.value = Result.Failure(Exception(it.message))
        }
    }

    fun getEarnsByCurrentMonth() = viewModelScope.launch {
        kotlin.runCatching {
            earnsCurrentMonth.value = repositoryEarn.getEarnsByCurrentMonth()!!
        }.onFailure {
            earnsCurrentMonth.value = 0.0
        }
    }

    fun getSpendsByCurrentMonth() = viewModelScope.launch {
        kotlin.runCatching {
            spendsCurrentMonth.value = repositorySpend.getSpendsCurrentMonth()!!
        }.onFailure {
            spendsCurrentMonth.value = 0.0
        }
    }

    fun getEarnsByLastMonth() = viewModelScope.launch {
        kotlin.runCatching {
            earnsLastMonth.value = repositoryEarn.getEarnsByLastMonth()!!
        }.onFailure {
            earnsLastMonth.value = 0.0
        }
    }

    fun getSpendsByLastMonth() = viewModelScope.launch {
        kotlin.runCatching {
            spendsLastMonth.value = repositorySpend.getSpendsLastMonth()!!
        }.onFailure {
            spendsLastMonth.value = 0.0
        }
    }

    fun getSpends() = viewModelScope.launch {
        kotlin.runCatching {
            spendsState.value = Result.Success(repositorySpend.getSpends())
        }.onFailure {
            spendsState.value = Result.Failure(Exception(it.message))
        }
    }

    fun getEarnList(): StateFlow<Result<List<Earn>>> = earnsState
    fun getSpendList(): StateFlow<Result<List<Spend>>> = spendsState
    fun getTotalCurrentMonth(): StateFlow<Double> = earnsCurrentMonth
    fun getTotalEarnLastMonth(): StateFlow<Double> = earnsLastMonth
    fun getTotalSpendsCurrentMonth(): StateFlow<Double> = spendsCurrentMonth
    fun getTotalSpendsLastMonth(): StateFlow<Double> = spendsLastMonth

    fun getAmountEarns() = viewModelScope.launch {
        kotlin.runCatching {
            totalAmountEarnsState.value = repositoryEarn.getTotalAmount()!!
            calculateTotalAmount()
        }.onFailure {
            totalAmountEarnsState.value = 0.0
        }
    }

    fun getAmountSpends() = viewModelScope.launch {
        kotlin.runCatching {
            totalAmountSpendsState.value = repositorySpend.getTotalAmount()!!
            calculateTotalAmount()
        }.onFailure {
            totalAmountSpendsState.value = 0.0
        }
    }

    fun getTotalSpends(): StateFlow<Double> = totalAmountSpendsState
    fun getTotalEarns(): StateFlow<Double> = totalAmountEarnsState
    fun getTotalAmount(): StateFlow<Double> = totalAmount
    private fun calculateTotalAmount() {
        totalAmount.value = totalAmountEarnsState.value - totalAmountSpendsState.value
    }

}