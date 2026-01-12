package com.example.oxxogas.ui.menu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oxxogas.R
import com.example.oxxogas.databinding.ItemMenuBinding
import com.example.oxxogas.domain.models.MenuItemList
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener

class MenuHomeAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<MenuHomeAdapter.MenuHomeViewHolder>() {

    private val menuItemList = listOf(
        MenuItemList(1, R.drawable.img_station, "Nueva Venta", true),
        MenuItemList(2, R.drawable.img_transport, "Flotilla Corporativa", true),
        MenuItemList(3, R.drawable.img_product, "Otros Productos", true),
        MenuItemList(4, R.drawable.img_gif, "Nuevas Promociones", true),
        MenuItemList(5, R.drawable.img_restricted, "Programas y Recompensas", true),
        MenuItemList(6, R.drawable.img_restricted, "Cancelar Transacción", true),
    )

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
            /*when (menuItem.idMenuItem) {
                1 -> {
                    itemBinding.ivMenu.setImageResource(menuItem.iconMenuItem)
                }

                2 -> {
                    itemBinding.ivMenu.setImageResource(menuItem.iconMenuItem)
                }

                3 -> {
                    itemBinding.ivMenu.setImageResource(menuItem.iconMenuItem)
                }

                4 -> {
                    itemBinding.ivMenu.setImageResource(menuItem.iconMenuItem)
                }

                5 -> {
                    itemBinding.ivMenu.setImageResource(menuItem.iconMenuItem)
                }

                6 -> {
                    itemBinding.ivMenu.setImageResource(menuItem.iconMenuItem)
                }
            }*/
            itemBinding.ivMenu.setImageResource(menuItem.iconMenuItem)
            itemBinding.tvMenuName.text = menuItem.menuNameItem
            itemBinding.root.setSafeOnClickListener {
                listener(menuItem.idMenuItem)
            }
        }
    }

}