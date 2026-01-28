package com.example.oxxogas.ui.paymentmethods.dialogs

import android.os.Bundle
import android.view.View
import com.example.oxxogas.R
import com.example.oxxogas.databinding.BottomDialogCashPaymentBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.afterTextChanged
import com.example.oxxogas.ui.main.utils.applyCurrencyFormat
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CashPaymentBottomSheetDialog(private val totalAmount: Double) :
    BaseBottomSheetDialog<BottomDialogCashPaymentBinding>() {

    var onApplyCashPaymentCallback: (() -> Unit)? = null

    override fun initBinding(): BottomDialogCashPaymentBinding =
        BottomDialogCashPaymentBinding.inflate(layoutInflater)

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
        binding.tvLabelCashAmount.text = setCurrencyFormat(totalAmount)
        binding.etEnterCash.applyCurrencyFormat()
        binding.btnApplyCash.setSafeOnClickListener {
            onApplyCashPaymentCallback?.invoke()
            dismiss()
        }
        validateExchangeAndPendingAmount()
    }

    private fun validateExchangeAndPendingAmount() {
        binding.etEnterCash.afterTextChanged { cash ->
            val clean = cash.replace("[^\\d.]".toRegex(), "")
            val amountCash = if (cash.isEmpty()) 0.0 else clean.toDouble()
            enabledButtonContinue(amountCash >= totalAmount)

            if (amountCash > totalAmount) {
                val exchange = amountCash - totalAmount
                binding.tvExchange.text = setCurrencyFormat(exchange)
            } else {
                binding.tvExchange.text = getString(R.string.zero_amount)
            }
        }
    }

    private fun enabledButtonContinue(validAmount: Boolean) {
        if (validAmount) {
            binding.btnApplyCash.isEnabled = true
            binding.btnApplyCash.setBackgroundResource(R.drawable.dark_green_btn)
        } else {
            binding.btnApplyCash.isEnabled = false
            binding.btnApplyCash.setBackgroundResource(R.drawable.gray_btn)
        }
    }
}