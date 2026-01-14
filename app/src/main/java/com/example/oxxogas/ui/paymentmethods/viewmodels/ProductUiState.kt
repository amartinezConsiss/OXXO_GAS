package com.example.oxxogas.ui.paymentmethods.viewmodels

import java.math.BigDecimal

data class ProductUiState(
    val description: String,
    val priceUnit: BigDecimal,
    val quantity: BigDecimal,
    val total: BigDecimal
)
