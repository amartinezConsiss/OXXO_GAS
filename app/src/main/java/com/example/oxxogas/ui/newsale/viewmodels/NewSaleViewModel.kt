package com.example.oxxogas.ui.newsale.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.oxxogas.R
import com.example.oxxogas.domain.models.PetrolPumpsList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewSaleViewModel @Inject constructor() : ViewModel() {

    fun getPumpsData(context: Context): List<PetrolPumpsList> =
        listOf(
            PetrolPumpsList(
                petrolPumpId = 1,
                petrolPumpStatus = 1,
                petrolPumpNameStatus = getStatusPump(1, context)
            ),
            PetrolPumpsList(
                petrolPumpId = 2,
                petrolPumpStatus = 1,
                petrolPumpNameStatus = getStatusPump(1, context)
            ),
            PetrolPumpsList(
                petrolPumpId = 3,
                petrolPumpStatus = 2,
                petrolPumpNameStatus = getStatusPump(2, context)
            ),
            PetrolPumpsList(
                petrolPumpId = 4,
                petrolPumpStatus = 1,
                petrolPumpNameStatus = getStatusPump(1, context)
            ),
            PetrolPumpsList(
                petrolPumpId = 5,
                petrolPumpStatus = 3,
                petrolPumpNameStatus = getStatusPump(3, context)
            ),
            PetrolPumpsList(
                petrolPumpId = 6,
                petrolPumpStatus = 4,
                petrolPumpNameStatus = getStatusPump(4, context)
            )
        )

    private fun getStatusPump(statusPump: Int, context: Context): String {
        return when (statusPump) {
            1 -> context.getString(R.string.available)
            2 -> context.getString(R.string.error)
            3 -> context.getString(R.string.closed)
            4 -> context.getString(R.string.in_use)
            else -> context.getString(R.string.available)
        }

    }
}