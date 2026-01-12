package com.example.oxxogas.ui.newsale.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.example.oxxogas.databinding.BottomDialogErrorConnectionBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener

class ErrorConnectionBottomSheetDialog :
    BaseBottomSheetDialog<BottomDialogErrorConnectionBinding>() {

    var onCancelActionListener: (() -> Unit)? = null
    var onTryAgainActionClick: (() -> Unit)? = null

    override fun initBinding(): BottomDialogErrorConnectionBinding =
        BottomDialogErrorConnectionBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {
        binding.btnCancel.setSafeOnClickListener {
            onCancelActionListener?.invoke()
            dismiss()
        }

        binding.btnTryAgain.setSafeOnClickListener {
            onTryAgainActionClick?.invoke()
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onCancelActionListener?.invoke()
    }
}