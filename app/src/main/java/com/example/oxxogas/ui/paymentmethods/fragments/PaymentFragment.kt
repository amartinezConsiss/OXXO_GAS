package com.example.oxxogas.ui.paymentmethods.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.oxxogas.R
import com.example.oxxogas.databinding.FragmentPaymentBinding
import com.example.oxxogas.domain.models.ShopInformation
import com.example.oxxogas.ui.home.activities.HomeActivity
import com.example.oxxogas.ui.main.dialogs.SuccessBottomDialog
import com.example.oxxogas.ui.main.fragments.BaseFragment
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.example.oxxogas.ui.newsale.dialogs.SpinPremiaBottomSheetDialog
import com.example.oxxogas.ui.paymentmethods.dialogs.CardPaymentBottomSheetDialog
import com.example.oxxogas.ui.paymentmethods.dialogs.CashPaymentBottomSheetDialog
import com.example.oxxogas.ui.paymentmethods.dialogs.PaymentBottomSheetDialog
import com.example.oxxogas.ui.paymentmethods.viewmodels.PaymentViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

@AndroidEntryPoint
class PaymentFragment : BaseFragment<FragmentPaymentBinding>() {

    private var parentActivity: HomeActivity? = null
    private val viewModel by viewModels<PaymentViewModel>()
    private var totalAmount: BigDecimal = BigDecimal.ZERO
    private var pumpId: Int = 1
    private val shopInformation = ShopInformation()


    override fun initBinding(): FragmentPaymentBinding =
        FragmentPaymentBinding.inflate(layoutInflater)

    override fun initView(view: View, savedState: Bundle?) {
        setUpToolbar()
        initListeners()
        observerViewModel()
        viewModel.loadDummyInfo()
    }

    private fun setUpToolbar() {
        if (arguments?.containsKey(Constants.PUMP_ID_KEY) == true) {
            pumpId = arguments?.getInt(Constants.PUMP_ID_KEY) ?: 1
        }

        parentActivity = activity as? HomeActivity
        parentActivity?.setupToolbar(
            binding.toolbarPayment,
            getString(R.string.pum_id, pumpId.toString()),
            true
        )
    }

    private fun initListeners() {
        binding.spinBtn.setSafeOnClickListener {
            showSpinPremiaDialog()
        }

        binding.cashBtn.setSafeOnClickListener {
            shopInformation.methodPayment = Constants.CASH_PAYMENT_METHOD
            showPaymentDialogMethod(Constants.CASH_PAYMENT_METHOD)
        }

        binding.cardBtn.setSafeOnClickListener {
            shopInformation.methodPayment = Constants.CARD_PAYMENT_METHOD
            showPaymentDialogMethod(Constants.CARD_PAYMENT_METHOD)
        }
    }

    private fun observerViewModel() {
        viewModel.productState.observe(viewLifecycleOwner) { state ->
            shopInformation.pump = pumpId
            shopInformation.typeProduct = state.description
            shopInformation.quantity = state.quantity.toDouble()
            shopInformation.price = state.priceUnit.toDouble()
            shopInformation.amount = state.total.toDouble()
            shopInformation.total = state.total.toDouble()

            totalAmount = state.total
            binding.tvProduct.text = state.description
            binding.tvTotal.text = setCurrencyFormat(state.total)
            binding.tvDescriptionProduct.text = getString(
                R.string.cant_price,
                state.quantity,
                setCurrencyFormat(state.priceUnit)
            )

            binding.tvOutstandingPaymentAmount.text =
                getString(R.string.outstanding_payment_amount, setCurrencyFormat(state.total))
        }
    }

    private fun showSpinPremiaDialog() {
        val spinPremiaDialog = SpinPremiaBottomSheetDialog()
        spinPremiaDialog.onContinuePhoneActionClick = {
            successSpinPremia()
        }

        spinPremiaDialog.onReadCodeActionClick = { codeSpin ->
            successSpinPremia()
        }

        spinPremiaDialog.show(
            parentFragmentManager,
            Constants.TAG_SPIN_PREMIA_BOTTOM_SHEET_DIALOG
        )
    }

    private fun successSpinPremia() {
        lifecycleScope.launch {
            parentActivity?.showProgressBottomDialog()
            delay(2500)
            parentActivity?.dismissProgressBottomDialog()

            val successBottomDialog = SuccessBottomDialog()
            successBottomDialog.show(
                parentFragmentManager,
                Constants.TAG_SPIN_PREMIA_BOTTOM_SHEET_DIALOG
            )
            delay(2500)
            successBottomDialog.dismiss()
            shopInformation.hasSpinPremia = true
            binding.spinBtn.visibility = View.GONE
        }
    }

    private fun showPaymentDialogMethod(paymentMethod: Int) {
        val cashDialog =
            PaymentBottomSheetDialog(totalAmount.toDouble(), paymentMethod)
        cashDialog.onApplyPaymentCallback = {
            viewLifecycleOwner.lifecycleScope.launch {
                if (paymentMethod == Constants.CARD_PAYMENT_METHOD) {
                    showCardDialog()
                } else {
                    parentActivity?.showProgressBottomDialog()
                    delay(2500)
                    parentActivity?.dismissProgressBottomDialog()
                    goToResumeFragment()
                }
            }
        }
        cashDialog.show(parentFragmentManager, Constants.TAG_CASH_BOTTOM_SHEET_DIALOG)
    }

    private fun showCardDialog() {
        val cardDialog =
            CardPaymentBottomSheetDialog(totalAmount.toDouble())
        cardDialog.onPayCardSuccessCallback = {
            cardDialog.dismiss()
            goToResumeFragment()
        }
        cardDialog.show(parentFragmentManager, Constants.TAG_CASH_BOTTOM_SHEET_DIALOG)
    }

    private fun goToResumeFragment() {
        Bundle().apply {
            putString(Constants.SHOP_INFORMATION, getShopInformation())
            findNavController().navigate(
                R.id.action_resume_ticket_fragment,
                this
            )
        }
    }

    private fun getShopInformation(): String =
        Gson().toJson(
            shopInformation
        )
}