package com.example.oxxogas.ui.menu.adapters

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oxxogas.R
import com.example.oxxogas.databinding.ItemMenuBinding
import com.example.oxxogas.domain.models.MenuItemList
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener

class MenuHomeAdapter(private val menuItemList: List<MenuItemList>, private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<MenuHomeAdapter.MenuHomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHomeViewHolder {
        val viewBinding =
            ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuHomeViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = menuItemList.size

    override fun onBindViewHolder(holder: MenuHomeViewHolder, position: Int) {
        val item = menuItemList[position]
        holder.bind(item)
    }

    inner class MenuHomeViewHolder(private val itemBinding: ItemMenuBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(menuItem: MenuItemList) {
            if (menuItem.status) {
                itemBinding.ivMenu.setImageResource(menuItem.iconActiveMenuItem)
                itemBinding.root.setBackgroundResource(R.drawable.box_white)
            } else {
                itemBinding.ivMenu.setImageResource(menuItem.iconInactiveMenuItem)
                itemBinding.root.setBackgroundResource(R.drawable.box_gray_30)
                itemBinding.tvMenuName.setTextColor(itemBinding.root.context.getColor(R.color.stroke))
            }
            itemBinding.tvMenuName.text = menuItem.menuNameItem
            itemBinding.root.setSafeOnClickListener {
                listener(menuItem.idMenuItem)
            }
        }
    }

}