package com.example.oxxogas.ui.main.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle

class Profile {
    private  val DW_PROFILE = "OxxoGass"

    fun createDataWedgeProfile(context: Context) {
        val profileConfig = Bundle().apply {
            putString("PROFILE_NAME", DW_PROFILE)
            putString("PROFILE_ENABLED", "true")
            putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST")

            putBundle("PLUGIN_CONFIG", Bundle().apply {
                putString("PLUGIN_NAME", "BARCODE")
                putString("RESET_CONFIG", "true")
                putBundle("PARAM_LIST", Bundle().apply {
                    putString("scanner_selection", "auto")
                })
            })

            putParcelableArray(
                "APP_LIST",
                arrayOf(Bundle().apply {
                    putString("PACKAGE_NAME", context.packageName)
                    putStringArray("ACTIVITY_LIST", arrayOf("*"))
                })
            )
        }

        val intent = Intent("com.symbol.datawedge.api.ACTION").apply {
            putExtra("com.symbol.datawedge.api.SET_CONFIG", profileConfig)
        }

        context.sendBroadcast(intent)
    }

    fun enableIntentOutput(context: Context) {
        val bundle = Bundle().apply {
            putString("PROFILE_NAME", DW_PROFILE)
            putString("CONFIG_MODE", "UPDATE")

            putBundle("PLUGIN_CONFIG", Bundle().apply {
                putString("PLUGIN_NAME", "INTENT")
                putString("RESET_CONFIG", "true")
                putBundle("PARAM_LIST", Bundle().apply {
                    putString("intent_output_enabled", "true")
                    putString("intent_action", "com.tuapp.SCAN")
                    putString("intent_delivery", "2") // Broadcast
                })
            })
        }

        val intent = Intent("com.symbol.datawedge.api.ACTION").apply {
            putExtra("com.symbol.datawedge.api.SET_CONFIG", bundle)
        }

        context.sendBroadcast(intent)
    }


}