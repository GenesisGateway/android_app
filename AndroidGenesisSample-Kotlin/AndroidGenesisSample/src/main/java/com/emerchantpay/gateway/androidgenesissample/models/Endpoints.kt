package com.emerchantpay.gateway.androidgenesissample.models

class Endpoints: SpinnerItem(endpoints) {

    companion object {
        private val endpoints = mapOf(
            "emerchantpay" to "emerchantpay.net",
            "e-comprocessing" to  "e-comprocessing.net"
        )
    }
}