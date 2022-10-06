package com.gscapin.myspends.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gscapin.myspends.R
import com.gscapin.myspends.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import com.gscapin.myspends.core.Result
import com.gscapin.myspends.data.model.Earn
import com.gscapin.myspends.data.model.Spend
import com.gscapin.myspends.presentation.earn.EarnSpendViewModel
import com.gscapin.myspends.ui.home.adapter.EarnsAdapter
import com.gscapin.myspends.ui.home.adapter.OnEarnClickListener
import com.gscapin.myspends.ui.home.adapter.OnSpendClickListener
import com.gscapin.myspends.ui.home.adapter.SpendsAdapter

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnEarnClickListener, OnSpendClickListener {
    private lateinit var binding: FragmentHomeBinding

    val viewModel: EarnSpendViewModel by viewModels()
    var totalEarn: Double = 0.0
    var totalSpend: Double = 0.0
    var total: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        addSpend()
        addEarn()
        loadEarns()
        loadSpends()

        getEarnsAmount()
        getSpendsAmount()
        getTotal()


        //viewModel.getAmountSpends()

    }

    private fun getTotal() {
        lifecycleScope.launchWhenCreated {
            viewModel.getTotalAmount().collect{result ->
                total = result
                binding.total.text = "Total: $ ${result}"

                val totalProgressIndicator= (total * 100) / totalEarn
                binding.circularProgressIndicator.progress = totalProgressIndicator.toInt()
                binding.totalCircular.text = totalProgressIndicator.toInt().toString()
            }
        }
    }

    private fun addEarn() {
        binding.addEarn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_earnFragment)
        }
    }

    private fun addSpend() {
        binding.addSpend.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_spendFragment)
        }
    }

    private fun getEarnsAmount() {
        viewModel.getAmountEarns()
        lifecycleScope.launchWhenCreated {
            viewModel.getTotalEarns().collect { result ->
                totalEarn = result
                binding.totalEarns.text = "Total ganancias: $ ${result}"
            }
        }
    }

    private fun getSpendsAmount() {
        viewModel.getAmountSpends()
        lifecycleScope.launchWhenCreated {
            viewModel.getTotalSpends().collect{ result ->
                totalSpend = result
                binding.totalSpends.text = "Gastos totales: $ $result"
            }
        }
    }

    private fun loadEarns() {
        viewModel.getEarns()
        lifecycleScope.launchWhenCreated {
            viewModel.getEarnList().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            return@collect
                        } else {
                            binding.rvEarns.adapter = EarnsAdapter(result.data, this@HomeFragment)
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

    private fun loadSpends() {
        viewModel.getSpends()
        lifecycleScope.launchWhenCreated {
            viewModel.getSpendList().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            return@collect
                        } else {
                            binding.rvSpends.adapter = SpendsAdapter(result.data, this@HomeFragment)
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

    override fun onEarnBtnClick(earn: Earn) {
        viewModel.deleteEarn(earn).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading -> {}
                is Result.Success -> {
                    getEarnsAmount()
                    getSpendsAmount()
                }
                is Result.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        "Error ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun onSpendBtnClick(spend: Spend) {
        viewModel.deleteSpend(spend).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading -> {}
                is Result.Success -> {
                    loadEarns()
                    loadSpends()
                    getEarnsAmount()
                    getSpendsAmount()
                }
                is Result.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        "Error ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}