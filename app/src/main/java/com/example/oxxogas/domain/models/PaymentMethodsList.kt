package com.example.oxxogas.domain.models

data class PaymentMethodsList(
    var idPaymentMethod: Int,
    var paymentMethodName: String,
    var paymentMethodIcon: Int,
    var isSelected: Boolean
)