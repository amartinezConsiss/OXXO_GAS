package com.example.oxxogas.ui.menu.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.oxxogas.R
import com.example.oxxogas.databinding.FragmentMenuBinding
import com.example.oxxogas.ui.menu.adapters.MenuHomeAdapter
import com.example.oxxogas.ui.main.fragments.BaseFragment

class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    override fun initBinding() = FragmentMenuBinding.inflate(layoutInflater)

    override fun initView(view: View, savedState: Bundle?) {
        initComponents()
        menuActions()
        loadDummyInformation()
    }

    private fun initComponents(){
        binding.rvMenu.isNestedScrollingEnabled = false
    }

    private fun menuActions() {
        binding.rvMenu.adapter = MenuHomeAdapter { itemId ->
            when (itemId) {
                1 -> findNavController().navigate(R.id.action_new_sale_fragment)
            }
        }
    }

    private fun loadDummyInformation(){
        /*binding.tvCumulativeSale.text = "808.99"
        binding.tvCashAmount.text = "6,900.64"*/
    }

}