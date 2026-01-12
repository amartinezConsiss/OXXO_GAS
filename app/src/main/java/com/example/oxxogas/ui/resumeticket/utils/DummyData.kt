package com.example.oxxogas.ui.resumeticket.utils

import com.example.oxxogas.domain.models.CardInformation
import com.example.oxxogas.domain.models.ResumeTicketData
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DummyData {

    fun getCashData(): ResumeTicketData {
        return ResumeTicketData(
            station = "ESTACION 15 DE MAYO MTY",
            address = "AVENIDA VENUSTIANO CARRANZA SUR 489, MONTERREY, NUEVO LEON",
            gasStationAttendant = "MIGUEL ANGEL GARCIA ALVARADO",
            date = getCurrentDate(),
            time = getCurrentTime(),
            folio = "395646090"
        )
    }

    fun getCardData(): ResumeTicketData {
        return ResumeTicketData(
            station = "ESTACION RODRIGO GOMEZ MTY",
            address = "AVENIDA RODRIGO GOMEZ NO. 1445, MONTERREY, NUEVO LEON",
            gasStationAttendant = "GAEL ALEXANDER RODRIGUEZ ALCANTARA",
            date = getCurrentDate(),
            time = getCurrentTime(),
            folio = "711679490",
            cardInformation = getCardInformation()
        )
    }

    fun getMixedData(): ResumeTicketData {
        return ResumeTicketData(
            station = "ESTACION VILLA LAS FUENTES MTY",
            address = "SENDERO DEL DORADO Y PASEO DEL AGUA, MONTERREY, NUEVO LEON",
            gasStationAttendant = "CARMEN VIOLETA RAMIREZ TARIN",
            date = getCurrentDate(),
            time = getCurrentTime(),
            folio = "748953620"
        )
    }


    private fun getCardNumber(): String {
        val number = (1000..9999).random()
        return "XXXX-XXXX-XXXX-$number"
    }

    fun getCardInformation(amountCard: Double = 0.0): CardInformation {
        val numberBankName = (1..3).random()
        val numberAppLabelName = (1..2).random()
        var bankName = when (numberBankName) {
            1 -> "Visa"
            2 -> "Mastercard"
            3 -> "Amex"
            else -> "Visa"
        }

        var appLabel = ""
        when (numberBankName) {
            1 -> {
                appLabel = if (numberAppLabelName == 1) {
                    "Visa debito"
                } else {
                    "Visa credito"
                }

            }

            2 -> {
                appLabel = if (numberAppLabelName == 1) {
                    "Mastercard debito"
                } else {
                    "Mastercard credito"
                }
            }

            3 -> {
                appLabel = if (numberAppLabelName == 1) {
                    "Amex debito"
                } else {
                    "Amex credito"
                }
            }
        }

        return CardInformation(
            cardNumber = getCardNumber(),
            bankName = bankName,
            terminalId = 55012003,
            affiliationId = 5013858,
            approvalId = 232345,
            arq = "7443fecff32fbda5",
            aid = "A000000031010",
            ref = "230813492381",
            charge = "2275",
            appLabel = appLabel,
            amountCard = amountCard
        )
    }

    private fun getCurrentDate(): String {
        val format = SimpleDateFormat("MMM dd yy", Locale("es", "MX"))
        return format.format(Date()).uppercase()
    }

    private fun getCurrentTime(): String {
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return format.format(Date())
    }

    fun getPoints(): Double {
        val value = 1.00 + (1000.00 - 1.00) * kotlin.random.Random.nextDouble()
        return round(value * 100)/100.0
    }

    fun getNamePaymentMethod(idPayment: Int): String {
        return when (idPayment) {
            1 -> "Tarjeta"
            2 -> "Efectivo"
            else -> "Efectivo"
        }
    }
}