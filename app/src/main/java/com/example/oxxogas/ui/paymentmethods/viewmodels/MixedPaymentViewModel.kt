package com.example.oxxogas.ui.paymentmethods.viewmodels

import androidx.lifecycle.ViewModel
import com.example.oxxogas.domain.models.PaymentsMadeList
import com.example.oxxogas.ui.main.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class MixedPaymentViewModel @Inject constructor() : ViewModel() {
    private var total: BigDecimal = BigDecimal.ZERO
    private var paymentsMade: List<PaymentsMadeList> = emptyList()

    fun initData(
        total: Double?,
        payments: List<PaymentsMadeList>
    ) {
        this.total = total?.toBigDecimal() ?: BigDecimal.ZERO
        this.paymentsMade = payments
    }

    fun updatePayments(payments: List<PaymentsMadeList>) {
        paymentsMade = payments
    }

    fun outstandingAmount(): BigDecimal {
        val sum = paymentsMade.sumOf { it.amount }
        return total - sum
    }

    fun onlyCardsOutstanding():
            Double {
        val sum = paymentsMade
            .filter { it.typeMethod == Constants.CARD_PAYMENT_METHOD }
            .sumOf { it.amount }

        return (total - sum).toDouble()
    }

    fun finalCashAmount(): BigDecimal {
        return paymentsMade
            .firstOrNull { it.typeMethod == Constants.CASH_PAYMENT_METHOD }
            ?.amount ?: BigDecimal.ZERO
    }
}