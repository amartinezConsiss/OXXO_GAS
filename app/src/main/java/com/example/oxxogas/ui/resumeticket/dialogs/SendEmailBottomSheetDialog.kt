package com.example.oxxogas.ui.resumeticket.dialogs

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import com.example.oxxogas.R
import com.example.oxxogas.databinding.BottomDialogSendEmailBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.afterTextChanged
import com.example.oxxogas.ui.main.utils.isValidEmail
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import kotlinx.coroutines.launch

class SendEmailBottomSheetDialog : BaseBottomSheetDialog<BottomDialogSendEmailBinding>() {

    var onSendEmailClick: ((String) -> Unit)? = null

    override fun initBinding(): BottomDialogSendEmailBinding =
        BottomDialogSendEmailBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {
        binding.btnSendEmail.setSafeOnClickListener {
            lifecycleScope.launch {
                val imm = requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                val email = binding.etEnterEmail.text
                onSendEmailClick?.invoke(email.toString())
                dismiss()
            }
        }

        binding.btnCloseEmail.setSafeOnClickListener {
            dismiss()
        }

        binding.etEnterEmail.afterTextChanged { email ->
            if (isValidEmail(email)) {
                binding.btnSendEmail.isEnabled = true
                binding.btnSendEmail.setBackgroundResource(R.drawable.blue_btn)
            } else {
                binding.btnSendEmail.isEnabled = true
                binding.btnSendEmail.setBackgroundResource(R.drawable.gray_btn)
            }
        }
    }
}