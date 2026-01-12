package com.example.oxxogas.ui.newsale.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.oxxogas.R
import com.example.oxxogas.domain.models.PetrolPumpsList
import com.example.oxxogas.databinding.FragmentNewSaleBinding
import com.example.oxxogas.ui.home.activities.HomeActivity
import com.example.oxxogas.ui.main.fragments.BaseFragment
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.newsale.dialogs.DisabledPumpBottomSheetDialog
import com.example.oxxogas.ui.newsale.adapters.NewSalePetrolPumpAdapter
import com.example.oxxogas.ui.newsale.dialogs.AskSpinBottomSheetDialog
import com.example.oxxogas.ui.newsale.dialogs.ErrorConnectionBottomSheetDialog
import com.example.oxxogas.ui.newsale.dialogs.SpinPremiaBottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewSaleFragment :
    BaseFragment<FragmentNewSaleBinding>() {

    private lateinit var adapter: NewSalePetrolPumpAdapter

    override fun initBinding(): FragmentNewSaleBinding =
        FragmentNewSaleBinding.inflate(layoutInflater)

    private var parentActivity: HomeActivity? = null

    override fun initView(view: View, savedState: Bundle?) {
        parentActivity = activity as? HomeActivity
        parentActivity?.setupToolbar(binding.toolbarNewSale, getString(R.string.back), true)
        initComponents()
    }

    private fun initComponents() {
        adapter =
            NewSalePetrolPumpAdapter(getDummyInformation()) { pompId, status ->
                if (status) {
                    if (pompId == 6) {
                        showErrorConnection()
                    } else {
                        showAskSpinPremiaDialog(pompId)
                    }
                } else {
                    showDisabledPomp()
                }

            }
        binding.rvPetrolPumps.adapter = adapter
        binding.rvPetrolPumps.isNestedScrollingEnabled = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showAskSpinPremiaDialog(pumpId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            showAlert()

            val askSpinPremiaDialog = AskSpinBottomSheetDialog()
            askSpinPremiaDialog.onPositiveActionClick = {
                showSpinPremiaDialog(pumpId)
            }

            askSpinPremiaDialog.onNegativeActionClick = {
                goToPaymentMethods(pumpId, false)
            }

            askSpinPremiaDialog.onDismissActionClick = {
                adapter.notifyDataSetChanged()
            }

            askSpinPremiaDialog.show(
                parentFragmentManager,
                Constants.TAG_ASK_SPIN_PREMIA_DIALOG
            )
        }
    }

    private fun showDisabledPomp() {
        val disabledPompBottomSheetDialog = DisabledPumpBottomSheetDialog()
        disabledPompBottomSheetDialog.show(
            parentFragmentManager,
            Constants.TAG_DISABLED_PUMP_BOTTOM
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showErrorConnection() {
        viewLifecycleOwner.lifecycleScope.launch {
            showAlert()
            val errorConnectionDialog = ErrorConnectionBottomSheetDialog()

            errorConnectionDialog.onTryAgainActionClick = {
                tryAgain()
            }

            errorConnectionDialog.onCancelActionListener = {
                adapter.notifyDataSetChanged()
            }

            errorConnectionDialog.show(
                parentFragmentManager,
                Constants.TAG_ERROR_CONNECTION_DIALOG
            )
        }
    }

    private suspend fun showAlert() {
        parentActivity?.showProgressDialog()
        delay(3000)
        parentActivity?.dismissProgressDialog()
    }

    private fun tryAgain() {
        showErrorConnection()
    }

    private fun showSpinPremiaDialog(pumpId: Int) {
        val spinPremiaDialog = SpinPremiaBottomSheetDialog()
        spinPremiaDialog.onContinuePhoneActionClick = {
            validateSpinPremia(pumpId)
        }

        spinPremiaDialog.onReadCodeActionClick = { codeSpin ->
            validateSpinPremia(pumpId)
        }

        spinPremiaDialog.onCancelActionClick = {
            goToPaymentMethods(pumpId, false)
        }

        spinPremiaDialog.show(
            parentFragmentManager,
            Constants.TAG_SPIN_PREMIA_BOTTOM_SHEET_DIALOG
        )
    }

    private fun validateSpinPremia(pumpId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            showAlert()
            goToPaymentMethods(pumpId, true)
        }
    }

    private fun goToPaymentMethods(pumpId: Int, hasSpinPremia: Boolean) {
        Bundle().apply {
            putInt(Constants.PUMP_ID_KEY, pumpId)
            putBoolean(Constants.HAS_SPIN_PREMIA, hasSpinPremia)
            findNavController().navigate(R.id.action_payment_methods_fragment, this)
        }
    }

    private fun getDummyInformation(): List<PetrolPumpsList> {
        return listOf(
            PetrolPumpsList(1, true),
            PetrolPumpsList(2, false),
            PetrolPumpsList(3, true),
            PetrolPumpsList(4, true),
            PetrolPumpsList(5, false),
            PetrolPumpsList(6, true),
        )
    }
}