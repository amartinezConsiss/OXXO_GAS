package com.example.oxxogas.ui.paymentmethods.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oxxogas.R
import com.example.oxxogas.databinding.ItemPaymentsMadeBinding
import com.example.oxxogas.domain.models.PaymentsMadeList
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.main.utils.setCurrencyFormat

class PaymentsMadeAdapter :
    RecyclerView.Adapter<PaymentsMadeAdapter.PaymentsMadeViewHolder>() {

    private var paymentsMadeList = mutableListOf<PaymentsMadeList>()

    @SuppressLint("NotifyDataSetChanged")
    fun setPaymentMade(paymentsMade: MutableList<PaymentsMadeList>) {
        paymentsMadeList = paymentsMade
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentsMadeViewHolder {
        val viewBinding =
            ItemPaymentsMadeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentsMadeViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PaymentsMadeViewHolder, position: Int) {
        holder.bind(paymentsMadeList[position])
    }

    override fun getItemCount(): Int = paymentsMadeList.size

    inner class PaymentsMadeViewHolder(private val viewBinding: ItemPaymentsMadeBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(paymentMade: PaymentsMadeList) {
            val context = viewBinding.root.context
            when (paymentMade.typeMethod) {
                Constants.CARD_PAYMENT_METHOD -> {
                    viewBinding.typePayment.text = context.getString(
                        R.string.pay_with_card_amount,
                        paymentMade.count.toString()
                    )
                    viewBinding.madeAmount.text = setCurrencyFormat(paymentMade.amount)
                }

                Constants.CASH_PAYMENT_METHOD -> {
                    viewBinding.typePayment.text = context.getString(R.string.cash_payment_amount)
                    viewBinding.madeAmount.text = setCurrencyFormat(paymentMade.amount)
                }
            }
        }

    }
}