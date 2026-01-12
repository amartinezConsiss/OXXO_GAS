package com.example.oxxogas.ui.paymentmethods.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.oxxogas.R
import com.example.oxxogas.domain.models.CardInformation
import com.example.oxxogas.domain.models.ShopInformation
import com.example.oxxogas.databinding.FragmentMixedPaymentMethodBinding
import com.example.oxxogas.ui.home.activities.HomeActivity
import com.example.oxxogas.ui.main.fragments.BaseFragment
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.main.utils.safeNavigate
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.example.oxxogas.ui.paymentmethods.dialogs.CardPaymentBottomSheetDialog
import com.example.oxxogas.ui.paymentmethods.dialogs.MixedCardPaymentBottomSheetDialog
import com.example.oxxogas.ui.paymentmethods.dialogs.MixedCashPaymentBottomSheetDialog
import com.example.oxxogas.ui.resumeticket.utils.DummyData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MixedPaymentMethodFragment : BaseFragment<FragmentMixedPaymentMethodBinding>() {

    private var parentActivity: HomeActivity? = null
    private var totalAmount = BigDecimal.ZERO
    private var amountTotalCash = BigDecimal.ZERO
    private var amountTotalCard = BigDecimal.ZERO
    private var subTotal = BigDecimal.ZERO
    private lateinit var shopInformationResponse: ShopInformation
    private var cardsUsed: MutableList<CardInformation> = mutableListOf()

    override fun initBinding(): FragmentMixedPaymentMethodBinding =
        FragmentMixedPaymentMethodBinding.inflate(layoutInflater)

    override fun initView(view: View, savedState: Bundle?) {
        loadInformation()
        loadListeners()
        parentActivity?.setupToolbar(binding.toolbar, "", true)
    }

    private fun loadInformation() {
        if (arguments?.containsKey(Constants.SHOP_INFORMATION) != null) {
            val shopInformation = arguments?.getString(Constants.SHOP_INFORMATION)

            val gson = Gson()
            try {
                shopInformationResponse =
                    gson.fromJson(shopInformation, ShopInformation::class.java)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }

            totalAmount = shopInformationResponse.total?.toBigDecimal() ?: BigDecimal.ZERO
            subTotal = totalAmount
            binding.tvTotal.text = setCurrencyFormat(totalAmount)
            binding.tvOutstandingPayment.text = setCurrencyFormat(totalAmount)
        }
    }


    private fun loadListeners() {
        binding.cashContainer.setSafeOnClickListener {
            changeSelectedBackground(binding.cashContainer, binding.tvLabelCash, binding.icCash)
            cleanSelectedBackground(binding.cardContainer, binding.tvLabelCard, binding.icCard)
            showCashPayment()
        }

        binding.cardContainer.setSafeOnClickListener {
            changeSelectedBackground(binding.cardContainer, binding.tvLabelCard, binding.icCard)
            cleanSelectedBackground(binding.cashContainer, binding.tvLabelCash, binding.icCash)
            showMixedCardPayment()
        }
    }

    private fun showCashPayment() {
        val mixedCashDialog = MixedCashPaymentBottomSheetDialog(subTotal)
        mixedCashDialog.onApplyCashCallback = { amountCash ->
            updateTotalAmount(amountCash, Constants.CASH_PAYMENT_METHOD)
        }
        mixedCashDialog.show(
            parentFragmentManager,
            Constants.TAG_MIXED_CASH_BOTTOM_SHEET_DIALOG
        )
    }

    private fun showMixedCardPayment() {
        val mixedCardDialog = MixedCardPaymentBottomSheetDialog(subTotal)
        mixedCardDialog.onApplyCardCallback = { amountCard ->
            showCardPayment(amountCard)
        }

        mixedCardDialog.show(parentFragmentManager, Constants.TAG_MIXED_CARD_BOTTOM_SHEET_DIALOG)
    }

    private fun showCardPayment(amountCard: BigDecimal) {
        val cardPaymentBottomSheetDialog = CardPaymentBottomSheetDialog(true)
        cardPaymentBottomSheetDialog.onPayCardSuccessCallback = {
            lifecycleScope.launch {
                delay(2000)
                cardPaymentBottomSheetDialog.dismiss()
                updateTotalAmount(amountCard, Constants.CARD_PAYMENT_METHOD)
                cardsUsed.add(
                    DummyData().getCardInformation(amountCard = amountCard.toDouble())
                )
            }
        }
        cardPaymentBottomSheetDialog.show(
            parentFragmentManager,
            Constants.TAG_CARD_PAYMENT_BOTTOM_SHEET_DIALOG
        )
    }

    private fun updateTotalAmount(amountPayed: BigDecimal, methodPay: Int) {
        subTotal -= amountPayed

        if (methodPay == Constants.CASH_PAYMENT_METHOD) {
            amountTotalCash += amountPayed
            binding.tvCashMethod.text =
                getString(R.string.negative_number, setCurrencyFormat(amountTotalCash))
        } else {
            amountTotalCard += amountPayed
            binding.tvCardMethod.text =
                getString(R.string.negative_number, setCurrencyFormat(amountTotalCard))
        }
        binding.tvOutstandingPayment.text = setCurrencyFormat(subTotal)

        if (subTotal <= BigDecimal.ZERO) {
            lifecycleScope.launch {
                binding.containerPaymentProcess.visibility = View.VISIBLE
                binding.btnCancel.setBackgroundResource(R.drawable.gray_btn)
                delay(3000)
                val bundle = Bundle().apply {
                    shopInformationResponse.cardsMixedInformation = cardsUsed
                    shopInformationResponse.cashAmount = amountTotalCash.toDouble()
                    shopInformationResponse.totalCardMixedAmount = amountTotalCard.toDouble()
                    val shopMixedInformation = Gson().toJson(shopInformationResponse)
                    putString(Constants.SHOP_INFORMATION, shopMixedInformation)
                }
                if (isAdded && !isDetached) {
                findNavController().safeNavigate(R.id.action_mixed_resume_ticket_fragment, bundle)
                }
            }
        }
    }

    private fun changeSelectedBackground(
        container: ConstraintLayout,
        textView: TextView,
        imageView: ImageView
    ) {
        container.setBackgroundResource(R.drawable.box_dark_blue)
        textView.setTextColor(
            requireContext().getColor(
                R.color.white
            )
        )
        imageView.imageTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.white)
    }

    private fun cleanSelectedBackground(
        container: ConstraintLayout,
        textView: TextView,
        imageView: ImageView
    ) {
        container.setBackgroundResource(R.drawable.border_dark_blue)
        textView.setTextColor(
            requireContext().getColor(
                R.color.dark_blue
            )
        )
        imageView.imageTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.dark_blue)
    }
}