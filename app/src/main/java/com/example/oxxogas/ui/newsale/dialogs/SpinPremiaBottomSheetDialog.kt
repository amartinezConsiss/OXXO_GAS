package com.example.oxxogas.ui.newsale.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import com.example.oxxogas.R
import com.example.oxxogas.databinding.DialogSpinPremiaBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.afterTextChanged
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog

class SpinPremiaBottomSheetDialog : BaseBottomSheetDialog<DialogSpinPremiaBinding>() {

    override fun initBinding(): DialogSpinPremiaBinding =
        DialogSpinPremiaBinding.inflate(layoutInflater)

    var onContinuePhoneActionClick: (() -> Unit)? = null
    var onReadCodeActionClick: ((String) -> Unit)? = null
    var onCancelActionClick: (() -> Unit)? = null
    var onDismissActionClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            )
        }
    }

    override fun initView(view: View, saveState: Bundle?) {

        binding.hiddenScannerInput.requestFocus()
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )

        binding.etEnterPhone.afterTextChanged { phone ->
            statusBtnContinue(phone.length == 10)
        }

        binding.readCodeBtn.setSafeOnClickListener {
            binding.hiddenScannerInput.requestFocus()
            startScan()
        }

        binding.btnContinue.setSafeOnClickListener {
            onContinuePhoneActionClick?.invoke()
            dismiss()
        }

        binding.cancelButton.setSafeOnClickListener {
            onCancelActionClick?.invoke()
            dismiss()
        }

        binding.hiddenScannerInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val code = s?.toString().orEmpty()
                if (code.isNotEmpty()) {
                    onReadCodeActionClick?.invoke(code)
                    dismiss()
                    binding.hiddenScannerInput.text?.clear()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun startScan() {
        val intent = Intent("com.symbol.datawedge.api.ACTION").apply {
            putExtra(
                "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER",
                "START_SCANNING"
            )
        }
        requireContext().sendBroadcast(intent)
    }


    private fun statusBtnContinue(validPhone: Boolean) {
        if (validPhone) {
            binding.btnContinue.setBackgroundResource(R.drawable.btn_blue)
            binding.btnContinue.isEnabled = true
        } else {
            binding.btnContinue.setBackgroundResource(R.drawable.btn_gray)
            binding.btnContinue.isEnabled = false
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissActionClick?.invoke()
    }
}