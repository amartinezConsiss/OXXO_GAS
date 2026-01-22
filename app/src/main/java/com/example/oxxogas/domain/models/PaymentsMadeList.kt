package com.example.oxxogas.domain.models

import java.math.BigDecimal

data class PaymentsMadeList(
    var typeMethod: Int,
    var amount: BigDecimal,
    var count: Int
)