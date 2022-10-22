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

        fillTypeCuotes()

        addSpend()

        checkCuotes()
    }

    private fun checkCuotes() {
        binding.checkBox.setOnClickListener {
            if (binding.checkBox.isChecked) {
                binding.inputCuotes.visibility = View.VISIBLE
            } else {
                binding.inputCuotes.visibility = View.GONE
            }
        }
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

            val nameSpend: String = if(binding.checkBox.isChecked && (!binding.autocompleteCuotes.text.equals("Cuotas"))){
                name + " 1/${binding.autocompleteCuotes.text.toString()}"
            } else{
                name
            }

            val spend =
                Spend(name = nameSpend, description = description, amount = amount, type = type)

            viewModel.addSpend(spend).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {

                        if (binding.checkBox.isChecked && (!binding.autocompleteCuotes.text.equals("Cuotas"))) {
                            addSpendCuotes(spend, name)
                        }
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

    private fun addSpendCuotes(spend: Spend, name: String) {
        val cuotes = binding.autocompleteCuotes.text.toString().toInt()

        for (i in 1 until cuotes) {
            var month = Calendar.getInstance().get(Calendar.MONTH) + 1 + i
            if(month>12){
                month -= 12
            }
            val numberCuote = i+1
            val spendInCuote = Spend(
                name = name + " ${numberCuote}/${cuotes}",
                description = spend.description,
                amount = spend.amount,
                type = spend.type,
                month = month
            )
            viewModel.addSpend(spendInCuote)

            Log.d("cuote", "$spendInCuote")
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

    private fun fillTypeCuotes() {
        val types = resources.getStringArray(R.array.cuotes)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            types
        )

        with(binding.autocompleteCuotes) {
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