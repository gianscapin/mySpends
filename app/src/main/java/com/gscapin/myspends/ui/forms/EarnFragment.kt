package com.gscapin.myspends.ui.forms

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.gscapin.myspends.R
import com.gscapin.myspends.data.model.Earn
import com.gscapin.myspends.data.model.Spend
import com.gscapin.myspends.databinding.FragmentEarnBinding

class EarnFragment : Fragment(R.layout.fragment_earn) {

    private lateinit var binding: FragmentEarnBinding

    var nameInputFill: Boolean = false
    var amountInputFill: Boolean = false
    var typeInputFill: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEarnBinding.bind(view)

        goHome()
        configNameInput()
        configAmountInput()
        configTypeInput()
        fillTypeInput()
        addEarn()

    }

    private fun goHome() {
        binding.materialToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun configNameInput() {
        binding.nameEarn.addTextChangedListener {
            val textInput = binding.nameEarn.text.toString()
            if (textInput.isEmpty()) {
                binding.nameEarn.error = "El nombre es obligatorio."
                if (nameInputFill) {
                    nameInputFill = false
                }
            } else {
                nameInputFill = true
            }
            isEnableBtn()
        }
    }

    private fun configAmountInput() {
        binding.amountEarn.addTextChangedListener {
            val amountInput = binding.amountEarn.text.toString()
            if (amountInput.isEmpty()) {
                binding.amountEarn.error = "El monto es obligatorio."
                if (amountInputFill) {
                    amountInputFill = false
                }
            } else {
                amountInputFill = true
            }
            isEnableBtn()
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

    private fun fillTypeInput() {
        val types = resources.getStringArray(R.array.types_earns)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            types
        )

        with(binding.autocompleteTypes) {
            setAdapter(adapter)
        }
    }

    private fun isEnableBtn() {
        binding.addEarnBtn.isEnabled = nameInputFill && amountInputFill && typeInputFill
    }

    private fun addEarn() {
        binding.addEarnBtn.setOnClickListener {
            val name = binding.nameEarn.text.toString()
            val amount = binding.amountEarn.text.toString().toDouble()
            val type = binding.autocompleteTypes.text.toString()

            val earn: Earn =
                Earn(name = name, amount = amount, type = type)

            Log.d("Earn", "$earn")
        }
    }

}