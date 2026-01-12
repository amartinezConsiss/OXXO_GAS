package com.example.oxxogas.domain.models.response

data class EmailResponse (
    val success: Boolean,
    val message: String,
    val statusCode: Int
)