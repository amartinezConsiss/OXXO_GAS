package com.example.oxxogas.ui.paymentmethods.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.oxxogas.R
import com.example.oxxogas.databinding.BottomDialogPaymentBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.main.utils.afterTextChanged
import com.example.oxxogas.ui.main.utils.applyCurrencyFormat
import com.example.oxxogas.ui.main.utils.cleanCurrencyFormat
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.math.BigDecimal

class PaymentBottomSheetDialog(private val totalAmount: Double, private val methodType: Int) :
    BaseBottomSheetDialog<BottomDialogPaymentBinding>() {

    var onApplyPaymentCallback: ((BigDecimal) -> Unit)? = null

    override fun initBinding(): BottomDialogPaymentBinding =
        BottomDialogPaymentBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
        val bottomSheet =
            dialog?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            ) ?: return

        BottomSheetBehavior.from(bottomSheet).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = true
        }
    }

    override fun initView(view: View, saveState: Bundle?) {
        loadInformation()
    }

    private fun loadInformation() {
        if (methodType == Constants.CARD_PAYMENT_METHOD) {
            binding.tvExchange.visibility = View.GONE
            binding.tvLabelExchange.visibility = View.GONE

            binding.titlePayment.text = getString(R.string.pay_with_card)
            binding.ivPayment.setImageResource(R.drawable.icon_credit_card)
            binding.ivPayment.imageTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.black)

        }

        binding.tvLabelAmount.text = setCurrencyFormat(totalAmount)
        binding.etEnterAmount.setText(setCurrencyFormat(totalAmount))
        binding.etEnterAmount.applyCurrencyFormat()
        binding.btnApplyAmount.setSafeOnClickListener {
            onApplyPaymentCallback?.invoke(cleanCurrencyFormat(binding.etEnterAmount.text.toString()))
            dismiss()
        }
        binding.btnCancelPayment.setSafeOnClickListener {
            dismiss()
        }
        validateExchangeAndPendingAmount()
    }

    private fun validateExchangeAndPendingAmount() {
        binding.etEnterAmount.afterTextChanged { cash ->
            val clean = cash.replace("[^\\d.]".toRegex(), "")
            val amountCash = if (cash.isEmpty()) 0.0 else clean.toDouble()

            if (methodType == Constants.MIXED_PAYMENT_METHOD) {
                mixedValidation(amountCash)
            } else {
                cashValidation(amountCash)
            }

        }
    }

    private fun cashValidation(amountCash: Double) {
        enabledButtonContinue(amountCash >= totalAmount)
        if (amountCash > totalAmount) {
            val exchange = amountCash - totalAmount
            binding.tvExchange.text = setCurrencyFormat(exchange)
            binding.tvErrorAmount.visibility = View.GONE
        } else {
            binding.tvExchange.text = getString(R.string.zero_amount)
        }
    }

    private fun mixedValidation(amountCash: Double) {
        if (methodType == Constants.CASH_PAYMENT_METHOD) {
            enabledButtonContinue(amountCash >= 1.00)
            if (amountCash > totalAmount) {
                val exchange = amountCash - totalAmount
                binding.tvExchange.text = setCurrencyFormat(exchange)
                binding.tvErrorAmount.visibility = View.GONE
            } else {
                binding.tvExchange.text = getString(R.string.zero_amount)
            }
        } else {
            enabledButtonContinue(amountCash in 1.00..totalAmount)
            if (amountCash > totalAmount) {
                binding.tvErrorAmount.visibility = View.VISIBLE
            } else {
                binding.tvErrorAmount.visibility = View.GONE
            }
        }

    }

    private fun enabledButtonContinue(validAmount: Boolean) {
        if (validAmount) {
            binding.btnApplyAmount.isEnabled = true
            binding.btnApplyAmount.setBackgroundResource(R.drawable.dark_green_btn)
        } else {
            binding.btnApplyAmount.isEnabled = false
            binding.btnApplyAmount.setBackgroundResource(R.drawable.gray_btn)
        }
    }
}