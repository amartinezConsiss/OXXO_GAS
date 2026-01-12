package com.example.oxxogas.ui.newsale.dialogs

import android.os.Bundle
import android.view.View
import com.example.oxxogas.databinding.BottomDialogDisabledPumpBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener

class DisabledPumpBottomSheetDialog : BaseBottomSheetDialog<BottomDialogDisabledPumpBinding>() {

    override fun initBinding() = BottomDialogDisabledPumpBinding.inflate(layoutInflater)

    override fun initView(view: View, saveState: Bundle?) {
        binding.btnCloseNotice.setSafeOnClickListener {
            dismiss()
        }
    }

}