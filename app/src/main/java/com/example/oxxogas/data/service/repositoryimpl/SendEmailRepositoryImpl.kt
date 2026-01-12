package com.example.oxxogas.data.service.repositoryimpl

import com.example.oxxogas.data.service.api.services.OxxoGasApi
import com.example.oxxogas.domain.models.request.EmailRequest
import com.example.oxxogas.domain.models.response.EmailResponse
import com.example.oxxogas.domain.repository.SendEmailRepository
import retrofit2.Response
import javax.inject.Inject

class SendEmailRepositoryImpl @Inject constructor(
    private val service: OxxoGasApi
) : SendEmailRepository {

    override suspend fun sendEmail(sendEmailRequest: EmailRequest): Response<EmailResponse> {
        return service.sendEmail(sendEmailRequest)
    }

}