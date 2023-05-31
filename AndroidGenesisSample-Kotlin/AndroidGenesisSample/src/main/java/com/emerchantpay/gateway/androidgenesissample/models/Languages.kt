package com.emerchantpay.gateway.androidgenesissample.models

class Languages: SpinnerItem(languages) {

    companion object {
        private val languages = mapOf(
            "Spanish" to "ar",
            "Bulgarian" to "bg",
            "German" to "de",
            "English" to "en",
            "Spanish" to "es",
            "French" to "fr",
            "Hindi" to "hi",
            "Japanese" to "ja",
            "Italian" to "it",
            "Portuguese" to "pt",
            "Russian" to "ru",
            "Turkish" to "tr",
            "Chinese" to "zh"
        )
    }
}