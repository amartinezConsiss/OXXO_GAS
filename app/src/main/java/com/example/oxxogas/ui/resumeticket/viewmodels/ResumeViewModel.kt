package com.example.oxxogas.ui.resumeticket.viewmodels

import android.content.Context
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oxxogas.domain.models.request.EmailRequest
import com.example.oxxogas.domain.models.response.BaseResponse
import com.example.oxxogas.domain.models.response.EmailResponse
import com.example.oxxogas.domain.repository.SendEmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ResumeViewModel @Inject constructor(
    private val sendEmailRepository: SendEmailRepository
) : ViewModel() {

    val sendEmailResult: MutableLiveData<BaseResponse<EmailResponse>> = MutableLiveData()

    fun sendEmail(
        context: Context,
        userEmail: String,
        paymentMethod: Int
    ) {
        val pdfFile = getPdfName(paymentMethod)

        val pdfBase64 = pdfFromAssetsToBase64(
            context,
            pdfFile
        )

        val emailRequest = EmailRequest(
            to = userEmail,
            subject = "OXXOGAS - Ticket Folio 395646090",
            message = "Gracias por su compra, se anexa su ticket de venta.",
            pdfBase64 = pdfBase64,
            pdfFileName = "Ticket.pdf"
        )

        sendEmailResult.value = BaseResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = sendEmailRepository.sendEmail(emailRequest)
                if (response?.code() == 200) {
                    sendEmailResult.postValue(BaseResponse.Success(response.body()))
                } else {
                    sendEmailResult.value = BaseResponse.Error(response?.message())
                }
            } catch (ex: Exception) {
                sendEmailResult.postValue(BaseResponse.Error(ex.message))
            }
        }
    }

    private fun pdfFromAssetsToBase64(context: Context, fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val bytes = inputStream.readBytes()
        inputStream.close()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun getPdfName(paymentMethod: Int): String {
        return when (paymentMethod) {
            1 -> "PDF-ticket-tarjeta.pdf"
            2 -> "PDF-ticket-efectivo.pdf"
            3 -> "PDF-ticket-mixto.pdf"
            else -> "PDF-ticket-tarjeta.pdf"
        }
    }

}