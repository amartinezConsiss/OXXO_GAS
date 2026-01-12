package com.example.oxxogas.ui.newsale.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.example.oxxogas.databinding.BottomDialogAskSpinBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener

class AskSpinBottomSheetDialog : BaseBottomSheetDialog<BottomDialogAskSpinBinding>() {

    override fun initBinding(): BottomDialogAskSpinBinding =
        BottomDialogAskSpinBinding.inflate(layoutInflater)

    var onPositiveActionClick: (() -> Unit)? = null
    var onNegativeActionClick: (() -> Unit)? = null
    var onDismissActionClick: (() -> Unit)? = null

    override fun initView(view: View, saveState: Bundle?) {

        binding.btnNo.setSafeOnClickListener {
            onNegativeActionClick?.invoke()
            dismiss()
        }
        binding.btnYes.setSafeOnClickListener {
            onPositiveActionClick?.invoke()
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissActionClick?.invoke()
    }

}