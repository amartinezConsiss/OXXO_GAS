package com.example.oxxogas.ui.resumeticket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oxxogas.R
import com.example.oxxogas.domain.models.CardInformation
import com.example.oxxogas.databinding.ItemVoucherCardBinding
import com.example.oxxogas.ui.main.utils.setCurrencyFormat

class MixedCardResumeAdapter(private val cardsItem: MutableList<CardInformation>) :
    RecyclerView.Adapter<MixedCardResumeAdapter.MixedCardResumeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MixedCardResumeViewHolder {
        val viewBinding =
            ItemVoucherCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MixedCardResumeViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MixedCardResumeViewHolder, position: Int) {
        val card = cardsItem[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int = cardsItem.size

    inner class MixedCardResumeViewHolder(val binding: ItemVoucherCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cardItem: CardInformation) {
            val context = binding.cardNumber.context
            binding.tvLabelPaymentMethod.visibility = View.VISIBLE
            binding.tvAmountCard.visibility = View.VISIBLE
            binding.tvAmountCard.text = setCurrencyFormat(cardItem.amountCard ?: 0.0)
            binding.cardNumber.text = cardItem.cardNumber
            binding.bankInformation.text = context.getString(
                R.string.card_information,
                cardItem.bankName,
                cardItem.terminalId.toString(),
                cardItem.affiliationId.toString()
            )
            binding.tvApproval.text =
                context.getString(R.string.approval, cardItem.approvalId.toString())
            binding.tvArq.text = context.getString(R.string.arq, cardItem.arq)
            binding.tvRef.text = context.getString(R.string.ref, cardItem.ref)
            binding.tvAid.text = context.getString(R.string.aid, cardItem.aid)
            binding.tvAppLabel.text =
                context.getString(R.string.app_label, cardItem.appLabel)
            binding.tvCharge.text = context.getString(R.string.charge, cardItem.charge)
        }
    }

}