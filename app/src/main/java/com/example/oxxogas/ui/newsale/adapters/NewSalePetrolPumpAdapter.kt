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
    private val listener: (Int, Boolean) -> Unit
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
            itemBinding.pumpId.text = petrolPump.petrolPumpId.toString()

            if (petrolPump.petrolPumpStatus) {
                itemBinding.pump.setBackgroundResource(R.drawable.yellow_box)
            } else {
                itemBinding.pump.setBackgroundResource(R.drawable.gray_box)
            }

            itemBinding.root.setSafeOnClickListener {
                if (petrolPump.petrolPumpStatus) {
                    it.setBackgroundResource(R.drawable.blue_box)
                }
                listener(petrolPump.petrolPumpId, petrolPump.petrolPumpStatus)
            }
        }
    }
}