package com.example.oxxogas.ui.paymentmethods.dialogs

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.oxxogas.R
import com.example.oxxogas.databinding.BottomDialogCardPaymentBinding
import com.example.oxxogas.ui.main.dialogs.BaseBottomSheetDialog
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CardPaymentBottomSheetDialog(
    private val totalAmount: Double,
    private val isSuccess: Boolean = true
) :
    BaseBottomSheetDialog<BottomDialogCardPaymentBinding>() {

    var onPayCardSuccessCallback: (() -> Unit)? = null
    var onPayCardFailCallback: (() -> Unit)? = null

    override fun initBinding(): BottomDialogCardPaymentBinding =
        BottomDialogCardPaymentBinding.inflate(layoutInflater)

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

    override fun initView(view: View, saveState: Bundle?) {
        binding.btnCancelCardPayment.setSafeOnClickListener {
            this.dismiss()
        }
        binding.tvAmountToPay.text = setCurrencyFormat(totalAmount)

        binding.ivCardPay.setSafeOnClickListener { button ->
            customResultPaymentView()
            if (isSuccess) {
                button.isEnabled = false
                this.isCancelable = false
                loadSuccessAnimation()
                successView()
            } else {
                loadFailAnimation()
                failedView()
            }
        }
    }

    private fun loadSuccessAnimation() {
        Glide.with(this)
            .asGif()
            .load(R.drawable.check_white)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>,
                    isFirstResource: Boolean
                ) = false

                override fun onResourceReady(
                    resource: GifDrawable,
                    model: Any,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    resource.setLoopCount(2)
                    resource.registerAnimationCallback(
                        object : Animatable2Compat.AnimationCallback() {
                            override fun onAnimationEnd(drawable: Drawable?) {
                                onPayCardSuccessCallback?.invoke()
                            }
                        }
                    )
                    return false
                }

            })
            .into(binding.ivCardPay)
    }

    private fun loadFailAnimation() {
        Glide.with(this)
            .asGif()
            .load(R.drawable.error_white)
            .into(binding.ivCardPay)
    }


    private fun customResultPaymentView() {
        if (isSuccess) this.isCancelable = false
        val paramsIvTop = binding.ivCardPay.layoutParams as ViewGroup.MarginLayoutParams
        val paramsBtnTop = binding.btnCancelCardPayment.layoutParams as ViewGroup.MarginLayoutParams
        paramsIvTop.topMargin = resources.getDimensionPixelSize(R.dimen.margin_125)
        paramsBtnTop.topMargin = resources.getDimensionPixelSize(R.dimen.margin_125)
        binding.ivCardPay.layoutParams = paramsIvTop
        binding.ivPayment.imageTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.white)
        binding.dragHandle.visibility = View.GONE
        binding.tvAmountToPay.visibility = View.GONE
        binding.btnCancelCardPayment.visibility = View.INVISIBLE
        binding.tvLabelAmountToPay.visibility = View.GONE
        binding.tvInstructionsCard.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.titleCardPayment.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }

    private fun successView() {
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.btn_green))
        binding.tvInstructionsCard.text = getString(R.string.success_card)
    }

    private fun failedView() {
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red_error_card
            )
        )
        binding.tvInstructionsCard.text = "Transacción Denegada"
        binding.tvInstructionsCard.isAllCaps = true
        binding.instructionsFail.visibility = View.VISIBLE
        binding.btnFailContinue.setSafeOnClickListener {
            dismiss()
        }
    }

}