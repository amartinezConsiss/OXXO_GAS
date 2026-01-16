package com.example.oxxogas.ui.main.activities

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.oxxogas.databinding.LayoutToolbarBinding
import com.example.oxxogas.ui.main.dialogs.ErrorBottomSheetDialog
import com.example.oxxogas.ui.main.dialogs.ProgressBottomSheetDialog
import com.example.oxxogas.ui.main.dialogs.ProgressDialog
import com.example.oxxogas.ui.main.dialogs.SuccessBottomDialog
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener

abstract class BaseActivity<T> : AppCompatActivity() where T : ViewBinding {

    private lateinit var binding: T

    private lateinit var progressDialog: ProgressDialog

    private lateinit var progressButtonDialog: ProgressBottomSheetDialog

    private lateinit var successButtonDialog: SuccessBottomDialog

    private lateinit var errorButtonDialog: ErrorBottomSheetDialog
    abstract fun initBinding(): T

    abstract fun initView(saveInstanceState: Bundle?)

    private var backCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = initBinding()
        setContentView(binding.root)
        this.initView(savedInstanceState)
    }

    fun setupToolbar(
        layoutToolbar: LayoutToolbarBinding,
        name: String,
        backAvailable: Boolean = true
    ) {
        layoutToolbar.tvTitle.text = name

        if (backAvailable) {
            layoutToolbar.ivBack.visibility = View.VISIBLE
            layoutToolbar.ivBack.setSafeOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        } else {
            layoutToolbar.ivBack.visibility = View.GONE
        }
    }

    fun showProgressDialog() {
        progressDialog = ProgressDialog()
        progressDialog.show(
            supportFragmentManager, "Progress Dialog"
        )
    }

    fun dismissProgressDialog() {
        progressDialog.dismiss()
    }

    fun showProgressBottomDialog(iconProgress: Int, titleProgress: Int) {
        progressButtonDialog = ProgressBottomSheetDialog(iconProgress, titleProgress)
        progressButtonDialog.show(
            supportFragmentManager, "Progress Dialog"
        )
    }

    fun dismissProgressBottomDialog() {
        progressButtonDialog.dismiss()
    }

    fun showSuccessBottomDialog(iconSuccess: Int, titleSuccess: Int, messageSuccess: Int) {
        successButtonDialog = SuccessBottomDialog(iconSuccess, titleSuccess, messageSuccess)
        successButtonDialog.show(
            supportFragmentManager, "Progress Dialog"
        )
    }

    fun dismissSucessBottomDialog() {
        successButtonDialog.dismiss()
    }

    fun showErrorBottomDialog(
        iconTypeError: Int,
        titleError: Int,
        iconError: Int,
        messageError: Int
    ) {
        errorButtonDialog =
            ErrorBottomSheetDialog(iconTypeError, titleError, iconError, messageError)
        errorButtonDialog.show(
            supportFragmentManager, "Progress Dialog"
        )
    }

    fun dismissErrorBottomDialog() {
        errorButtonDialog.dismiss()
    }

    fun enableBack() {
        backCallback?.isEnabled = false
    }

    private fun disableBack() {
        backCallback?.remove()
        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Back bloqueado
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback!!)
    }

}