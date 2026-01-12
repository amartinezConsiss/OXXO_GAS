package com.example.oxxogas.ui.resumeticket.interfaces

interface PrinterCallback {
    fun onStart()
    fun onFinish()
    fun onError(message: String)
}
