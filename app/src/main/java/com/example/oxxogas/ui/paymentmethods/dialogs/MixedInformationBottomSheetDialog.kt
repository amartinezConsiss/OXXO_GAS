package com.example.oxxogas.ui.paymentmethods.dialogs

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.oxxogas.R
import com.example.oxxogas.databinding.BottomDialogMixedInformationBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener

class MixedInformationBottomSheetDialog :
    BaseBottomSheetDialog<BottomDialogMixedInformationBinding>() {

    var onContinueMixedCallback: (() -> Unit)? = null
    override fun initBinding(): BottomDialogMixedInformationBinding =
        BottomDialogMixedInformationBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {
        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_1_2_pay)
            .into(binding.ivPaymentGif)
        binding.btnContinue.setSafeOnClickListener {
            dismiss()
            onContinueMixedCallback?.invoke()
        }
        dialog?.setOnDismissListener {
            onContinueMixedCallback?.invoke()
        }
    }
}