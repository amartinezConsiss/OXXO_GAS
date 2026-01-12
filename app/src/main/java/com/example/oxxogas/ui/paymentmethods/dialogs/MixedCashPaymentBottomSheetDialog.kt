package com.example.oxxogas.ui.paymentmethods.dialogs

import android.os.Bundle
import android.view.View
import com.example.oxxogas.R
import com.example.oxxogas.databinding.BottomDialogMixedCashPaymentBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.afterTextChanged
import com.example.oxxogas.ui.main.utils.applyCurrencyFormat
import com.example.oxxogas.ui.main.utils.roundUp
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import java.math.BigDecimal

class MixedCashPaymentBottomSheetDialog(private val totalAmount: BigDecimal) :
    BaseBottomSheetDialog<BottomDialogMixedCashPaymentBinding>() {

    private var amountCash = BigDecimal.ZERO
    var onApplyCashCallback: ((BigDecimal) -> Unit)? = null

    override fun initBinding(): BottomDialogMixedCashPaymentBinding =
        BottomDialogMixedCashPaymentBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {
        loadInitialInfo()
        validateExchangeAndPendingAmount()
    }

    private fun loadInitialInfo() {
        binding.tvCashAmount.text = setCurrencyFormat(totalAmount)
        binding.etEnterCash.applyCurrencyFormat()
        binding.etEnterCashReceipt.applyCurrencyFormat()
        binding.tvErrorAmount.text =
            getString(R.string.amount_receipt_error, setCurrencyFormat(totalAmount))

        binding.btnApplyCash.setSafeOnClickListener {
            onApplyCashCallback?.invoke(amountCash)
            dismiss()
        }
    }

    private fun validateExchangeAndPendingAmount() {
        var amountCashReceipt = BigDecimal.ZERO

        binding.etEnterCashReceipt.afterTextChanged { cash ->
            val clean = cash.replace("[^\\d.]".toRegex(), "")
            amountCashReceipt = if (cash.isEmpty()) BigDecimal.ZERO else clean.toBigDecimal()
            enabledButtonContinue(isAmountValid(amountCashReceipt))
            binding.etEnterCash.setText(cash)
        }

        binding.etEnterCash.afterTextChanged { cash ->
            val amountCashClean = cash.replace("[^\\d.]".toRegex(), "")
            amountCash = if (cash.isEmpty()) BigDecimal.ZERO else amountCashClean.toBigDecimal()
            enabledButtonContinue(isAmountValid(amountCashReceipt))

            if (amountCash > totalAmount) {
                binding.tvErrorAmount.visibility = View.VISIBLE
            } else {
                binding.tvErrorAmount.visibility = View.GONE
            }

            if (amountCash < amountCashReceipt) {
                val exchange = amountCashReceipt - amountCash
                binding.tvExchange.text = setCurrencyFormat(exchange)
            } else {
                binding.tvExchange.text = getString(R.string.zero_amount)
            }
        }
    }

    private fun isAmountValid(amountCashReceipt: BigDecimal): Boolean =
        amountCash >= BigDecimal(1.0) && amountCashReceipt > BigDecimal.ZERO && amountCash <= totalAmount

    private fun enabledButtonContinue(validAmount: Boolean) {
        if (validAmount) {
            binding.btnApplyCash.isEnabled = true
            binding.btnApplyCash.setBackgroundResource(R.drawable.dark_green_btn)
        } else {
            binding.btnApplyCash.isEnabled = true
            binding.btnApplyCash.setBackgroundResource(R.drawable.gray_btn)
        }
    }
}