package com.example.oxxogas.ui.newsale.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oxxogas.R
import com.example.oxxogas.domain.models.PetrolPumpsList
import com.example.oxxogas.databinding.ItemNewSalePetrolPumpBinding
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener

class NewSalePetrolPumpAdapter(
    private val petrolPumpList: List<PetrolPumpsList>,
    private val listener: (Int, Int) -> Unit
) : RecyclerView.Adapter<NewSalePetrolPumpAdapter.NewSalePatrolPumpViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewSalePatrolPumpViewHolder {
        val viewBinding =
            ItemNewSalePetrolPumpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewSalePatrolPumpViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = petrolPumpList.size

    override fun onBindViewHolder(holder: NewSalePatrolPumpViewHolder, position: Int) {
        val item = petrolPumpList[position]
        holder.bind(item)
    }

    inner class NewSalePatrolPumpViewHolder(private val itemBinding: ItemNewSalePetrolPumpBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(petrolPump: PetrolPumpsList) {
            val context = itemBinding.root.context
            itemBinding.pumpStatus.text = petrolPump.petrolPumpNameStatus
            itemBinding.pumpTitle.text = context.getString(R.string.pum_id, petrolPump.petrolPumpId.toString())

            when (petrolPump.petrolPumpStatus) {
                1 -> {
                    itemBinding.ivPump.setImageResource(R.drawable.img_gas_blue)
                }
                2 -> {
                    itemBinding.ivPump.setImageResource(R.drawable.img_bad_alert)
                    itemBinding.pump.setBackgroundResource(R.drawable.box_red_stroke)
                    itemBinding.pumpStatus.setTextColor(context.getColor(R.color.white))
                    itemBinding.pumpTitle.setTextColor(context.getColor(R.color.white))
                }

                3 -> {
                    itemBinding.ivPump.setImageResource(R.drawable.img_gray_close)
                    itemBinding.pump.setBackgroundResource(R.drawable.box_gray_30)
                    itemBinding.pumpStatus.setTextColor(context.getColor(R.color.stroke))
                    itemBinding.pumpTitle.setTextColor(context.getColor(R.color.stroke))
                }

                4 -> {
                    itemBinding.ivPump.setImageResource(R.drawable.img_gas_blue)
                    itemBinding.pump.setBackgroundResource(R.drawable.box_brand_yellow)
                }
            }

            itemBinding.root.setSafeOnClickListener {
                listener(petrolPump.petrolPumpId, petrolPump.petrolPumpStatus)
            }
        }
    }
}