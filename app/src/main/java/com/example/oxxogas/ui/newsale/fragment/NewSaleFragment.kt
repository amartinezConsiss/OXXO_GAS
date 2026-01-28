package com.example.oxxogas.ui.newsale.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.oxxogas.R
import com.example.oxxogas.databinding.FragmentNewSaleBinding
import com.example.oxxogas.ui.home.activities.HomeActivity
import com.example.oxxogas.ui.main.fragments.BaseFragment
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.newsale.adapters.NewSalePetrolPumpAdapter
import com.example.oxxogas.ui.newsale.viewmodels.NewSaleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewSaleFragment :
    BaseFragment<FragmentNewSaleBinding>() {

    private lateinit var adapter: NewSalePetrolPumpAdapter
    private val viewModel by viewModels<NewSaleViewModel>()

    override fun initBinding(): FragmentNewSaleBinding =
        FragmentNewSaleBinding.inflate(layoutInflater)

    private var parentActivity: HomeActivity? = null

    override fun initView(view: View, savedState: Bundle?) {
        parentActivity = activity as? HomeActivity
        parentActivity?.setupToolbar(binding.toolbarNewSale, getString(R.string.new_sale), true)
        initComponents()
    }

    private fun initComponents() {
        adapter =
            NewSalePetrolPumpAdapter(viewModel.getPumpsData(requireContext())) { pompId, status ->
                if (status == 1) {
                    goToPaymentFragment(pompId)
                } else {
                    showDisabledPomp(status, pompId)
                }

            }
        binding.rvPetrolPumps.adapter = adapter
        binding.rvPetrolPumps.isNestedScrollingEnabled = false
    }

    private fun showDisabledPomp(idStatus: Int, pumpId: Int) {
        when (idStatus) {
            2 -> {
                showErrorAlert(
                    R.drawable.img_gas_blue,
                    R.string.error,
                    R.drawable.img_bad_alert,
                    R.string.error_connection
                )
            }

        }
    }

    private fun showErrorAlert(
        iconTypeError: Int,
        titleError: Int,
        iconError: Int,
        messageError: Int
    ) {
        lifecycleScope.launch {
            parentActivity?.showErrorBottomDialog(
                iconTypeError,
                titleError,
                iconError,
                messageError
            )
            delay(2500)
            parentActivity?.dismissErrorBottomDialog()
        }

    }

    private fun goToPaymentFragment(pumpId: Int) {
        Bundle().apply {
            putInt(Constants.PUMP_ID_KEY, pumpId)
            findNavController().navigate(R.id.action_payment_fragment, this)
        }
    }


}