package com.emerchantpay.gateway.androidgenesissample.models


class TransactionTypes: SpinnerItem(transactionTypes) {

    companion object {
        internal val transactionTypes = mapOf(
            "Authorize" to "authorize",
            "Authorize 3D" to  "authorize3d",
            "Sale" to "sale",
            "Sale 3D" to "sale3d",
            "Paysafecard" to "paysafecard",
            "Google Pay" to "google_pay"
        )
    }
}
