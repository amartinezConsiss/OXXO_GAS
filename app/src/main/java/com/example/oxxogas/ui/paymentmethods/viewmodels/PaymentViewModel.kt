package com.example.oxxogas.ui.paymentmethods.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PaymentViewModel @Inject constructor() : ViewModel() {

    private val _productState = MutableLiveData<ProductUiState>()
    val productState: LiveData<ProductUiState> = _productState

    fun loadDummyInfo() {
        val productNumber = (1..3).random()
        val quantityNumber = 1.00 + (50.00 - 1.00) * Random.nextDouble()

        val quantity = BigDecimal.valueOf(quantityNumber)
            .setScale(3, RoundingMode.HALF_UP)

        val (description, price) = when (productNumber) {
            1 -> "Magna" to BigDecimal("24.63")
            2 -> "Premium" to BigDecimal("25.86")
            else -> "Diesel" to BigDecimal("23.52")
        }

        val total = price
            .multiply(quantity)
            .setScale(2, RoundingMode.HALF_UP)

        _productState.value = ProductUiState(
            description = description,
            priceUnit = price,
            quantity = quantity,
            total = total
        )
    }
}
