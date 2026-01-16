package com.example.oxxogas.ui.main.dialogs

import android.os.Bundle
import android.view.View
import com.example.oxxogas.databinding.BottomDialogProgressBinding

class ProgressBottomSheetDialog(
    private val iconType: Int,
    private val titleProgress: Int,
) : BaseBottomSheetDialog<BottomDialogProgressBinding>() {

    override fun initBinding(): BottomDialogProgressBinding =
        BottomDialogProgressBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {
        binding.ivProgressType.setImageResource(iconType)
        binding.titleProgress.setText(titleProgress)
    }
}