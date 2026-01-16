package com.example.oxxogas.ui.main.dialogs

import android.os.Bundle
import android.view.View
import com.example.oxxogas.databinding.BottomDialogSuccessBinding

class SuccessBottomDialog(
    private val iconType: Int,
    private val titleSuccess: Int,
    private val messageSuccess: Int
) :
    BaseBottomSheetDialog<BottomDialogSuccessBinding>() {
    override fun initBinding(): BottomDialogSuccessBinding =
        BottomDialogSuccessBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {
        binding.ivSuccessType.setImageResource(iconType)
        binding.titleSuccess.setText(titleSuccess)
        binding.messageSuccess.setText(messageSuccess)
        this.isCancelable = false
    }
}