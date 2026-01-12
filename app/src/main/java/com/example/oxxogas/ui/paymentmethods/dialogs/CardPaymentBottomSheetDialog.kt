package com.example.oxxogas.ui.paymentmethods.dialogs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.example.oxxogas.R
import com.example.oxxogas.databinding.BottomDialogCardPaymentBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CardPaymentBottomSheetDialog(private val isSuccess: Boolean) :
    BaseBottomSheetDialog<BottomDialogCardPaymentBinding>() {

    var onPayCardSuccessCallback: (() -> Unit)? = null

    override fun initBinding(): BottomDialogCardPaymentBinding =
        BottomDialogCardPaymentBinding.inflate(layoutInflater)

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
        binding.btnCancelCardPayment.setSafeOnClickListener {
            this.dismiss()
        }

        binding.ivPayCard.setSafeOnClickListener {
            it.isEnabled = false
            if (isSuccess) {
                customResultPaymentView()
                binding.ivPayCard.setImageResource(R.drawable.ic_check_circle)
                binding.tvWaitingData.text = getString(R.string.transaction_completed)
                onPayCardSuccessCallback?.invoke()
                binding.btnCancelCardPayment.isEnabled = false
                binding.btnCancelCardPayment.setBackgroundResource(R.drawable.gray_btn)
            } else {
                customResultPaymentView()
                binding.ivPayCard.setImageResource(R.drawable.ic_close_circle)
                binding.tvWaitingData.text = getString(R.string.transaction_rejected)
                binding.btnCancelCardPayment.text = getString(R.string.choose_payment_method)
            }
        }
    }

    private fun customResultPaymentView() {
        val paramsIvTop = binding.ivPayCard.layoutParams as ViewGroup.MarginLayoutParams
        val paramsBtnTop = binding.btnCancelCardPayment.layoutParams as ViewGroup.MarginLayoutParams
        paramsIvTop.topMargin = resources.getDimensionPixelSize(R.dimen.margin_100)
        paramsBtnTop.topMargin = resources.getDimensionPixelSize(R.dimen.margin_100)

        binding.ivPayCard.layoutParams = paramsIvTop
        binding.btnCancelCardPayment.layoutParams = paramsBtnTop

        binding.ivActionPayCard.visibility = View.GONE
    }

}