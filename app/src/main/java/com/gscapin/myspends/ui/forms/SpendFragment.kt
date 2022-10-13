package com.gscapin.myspends.ui.forms

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gscapin.myspends.R
import com.gscapin.myspends.data.model.Spend
import com.gscapin.myspends.databinding.FragmentSpendBinding
import com.gscapin.myspends.presentation.earn.EarnSpendViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log
import com.gscapin.myspends.core.Result
import java.time.LocalDate
import java.util.Calendar

@AndroidEntryPoint
class SpendFragment : Fragment(R.layout.fragment_spend) {
    private lateinit var binding: FragmentSpendBinding

    var nameInputFill: Boolean = false
    var amountInputFill: Boolean = false
    var typeInputFill: Boolean = false

    val viewModel: EarnSpendViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSpendBinding.bind(view)

        goHome()

        configNameInput()

        configAmountInput()

        configTypeInput()

        fillTypeInput()

        addSpend()
    }

    private fun goHome() {
        binding.navigationIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addSpend() {
        binding.addSpendBtn.setOnClickListener {
            val name = binding.nameSpend.text.toString()
            val description = binding.descriptionSpend.text.toString()
            val amount = binding.amountSpend.text.toString().toDouble()
            val type = binding.autocompleteTypes.text.toString()

            val spend: Spend =
                Spend(name = name, description = description, amount = amount, type = type)

            viewModel.addSpend(spend).observe(viewLifecycleOwner, Observer { result ->
                when(result){
                    is Result.Loading -> {}
                    is Result.Success -> {
                        findNavController().popBackStack()
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

    private fun fillTypeInput() {
        val types = resources.getStringArray(R.array.types_spends)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            types
        )

        with(binding.autocompleteTypes) {
            setAdapter(adapter)
        }
    }

    private fun configTypeInput() {
        binding.autocompleteTypes.addTextChangedListener {
            val typeInput = binding.autocompleteTypes.text.toString()
            if (typeInput.isEmpty()) {
                binding.autocompleteTypes.error = "El monto es obligatorio."
                if (typeInputFill) {
                    typeInputFill = false
                }
            } else {
                typeInputFill = true
            }
            isEnableBtn()
        }
    }

    private fun configAmountInput() {
        binding.amountSpend.addTextChangedListener {
            val amountInput = binding.nameSpend.text.toString()
            if (amountInput.isEmpty()) {
                binding.amountSpend.error = "El monto es obligatorio."
                if (amountInputFill) {
                    amountInputFill = false
                }
            } else {
                amountInputFill = true
            }
            isEnableBtn()
        }
    }

    private fun configNameInput() {
        binding.nameSpend.addTextChangedListener {
            val textInput = binding.nameSpend.text.toString()
            if (textInput.isEmpty()) {
                binding.nameSpend.error = "El nombre es obligatorio."
                if (nameInputFill) {
                    nameInputFill = false
                }
            } else {
                nameInputFill = true
            }
            isEnableBtn()
        }
    }

    private fun isEnableBtn() {
        binding.addSpendBtn.isEnabled = nameInputFill && amountInputFill && typeInputFill
    }
}