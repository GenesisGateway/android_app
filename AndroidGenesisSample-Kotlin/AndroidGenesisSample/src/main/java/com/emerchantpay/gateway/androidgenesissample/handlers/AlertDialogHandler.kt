package com.emerchantpay.gateway.androidgenesissample.handlers

import android.app.AlertDialog
import android.content.Context

class AlertDialogHandler(context: Context, title: String, message: String) {

    private val dialogBuilder: AlertDialog.Builder
    private val alert: AlertDialog

    init {

        dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setNeutralButton("OK", null)

        alert = dialogBuilder.create()
    }

    fun show() {
        alert.show()
    }
}
