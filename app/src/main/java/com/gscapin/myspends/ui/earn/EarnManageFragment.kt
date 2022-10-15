package com.gscapin.myspends.ui.earn

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
import androidx.navigation.fragment.navArgs
import com.gscapin.myspends.R
import com.gscapin.myspends.data.model.Earn
import com.gscapin.myspends.databinding.FragmentEarnManageBinding
import com.gscapin.myspends.presentation.earn.EarnSpendViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.gscapin.myspends.core.Result
import com.gscapin.myspends.ui.home.adapter.EarnsAdapter
import com.gscapin.myspends.ui.home.adapter.OnEarnClickListener
import kotlinx.coroutines.flow.collect
import kotlin.math.log

@AndroidEntryPoint
class EarnManageFragment : Fragment(R.layout.fragment_earn_manage), OnEarnClickListener {

    private lateinit var binding: FragmentEarnManageBinding
    val viewModel: EarnSpendViewModel by viewModels()
    var balanceAmount: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEarnManageBinding.bind(view)

        goHome()

        getEarnList()

        getEarnsCurrentMonth()

        getEarnsLastMonth()



    }

    private fun getEarnsLastMonth() {
        lifecycleScope.launchWhenCreated {
            viewModel.getEarnsByLastMonth()
            viewModel.getTotalEarnLastMonth().collect { result ->
                binding.totalPastMonth.text = "$ $result"
                balanceAmount -= result
                binding.totalBalance.text = "$ $balanceAmount"
            }
        }
    }

    private fun getEarnsCurrentMonth() {
        lifecycleScope.launchWhenCreated {
            viewModel.getEarnsByCurrentMonth()
            viewModel.getTotalCurrentMonth().collect { result ->
                binding.totalPresentMonth.text = "$ $result"
                balanceAmount += result
                binding.totalBalance.text = "$ $balanceAmount"
            }
        }
    }

    private fun getEarnList() {
        lifecycleScope.launchWhenCreated {
            viewModel.getEarns()
            viewModel.getEarnList().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            binding.rvEarns.visibility = View.GONE
                            binding.textEmptyList.visibility = View.VISIBLE
                            return@collect
                        } else {
                            binding.textEmptyList.visibility = View.GONE
                            binding.rvEarns.visibility = View.VISIBLE
                            binding.rvEarns.adapter =
                                EarnsAdapter(result.data, this@EarnManageFragment)
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

    override fun onEarnBtnClick(earn: Earn) {
        TODO("Not yet implemented")
    }
}