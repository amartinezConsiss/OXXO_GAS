package com.example.oxxogas.ui.main.dialogs

import android.os.Bundle
import android.view.View
import com.example.oxxogas.databinding.BottomDialogErrorBinding

class ErrorBottomSheetDialog(
    private val iconType: Int,
    private val titleError: Int,
    private val iconError: Int,
    private val messageSuccess: Int
) : BaseBottomSheetDialog<BottomDialogErrorBinding>() {
    override fun initBinding(): BottomDialogErrorBinding =
        BottomDialogErrorBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {
        binding.ivErrorType.setImageResource(iconType)
        binding.titleError.setText(titleError)
        binding.iconError.setImageResource(iconError)
        binding.messageError.setText(messageSuccess)
    }
}