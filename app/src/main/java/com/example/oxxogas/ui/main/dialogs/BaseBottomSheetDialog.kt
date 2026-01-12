package com.example.oxxogas.ui.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialog<T> : BottomSheetDialogFragment() where T : ViewBinding{

    private var _binding: T? = null

    protected val binding: T get() = _binding!!

    abstract fun initBinding(): T

    abstract fun initView(view: View, saveState: Bundle?)

    override fun onStart() {
        super.onStart()
        val bottomSheet =
            dialog?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            ) ?: return

        BottomSheetBehavior.from(bottomSheet).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view,savedInstanceState)
    }

}