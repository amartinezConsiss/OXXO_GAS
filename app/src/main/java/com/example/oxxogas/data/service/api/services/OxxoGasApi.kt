package com.example.oxxogas.data.service.api.services

import com.example.oxxogas.domain.models.request.EmailRequest
import com.example.oxxogas.domain.models.response.EmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OxxoGasApi {

    @POST("api/send-email")
    suspend fun sendEmail(
        @Body emailRequest: EmailRequest
    ): Response<EmailResponse>
}