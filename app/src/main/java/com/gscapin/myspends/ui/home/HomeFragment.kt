package com.gscapin.myspends.ui.home

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = 0

        configNightMode(sharedPref, defaultValue)

        configStatusBar()

        goConfiguration()

        addSpend()
        addEarn()
        loadEarns()
        loadSpends()

        getTotal()
        goEarnsManage()
        goSpendsManage()

        //viewModel.getAmountSpends()

    }

    private fun configNightMode(
        sharedPref: SharedPreferences?,
        defaultValue: Int
    ) {
        if (sharedPref?.getInt("nightMode", defaultValue) == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            WindowInsetsControllerCompat(
                requireActivity().window,
                requireActivity().window.decorView
            ).isAppearanceLightStatusBars = false
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            WindowInsetsControllerCompat(
                requireActivity().window,
                requireActivity().window.decorView
            ).isAppearanceLightStatusBars = true
        }
    }

    private fun configStatusBar() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            com.google.android.material.R.color.mtrl_btn_transparent_bg_color
        )
    }

    private fun goConfiguration() {
        binding.btnConfiguration.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_configurationFragment)
        }
    }

    private fun goSpendsManage() {
        binding.cardSpends.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_spendManageFragment)
        }
    }

    private fun goEarnsManage() {
        binding.cardEarns.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_earnManageFragment)
        }
    }

    private fun getTotal() {
        lifecycleScope.launchWhenCreated {
            viewModel.getTotalAmount().collect { result ->
                total = result
                binding.total.text = "Total: $ ${result}"


                var totalProgressIndicator: Double = 0.0
                if (result > 1) {
                    totalProgressIndicator = (total * 100) / totalEarn
                }
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
                getTotal()
            }
        }

    }

    private fun getSpendsAmount() {
        viewModel.getAmountSpends()
        lifecycleScope.launchWhenCreated {
            viewModel.getTotalSpends().collect { result ->
                totalSpend = result
                binding.totalSpends.text = "Gastos totales: $ $result"
                getTotal()
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
                            binding.rvEarns.visibility = View.GONE
                            binding.messageEarn.visibility = View.VISIBLE
                            return@collect
                        } else {
                            binding.messageEarn.visibility = View.GONE
                            binding.rvEarns.visibility = View.VISIBLE
                            binding.rvEarns.adapter = EarnsAdapter(result.data, this@HomeFragment)
                            getEarnsAmount()

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
                            binding.rvSpends.visibility = View.GONE
                            binding.messageSpend.visibility = View.VISIBLE
                            return@collect
                        } else {
                            binding.messageSpend.visibility = View.GONE
                            binding.rvSpends.visibility = View.VISIBLE
                            binding.rvSpends.adapter = SpendsAdapter(result.data, this@HomeFragment)
                            getSpendsAmount()
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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Desea eliminar la ganancia seleccionada?")
            .setMessage("Se eliminar?? la ganancia: ${earn.name}")
            .setPositiveButton(
                "Eliminar"
            ) { dialog, p1 ->
                deleteEarn(earn)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, p1 ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteEarn(earn: Earn) {
        viewModel.deleteEarn(earn).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    loadEarns()
                    loadSpends()
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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Desea eliminar el gasto?")
            .setMessage("El gasto eliminado es: ${spend.name}")
            .setPositiveButton("Eliminar") { dialog, p0 ->
                deleteSpend(spend)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, p0 ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteSpend(spend: Spend) {
        viewModel.deleteSpend(spend).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    loadEarns()
                    loadSpends()
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