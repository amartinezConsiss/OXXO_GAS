package com.example.oxxogas.ui.main.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.oxxogas.databinding.DialogProcessBinding

class ProgressDialog : DialogFragment() {

    private var _binding: DialogProcessBinding? = null

    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogProcessBinding.inflate(layoutInflater)

        return Dialog(requireContext()).apply {
            setContentView(binding.root)
            setCancelable(false)
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()

        dialog.window?.apply {
            setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}