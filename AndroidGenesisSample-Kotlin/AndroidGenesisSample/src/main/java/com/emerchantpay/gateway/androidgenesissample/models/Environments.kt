package com.emerchantpay.gateway.androidgenesissample.models

class Environments: SpinnerItem(environments) {

    companion object {
        private val environments = mapOf(
            "Staging" to "staging.gate",
            "Production" to "gate"
        )
    }
}