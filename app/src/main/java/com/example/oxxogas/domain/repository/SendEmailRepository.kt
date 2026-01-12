package com.example.oxxogas.domain.repository

import com.example.oxxogas.domain.models.request.EmailRequest
import com.example.oxxogas.domain.models.response.EmailResponse
import retrofit2.Response

interface SendEmailRepository {

    suspend fun sendEmail(sendEmailRequest: EmailRequest): Response<EmailResponse>?
}