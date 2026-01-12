package com.example.oxxogas.domain.models

data class ResumeTicketData(
    var station: String? = "",
    var address: String? = "",
    var gasStationAttendant: String? = "",
    var date: String? = "",
    var time: String? = "",
    var folio: String? = "",
    var shopInformation: ShopInformation? = null,
    var cardInformation: CardInformation? = null
)

data class CardInformation(
    var cardNumber: String? = "",
    var bankName: String = "",
    var terminalId: Int? = 0,
    var affiliationId: Int? = 0,
    var approvalId: Int? = 0,
    var arq: String? = "",
    var ref: String? = "",
    var aid: String? = "",
    var appLabel: String? = "",
    var charge: String? = "",
    var amountCard: Double? = 0.0
)

data class ShopInformation(
    var pump: Int? = 0,
    var methodPayment: Int? = 0,
    var typeProduct: String? = "",
    var quantity: Double? = 0.0,
    var price: Double? = 0.0,
    var amount: Double? = 0.0,
    var cashAmount: Double? = 0.0,
    var totalCardMixedAmount: Double? = 0.0,
    var cardsMixedInformation: MutableList<CardInformation>? = mutableListOf(),
    var total: Double? = 0.0,
    var hasSpinPremia: Boolean? = false,
)
