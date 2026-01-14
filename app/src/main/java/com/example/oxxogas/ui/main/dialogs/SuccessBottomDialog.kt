package com.example.oxxogas.ui.main.dialogs

import android.os.Bundle
import android.view.View
import com.example.oxxogas.databinding.BottomDialogSuccessBinding

class SuccessBottomDialog : BaseBottomSheetDialog<BottomDialogSuccessBinding>() {
    override fun initBinding(): BottomDialogSuccessBinding =
        BottomDialogSuccessBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {

    }
}