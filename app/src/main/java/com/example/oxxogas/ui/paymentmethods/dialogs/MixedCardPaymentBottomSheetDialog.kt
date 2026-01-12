package com.example.oxxogas.ui.paymentmethods.dialogs

import android.os.Bundle
import android.view.View
import com.example.oxxogas.R
import com.example.oxxogas.databinding.BottomDialogMixedCardPaymentBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.afterTextChanged
import com.example.oxxogas.ui.main.utils.applyCurrencyFormat
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal

class MixedCardPaymentBottomSheetDialog(private val totalAmount: BigDecimal) :
    BaseBottomSheetDialog<BottomDialogMixedCardPaymentBinding>() {

    private var amountCard = BigDecimal.ZERO
    var onApplyCardCallback: ((BigDecimal) -> Unit)? = null

    override fun initBinding(): BottomDialogMixedCardPaymentBinding =
        BottomDialogMixedCardPaymentBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {
        loadListeners()
        loadInformation()
    }

    private fun loadInformation() {
        binding.tvTotalAmount.text = setCurrencyFormat(totalAmount)
        binding.etEnterCardReceipt.applyCurrencyFormat()
        binding.tvErrorAmount.text =
            getString(R.string.amount_receipt_error, setCurrencyFormat(totalAmount))
    }

    private fun loadListeners() {
        binding.etEnterCardReceipt.afterTextChanged { card ->
            val amountCashClean = card.replace("[^\\d.]".toRegex(), "")
            amountCard = if (card.isEmpty()) BigDecimal.ZERO else amountCashClean.toBigDecimal()
            if (amountCard > totalAmount) {
                binding.tvErrorAmount.visibility = View.VISIBLE
            } else {
                binding.tvErrorAmount.visibility = View.GONE
            }

            enabledButtonContinue(amountCard > BigDecimal.ZERO && amountCard <= totalAmount)
        }

        binding.btnApplyCard.setSafeOnClickListener {
            onApplyCardCallback?.invoke(amountCard)
            dismiss()
        }

    }

    private fun enabledButtonContinue(validAmount: Boolean) {
        if (validAmount) {
            binding.btnApplyCard.isEnabled = true
            binding.btnApplyCard.setBackgroundResource(R.drawable.dark_green_btn)
        } else {
            binding.btnApplyCard.isEnabled = true
            binding.btnApplyCard.setBackgroundResource(R.drawable.gray_btn)
        }
    }
}