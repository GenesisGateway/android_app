package com.emerchantpay.gateway.androidgenesissample.models

open class SpinnerItem(private val map: Map<String, String>) {
    val names: List<String>
        @Throws(IllegalAccessException::class)
        get() = map.keys.sortedWith { value2, value1 -> value2.compareTo(value1) }

    val values: List<String>
        @Throws(IllegalAccessException::class)
        get() = map.values.sortedWith { value2, value1 -> value2.compareTo(value1) }

    fun valueFrom(name: String): String? = map[name]
}