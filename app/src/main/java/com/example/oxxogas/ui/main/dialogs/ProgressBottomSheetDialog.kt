package com.example.oxxogas.ui.main.dialogs

import android.os.Bundle
import android.view.View
import com.example.oxxogas.databinding.BottomDialogProgressBinding

class ProgressBottomSheetDialog : BaseBottomSheetDialog<BottomDialogProgressBinding>() {
    override fun initBinding(): BottomDialogProgressBinding =
        BottomDialogProgressBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {

    }
}