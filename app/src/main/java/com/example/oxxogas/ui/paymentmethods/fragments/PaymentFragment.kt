package com.example.oxxogas.ui.paymentmethods.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.oxxogas.R
import com.example.oxxogas.databinding.FragmentPaymentBinding
import com.example.oxxogas.domain.models.ShopInformation
import com.example.oxxogas.ui.home.activities.HomeActivity
import com.example.oxxogas.ui.main.fragments.BaseFragment
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.example.oxxogas.ui.newsale.dialogs.SpinPremiaBottomSheetDialog
import com.example.oxxogas.ui.paymentmethods.dialogs.CardPaymentBottomSheetDialog
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
    private var isSuccessCard = false

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
            showPaymentDialogMethod()
            setBlueBtn(binding.cashBtn, binding.tvCash, binding.ivIconCash)
            setWhiteBtn(binding.cardBtn, binding.tvCard, binding.ivIconCard)
            setWhiteBtn(binding.mixedBtn, binding.tvMixed, binding.ivIconMixed)
        }

        binding.cardBtn.setSafeOnClickListener {
            shopInformation.methodPayment = Constants.CARD_PAYMENT_METHOD
            showCardDialog()
            setWhiteBtn(binding.cashBtn, binding.tvCash, binding.ivIconCash)
            setBlueBtn(binding.cardBtn, binding.tvCard, binding.ivIconCard)
            setWhiteBtn(binding.mixedBtn, binding.tvMixed, binding.ivIconMixed)
        }

        binding.mixedBtn.setSafeOnClickListener {
            shopInformation.methodPayment = Constants.MIXED_PAYMENT_METHOD
            goToMixedFragment()
            setWhiteBtn(binding.cashBtn, binding.tvCash, binding.ivIconCash)
            setWhiteBtn(binding.cardBtn, binding.tvCard, binding.ivIconCard)
            setBlueBtn(binding.mixedBtn, binding.tvMixed, binding.ivIconMixed)
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
            parentActivity?.showProgressBottomDialog(
                R.drawable.ic_card_bulleted_settings,
                R.string.spin_premia2,
            )

            delay(2500)
            parentActivity?.dismissProgressBottomDialog()

            parentActivity?.showSuccessBottomDialog(
                R.drawable.ic_card_bulleted_settings,
                R.string.spin_premia2,
                R.string.spin_message_success
            )
            delay(2500)
            parentActivity?.dismissSucessBottomDialog()
            shopInformation.hasSpinPremia = true
            binding.spinBtn.visibility = View.GONE
        }
    }

    private fun showPaymentDialogMethod() {
        val cashDialog =
            PaymentBottomSheetDialog(totalAmount.toDouble(), Constants.CASH_PAYMENT_METHOD)
        cashDialog.onApplyPaymentCallback = {
            viewLifecycleOwner.lifecycleScope.launch {
                parentActivity?.showProgressBottomDialog(
                    R.drawable.icon_cash,
                    R.string.cash_payment
                )
                delay(2500)
                parentActivity?.dismissProgressBottomDialog()
                goToResumeFragment()
            }
        }
        cashDialog.show(parentFragmentManager, Constants.TAG_CASH_BOTTOM_SHEET_DIALOG)
    }

    private fun showCardDialog() {
        val cardDialog =
            CardPaymentBottomSheetDialog(totalAmount.toDouble(), isSuccessCard)
        cardDialog.onPayCardSuccessCallback = {
            cardDialog.dismiss()
            goToResumeFragment()
        }
        cardDialog.onPayCardFailCallback = {

        }
        cardDialog.show(parentFragmentManager, Constants.TAG_CASH_BOTTOM_SHEET_DIALOG)
        isSuccessCard = true
    }

    private fun setBlueBtn(
        viewConstraint: ConstraintLayout,
        textView: TextView,
        iconView: ImageView
    ) {
        viewConstraint.setBackgroundResource(R.drawable.blue_box)
        textView.setTextColor(requireContext().getColor(R.color.white))
        iconView.imageTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.white)
    }

    private fun setWhiteBtn(
        viewConstraint: ConstraintLayout,
        textView: TextView,
        iconView: ImageView
    ) {
        viewConstraint.setBackgroundResource(R.drawable.box_gray_payment)
        textView.setTextColor(requireContext().getColor(R.color.black))
        iconView.imageTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.black)

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

    private fun goToMixedFragment() {
        Bundle().apply {
            putString(Constants.SHOP_INFORMATION, getShopInformation())
            findNavController().navigate(
                R.id.action_mixed_payment_fragment,
                this
            )
        }
    }

    private fun getShopInformation(): String =
        Gson().toJson(
            shopInformation
        )
}