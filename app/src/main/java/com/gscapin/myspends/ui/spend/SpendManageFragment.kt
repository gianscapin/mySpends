package com.gscapin.myspends.ui.spend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gscapin.myspends.R
import com.gscapin.myspends.core.Result
import com.gscapin.myspends.data.model.Spend
import com.gscapin.myspends.databinding.FragmentSpendManageBinding
import com.gscapin.myspends.presentation.earn.EarnSpendViewModel
import com.gscapin.myspends.ui.home.adapter.EarnsAdapter
import com.gscapin.myspends.ui.home.adapter.OnSpendClickListener
import com.gscapin.myspends.ui.home.adapter.SpendsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class SpendManageFragment : Fragment(R.layout.fragment_spend_manage), OnSpendClickListener {
    private lateinit var binding: FragmentSpendManageBinding
    val viewModel: EarnSpendViewModel by viewModels()
    var balanceAmount: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSpendManageBinding.bind(view)

        goHome()

        getSpendList()

        getSpendsCurrentMonth()

        getSpendsLastMonth()
    }

    private fun getSpendsLastMonth() {
        lifecycleScope.launchWhenCreated {
            viewModel.getSpendsByLastMonth()
            viewModel.getTotalEarnLastMonth().collect { result ->
                binding.totalPastMonth.text = "$ $result"
                balanceAmount -= result
                binding.totalBalance.text = "$ $balanceAmount"
            }
        }
    }

    private fun getSpendsCurrentMonth() {
        lifecycleScope.launchWhenCreated {
            viewModel.getSpendsByCurrentMonth()
            viewModel.getTotalSpendsCurrentMonth().collect { result ->
                binding.totalPresentMonth.text = "$ $result"
                balanceAmount += result
                binding.totalBalance.text = "$ $balanceAmount"
            }
        }
    }

    private fun getSpendList() {
        lifecycleScope.launchWhenCreated {
            viewModel.getSpends()
            viewModel.getSpendList().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            binding.rvSpends.visibility = View.GONE
                            binding.textEmptyList.visibility = View.VISIBLE
                            return@collect
                        } else {
                            binding.textEmptyList.visibility = View.GONE
                            binding.rvSpends.visibility = View.VISIBLE
                            binding.rvSpends.adapter =
                                SpendsAdapter(result.data, this@SpendManageFragment)
                        }
                    }
                    is Result.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "Error ${result.exception}",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d("error db", "${result.exception}")
                    }
                }
            }
        }
    }

    private fun goHome() {
        binding.navigationIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onSpendBtnClick(spend: Spend) {
        TODO("Not yet implemented")
    }
}