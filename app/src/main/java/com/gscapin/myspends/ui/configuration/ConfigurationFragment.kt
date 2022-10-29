package com.gscapin.myspends.ui.configuration

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.gscapin.myspends.R
import com.gscapin.myspends.databinding.FragmentConfigurationBinding


class ConfigurationFragment : Fragment(R.layout.fragment_configuration) {
    private lateinit var binding: FragmentConfigurationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentConfigurationBinding.bind(view)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = 0

        goHome()

        configSwitch(sharedPref, defaultValue)


    }

    private fun configSwitch(sharedPref: SharedPreferences?, defaultValue: Int) {
        binding.btnNightMode.isChecked = sharedPref?.getInt("nightMode", defaultValue) == 1

        binding.btnNightMode.setOnCheckedChangeListener { compoundButton, check ->
            if (check) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPref!!.edit().putInt("nightMode", 1).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPref!!.edit().putInt("nightMode", 0).apply()
            }
        }
    }

    private fun goHome() {
        binding.navigationIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}