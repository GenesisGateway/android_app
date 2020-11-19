package com.emerchantpay.gateway.androidgenesissample.models

import java.util.ArrayList
import java.util.Comparator
import java.util.HashMap

class TransactionTypes {

    var displayName: String = ""
        private set(displayName) {
            field = displayName
        }
        get() = value
    var value: String = ""
        private set(value) {
            field = value
        }
        get() = value

    // Get Transaction type names list
    val listNames: ArrayList<String>
        @Throws(IllegalAccessException::class)
        get() {
            transactionTypes

            val arrayList = ArrayList<String>()
            arrayList.addAll(transactionTypesMap.keys)

            return sortArrayList(arrayList)
        }

    // Get Transaction type values list
    val listValues: ArrayList<String>
        @Throws(IllegalAccessException::class)
        get() {
            transactionTypes

            val arrayList = ArrayList<String>()

            for (value in transactionTypesMap.keys) {
                transactionTypesMap[value]?.let { arrayList.add(it) }
            }

            return sortArrayList(arrayList)
        }

    // Get Transaction types
    protected val transactionTypes: Map<String, String>
        get() = transactionTypesMap

    constructor() {

    }

    constructor(displayName: String, value: String) {
        this.displayName = displayName
        this.value = value
        transactionTypesMap[displayName] = value
    }

    // Sort Array list
    protected fun sortArrayList(list: ArrayList<String>): ArrayList<String> {
        list.sortWith(Comparator { value2, value1 -> value2.compareTo(value1) })

        return list
    }

    companion object {

        private val transactionTypesMap = HashMap<String, String>()

        var authorize = TransactionTypes("Authorize", "authorize")
        var authorize3d = TransactionTypes("Authorize 3D", "authorize3d")
        var sale = TransactionTypes("Sale", "sale")
        var sale3d = TransactionTypes("Sale 3D", "sale3d")
        var paysafecard = TransactionTypes("Paysafecard", "paysafecard")
    }
}
