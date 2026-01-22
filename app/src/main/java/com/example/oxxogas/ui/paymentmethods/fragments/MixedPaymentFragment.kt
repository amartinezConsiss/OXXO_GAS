package com.example.oxxogas.ui.paymentmethods.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.oxxogas.R
import com.example.oxxogas.databinding.FragmentMixedPaymentBinding
import com.example.oxxogas.domain.models.CardInformation
import com.example.oxxogas.domain.models.PaymentsMadeList
import com.example.oxxogas.domain.models.ShopInformation
import com.example.oxxogas.ui.home.activities.HomeActivity
import com.example.oxxogas.ui.main.fragments.BaseFragment
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.main.utils.afterTextChanged
import com.example.oxxogas.ui.main.utils.applyCurrencyFormat
import com.example.oxxogas.ui.main.utils.cleanCurrencyDoubleFormat
import com.example.oxxogas.ui.main.utils.cleanCurrencyFormat
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.example.oxxogas.ui.paymentmethods.adapters.PaymentsMadeAdapter
import com.example.oxxogas.ui.paymentmethods.dialogs.CardPaymentBottomSheetDialog
import com.example.oxxogas.ui.resumeticket.utils.DummyData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MixedPaymentFragment : BaseFragment<FragmentMixedPaymentBinding>() {

    private lateinit var backCallback: OnBackPressedCallback
    private lateinit var shopInformationResponse: ShopInformation
    private val adapter by lazy { PaymentsMadeAdapter() }
    private var parentActivity: HomeActivity? = null
    private var subtotal = BigDecimal.ZERO
    private var totalCards = 1
    private var totalCash = 1
    private var paymentsMade = mutableListOf<PaymentsMadeList>()
    private var isFirstPayment = true
    private var paymentSelected = Constants.CARD_PAYMENT_METHOD

    override fun initBinding(): FragmentMixedPaymentBinding =
        FragmentMixedPaymentBinding.inflate(layoutInflater)

    override fun initView(view: View, savedState: Bundle?) {
        getShopInformation()
        initView()
        initCardFlow()
    }

    private fun initView() {
        subtotal = shopInformationResponse.total?.toBigDecimal()
        binding.tvTotal.text = setCurrencyFormat(shopInformationResponse.total ?: 0.0)
        binding.tvOutstandingAmount.text = setCurrencyFormat(subtotal)
        binding.rvPaymentMade.adapter = adapter
        binding.rvPaymentMade.isNestedScrollingEnabled = false

        binding.btnCard.setSafeOnClickListener {
            setBlueBtn(binding.btnCard, binding.tvCard, binding.ivIconCard)
            setWhiteBtn(binding.btnCash, binding.tvCash, binding.ivIconCash)
            paymentSelected = Constants.CARD_PAYMENT_METHOD
            binding.tvExchange.visibility = View.GONE
            enabledButtonContinue(
                cleanCurrencyDoubleFormat(binding.etEnterAmount.text.toString()) in 1.00..subtotal.toDouble()
            )
        }

        binding.btnCash.setSafeOnClickListener {
            setWhiteBtn(binding.btnCard, binding.tvCard, binding.ivIconCard)
            setBlueBtn(binding.btnCash, binding.tvCash, binding.ivIconCash)
            paymentSelected = Constants.CASH_PAYMENT_METHOD
            binding.tvExchange.visibility = View.VISIBLE
            validateExchangeAndPendingAmount(binding.etEnterAmount.text.toString())
            enabledButtonContinue(
                cleanCurrencyDoubleFormat(binding.etEnterAmount.text.toString()) > subtotal.toDouble()
            )
        }
    }

    private fun initCardFlow() {
        binding.etEnterAmount.setText(setCurrencyFormat(subtotal))
        binding.etEnterAmount.applyCurrencyFormat()

        binding.etEnterAmount.afterTextChanged { cash ->
            val amountCash = cleanCurrencyDoubleFormat(cash)
            if (paymentSelected == Constants.CASH_PAYMENT_METHOD) {
                validateExchangeAndPendingAmount(cash)
                enabledButtonContinue(amountCash > subtotal.toDouble())
            } else {
                enabledButtonContinue(amountCash in 1.00..subtotal.toDouble())
            }
        }

        binding.btnProccessCard.setSafeOnClickListener {
            if (paymentSelected == Constants.CARD_PAYMENT_METHOD) {
                showCardProcess()
            } else {
                showCashProcess()
            }
        }
    }

    private fun showCardProcess() {
        val cardDialog =
            CardPaymentBottomSheetDialog(cleanCurrencyDoubleFormat(binding.etEnterAmount.text.toString()))
        cardDialog.onPayCardSuccessCallback = {
            cardDialog.dismiss()
            disabledToolbar()
            addPaymentMade()
            totalCards++
            updateOutstandingAmount(cleanCurrencyFormat(binding.etEnterAmount.text.toString()))
            if (isFirstPayment) {
                binding.btnPaymentsContainer.visibility = View.VISIBLE
                binding.tvPaymentLabel.text = "Siguiente pago a realizar"
                isFirstPayment = false
            }
        }
        cardDialog.onPayCardFailCallback = {

        }
        cardDialog.show(parentFragmentManager, Constants.TAG_CASH_BOTTOM_SHEET_DIALOG)
    }

    private fun showCashProcess() {
        viewLifecycleOwner.lifecycleScope.launch {
            parentActivity?.showProgressBottomDialog(
                R.drawable.icon_cash,
                R.string.cash_payment
            )
            delay(2500)
            parentActivity?.dismissProgressBottomDialog()
            addPaymentMade()
            updateOutstandingAmount(cleanCurrencyFormat(binding.etEnterAmount.text.toString()))
            totalCash += 1
        }
    }

    private fun addPaymentMade() {
        val totalPaymentMethodsUsed = if (paymentSelected == Constants.CASH_PAYMENT_METHOD) {
            totalCash
        } else {
            totalCards
        }
        paymentsMade.add(
            PaymentsMadeList(
                paymentSelected,
                cleanCurrencyFormat(binding.etEnterAmount.text.toString()),
                totalPaymentMethodsUsed
            )
        )
        adapter.setPaymentMade(paymentsMade)
    }

    private fun enabledButtonContinue(validAmount: Boolean) {
        if (validAmount) {
            binding.btnProccessCard.isEnabled = true
            binding.btnProccessCard.setBackgroundResource(R.drawable.dark_green_btn)
        } else {
            binding.btnProccessCard.isEnabled = true
            binding.btnProccessCard.setBackgroundResource(R.drawable.gray_btn)
        }
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

    private fun updateOutstandingAmount(amountPayment: BigDecimal) {
        if (amountPayment > subtotal) {
            binding.tvOutstandingAmount.text = setCurrencyFormat(0.0)
            setMixedInformation()
            goToResumeFragment()
        } else {
            subtotal -= amountPayment
            binding.tvOutstandingAmount.text = setCurrencyFormat(subtotal)
            binding.etEnterAmount.setText(setCurrencyFormat(subtotal))
            if (subtotal == BigDecimal.ZERO) {
                setMixedInformation()
                goToResumeFragment()
            }
        }

    }

    private fun disabledToolbar() {
        parentActivity = activity as? HomeActivity

        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Bloqueado
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backCallback)
    }

    private fun setMixedInformation() {
        val cardsUsed: MutableList<CardInformation> = mutableListOf()
        var amountTotalCard = 0.0
        paymentsMade.forEach { paymentMade ->
            if (paymentMade.typeMethod == Constants.CARD_PAYMENT_METHOD) {
                cardsUsed.add(
                    DummyData().getCardInformation(amountCard = paymentMade.amount.toDouble())
                )
                amountTotalCard += paymentMade.amount.toDouble()
            }
        }

        shopInformationResponse.cardsMixedInformation = cardsUsed
        shopInformationResponse.cashAmount = shopInformationResponse.total?.minus(amountTotalCard)
    }

    private fun getShopInformation() {
        if (arguments?.containsKey(Constants.SHOP_INFORMATION) == true) {
            val shopInformation = arguments?.getString(Constants.SHOP_INFORMATION)

            val gson = Gson()
            try {
                shopInformationResponse =
                    gson.fromJson(shopInformation, ShopInformation::class.java)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }
        }
    }

    private fun validateExchangeAndPendingAmount(amount: String) {
        val amountCash = cleanCurrencyDoubleFormat(amount)
        if (amountCash > subtotal.toDouble()) {
            val exchange = amountCash - subtotal.toDouble()
            binding.tvExchange.text =
                getString(R.string.exchange_total_title, setCurrencyFormat(exchange))
        } else {
            binding.tvExchange.text =
                getString(R.string.exchange_total_title, getString(R.string.zero_amount))
        }
    }

    private fun goToResumeFragment() {
        Bundle().apply {
            putString(Constants.SHOP_INFORMATION, getShopInformationGson())
            findNavController().navigate(
                R.id.action_mixed_resume_ticket_fragment,
                this
            )
        }
    }

    private fun getShopInformationGson(): String =
        Gson().toJson(
            shopInformationResponse
        )
}