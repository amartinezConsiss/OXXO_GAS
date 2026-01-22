package com.example.oxxogas.ui.main.utils

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.navigation.NavController
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun EditText.applyCurrencyFormat(
    locale: Locale = Locale("es", "MX")
) {
    var current = ""

    this.addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s.toString() != current) {
                removeTextChangedListener(this)

                val cleanString = s.toString().replace("[^\\d]".toRegex(), "")

                if (cleanString.isNotEmpty()) {
                    val parsed = cleanString.toDouble() / 100
                    val formatted = NumberFormat
                        .getCurrencyInstance(locale)
                        .format(parsed)

                    current = formatted
                    setText(formatted)
                    setSelection(formatted.length)
                }

                addTextChangedListener(this)
            }
        }
    })
}

fun setCurrencyFormat(amount: Double, locale: Locale = Locale("es", "MX")): String {
    return NumberFormat
        .getCurrencyInstance(locale)
        .format(amount)
}

fun setCurrencyFormat(amount: BigDecimal, locale: Locale = Locale("es", "MX")): String {
    return NumberFormat
        .getCurrencyInstance(locale)
        .format(amount)
}

fun Double.roundUp(decimales: Double): Double {
    val factor = 10.0.pow(decimales)
    return kotlin.math.ceil(this * factor) / factor
}

fun View.setSafeOnClickListener(
    interval: Long = 1000L,
    onSafeClick: (View) -> Unit
) {
    var lastClickTime = 0L

    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= interval) {
            lastClickTime = currentTime
            onSafeClick(it)
        }
    }
}

fun NavController.safeNavigate(actionId: Int, bundle: Bundle) {
    val action = currentDestination
        ?.getAction(actionId)
        ?: graph.getAction(actionId)

    if (action != null) {
        navigate(actionId, bundle)
    }
}

fun cleanCurrencyFormat(cash: String): BigDecimal {
    val clean = cash.replace("[^\\d.]".toRegex(), "")
    return if (cash.isEmpty()) BigDecimal(0.0) else clean.toBigDecimal()
}

fun cleanCurrencyDoubleFormat(cash: String): Double {
    val clean = cash.replace("[^\\d.]".toRegex(), "")
    return if (cash.isEmpty()) 0.0 else clean.toDouble()
}
