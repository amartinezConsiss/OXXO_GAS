package com.example.oxxogas.ui.main.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.oxxogas.ui.resumeticket.interfaces.PrinterCallback
import com.zebra.sdk.comm.BluetoothConnection
import com.zebra.sdk.comm.Connection
import com.zebra.sdk.printer.ZebraPrinterFactory
import com.zebra.sdk.printer.PrinterStatus
import com.zebra.sdk.comm.ConnectionException

class ZQ320PrinterManager(
    private val macAddress: String,
    private val context: Context
) {

    private var connection: Connection? = null

    /**
     * Abre conexión Bluetooth
     */
    @Throws(ConnectionException::class)
    private fun openConnection() {
        if (connection?.isConnected == true) return
        connection = BluetoothConnection(macAddress)
        connection?.open()
    }

    /**
     * Cierra conexión
     */
    private fun closeConnection() {
        try {
            connection?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error cerrando conexión", e)
            Toast.makeText(context, "Error imprimiendo", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Valida estado de impresora (batería, papel, pausa)
     */
    @Throws(Exception::class)
    private fun validatePrinterStatus() {
        val printer = ZebraPrinterFactory.getInstance(connection)
        val status: PrinterStatus = printer.currentStatus

        when {
            status.isPaused -> throw Exception("La impresora está en pausa")
            status.isPaperOut -> throw Exception("La impresora no tiene papel")
            status.isHeadOpen -> throw Exception("Cabezal abierto")
            //status.isBatteryLow -> throw Exception("Batería baja")
        }
    }

    /**
     * Imprime un ZPL
     */
    @Throws(Exception::class)
    fun print(
        zpl: String,
        callback: PrinterCallback
    ) {
        try {
            openConnection()
            validatePrinterStatus()
            connection?.write(zpl.toByteArray(Charsets.UTF_8))
            Thread.sleep(1500)
            callback.onFinish()
        } finally {
            closeConnection()
        }
    }

    companion object {
        private const val TAG = "ZQ320Printer"
    }
}
