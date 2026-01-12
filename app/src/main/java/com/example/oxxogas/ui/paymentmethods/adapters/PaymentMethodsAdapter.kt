package com.example.oxxogas.ui.paymentmethods.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.oxxogas.R
import com.example.oxxogas.domain.models.PaymentMethodsList
import com.example.oxxogas.databinding.ItemPaymentMethodBinding
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener

class PaymentMethodsAdapter(
    private var paymentMethods: List<PaymentMethodsList>,
    private val listener: (Int) -> Unit
) :
    RecyclerView.Adapter<PaymentMethodsAdapter.PaymentMethodsViewHolder>() {

    fun setPaymentMethods(paymentMethods: List<PaymentMethodsList>) {
        this.paymentMethods = paymentMethods

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodsViewHolder {
        val viewBinding =
            ItemPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentMethodsViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = paymentMethods.size

    override fun onBindViewHolder(holder: PaymentMethodsViewHolder, position: Int) {
        val item = paymentMethods[position]
        holder.bind(item)
    }

    inner class PaymentMethodsViewHolder(private val viewBinding: ItemPaymentMethodBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(paymentMethod: PaymentMethodsList) {
            val context = viewBinding.ivPaymentMethod.context
            viewBinding.ivPaymentMethod.setImageResource(paymentMethod.paymentMethodIcon)
            viewBinding.tvNamePaymentMethod.text = paymentMethod.paymentMethodName

            viewBinding.root.setSafeOnClickListener {
                listener.invoke(paymentMethod.idPaymentMethod)
            }

            setSelectedComponent(paymentMethod.isSelected, context)

        }

        private fun setSelectedComponent(paymentMethodStatus: Boolean, context: Context) {
            if (paymentMethodStatus) {
                viewBinding.tvNamePaymentMethod.setTextColor(
                    context.getColor(
                        R.color.white
                    )
                )
                viewBinding.ivPaymentMethod.imageTintList =
                    ContextCompat.getColorStateList(context, R.color.white)
                viewBinding.containerPaymentMethod.setBackgroundResource(R.drawable.blue_box_with_not_padding)
            } else {
                viewBinding.tvNamePaymentMethod.setTextColor(
                    context.getColor(
                        R.color.blue
                    )
                )
                viewBinding.ivPaymentMethod.imageTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                viewBinding.containerPaymentMethod.setBackgroundResource(R.drawable.border_blue)
            }

        }
    }

}