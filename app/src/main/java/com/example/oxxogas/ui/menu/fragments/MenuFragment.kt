package com.example.oxxogas.ui.menu.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.oxxogas.R
import com.example.oxxogas.databinding.FragmentMenuBinding
import com.example.oxxogas.ui.menu.adapters.MenuHomeAdapter
import com.example.oxxogas.ui.main.fragments.BaseFragment
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.menu.viewmodels.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    private val viewModel by viewModels<MenuViewModel>()

    override fun initBinding() = FragmentMenuBinding.inflate(layoutInflater)

    override fun initView(view: View, savedState: Bundle?) {
        initComponents()
        menuActions()
        loadDummyInformation()
    }

    private fun initComponents() {
        binding.rvMenu.isNestedScrollingEnabled = false
    }

    private fun menuActions() {
        binding.rvMenu.adapter = MenuHomeAdapter(viewModel.getMenuInformation()) { itemId ->
            when (itemId) {
                1 -> findNavController().navigate(R.id.action_new_sale_fragment)
            }
        }
    }

    private fun loadDummyInformation() {
        binding.tvCurrentCash.text =
            getString(R.string.mxn, setCurrencyFormat(viewModel.getCurrentCash()))
        binding.tvCumulativeSale.text =
            getString(R.string.lts, String.format("%.2f", viewModel.getCumulativeSale()))
    }

}