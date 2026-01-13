package com.example.oxxogas.ui.menu.viewmodels

import androidx.lifecycle.ViewModel
import com.example.oxxogas.R
import com.example.oxxogas.domain.models.MenuItemList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : ViewModel() {

    fun getMenuInformation(): List<MenuItemList> = listOf(
        MenuItemList(
            idMenuItem = 1,
            iconActiveMenuItem = R.drawable.img_station,
            iconInactiveMenuItem = R.drawable.img_gray_station,
            menuNameItem = "Nueva Venta",
            status = true
        ),
        MenuItemList(
            idMenuItem = 2,
            iconActiveMenuItem = R.drawable.img_transport,
            iconInactiveMenuItem = R.drawable.img_gray_transport,
            menuNameItem = "Flotilla Corporativa",
            status = true
        ),
        MenuItemList(
            idMenuItem = 3,
            iconActiveMenuItem = R.drawable.img_product,
            iconInactiveMenuItem = R.drawable.img_gray_product,
            menuNameItem = "Otros Productos",
            status = true
        ),
        MenuItemList(
            idMenuItem = 4,
            iconActiveMenuItem = R.drawable.img_gif,
            iconInactiveMenuItem = R.drawable.img_gray_gif,
            menuNameItem = "Nuevas Promociones",
            status = true
        ),
        MenuItemList(
            idMenuItem = 5,
            iconActiveMenuItem = R.drawable.img_restricted,
            iconInactiveMenuItem = R.drawable.img_gray_money,
            menuNameItem = "Programas y Recompensas",
            status = false
        ),
        MenuItemList(
            idMenuItem = 6,
            iconActiveMenuItem = R.drawable.img_restricted,
            iconInactiveMenuItem = R.drawable.img_gray_restricted,
            menuNameItem = "Cancelar Transacción",
            status = true
        ),
    )

    fun getCurrentCash(): Double {
        return 1.00 + (10000.00 - 1000.00) * kotlin.random.Random.nextDouble()
    }

    fun getCumulativeSale(): Double {
        return 1.00 + (1000.00 - 100.00) * kotlin.random.Random.nextDouble()
    }

}