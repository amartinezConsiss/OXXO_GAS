package com.example.oxxogas.domain.models.request

data class EmailRequest(
    val to: String,
    val subject: String,
    val message: String,
    val pdfBase64: String,
    val pdfFileName: String
)