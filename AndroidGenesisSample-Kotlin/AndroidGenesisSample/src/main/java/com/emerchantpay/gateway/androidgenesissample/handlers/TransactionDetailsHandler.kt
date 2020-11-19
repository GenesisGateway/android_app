package com.emerchantpay.gateway.androidgenesissample.handlers

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

import com.emerchantpay.gateway.androidgenesissample.R
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras
import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.util.GenesisSharedPreferences

import java.util.HashMap
import java.util.UUID

class TransactionDetailsHandler {
    // String values
    var transactionId: String? = null
        private set
    var amount: String? = null
        private set
    var customerEmail: String? = null
        private set
    var customerPhone: String? = null
        private set
    var firstname: String? = null
        private set
    var lastname: String? = null
        private set
    var address1: String? = null
        private set
    var address2: String? = null
        private set
    var zipCode: String? = null
        private set
    var city: String? = null
        private set
    var state: String? = null
        private set
    var notificationUrl: String? = null
        private set
    var currency: String? = null
        private set
    var usage: String? = null
        private set
    var country: String? = null
        private set
    var transactionType: String? = null
        private set

    // UI controls
    private var transactionIdEditText: EditText? = null
    private var amountEditText: EditText? = null
    private var usageEditText: EditText? = null
    private var customerEmailEditText: EditText? = null
    private var customerPhoneEditText: EditText? = null
    private var firstnameEditText: EditText? = null
    private var lastnameEditText: EditText? = null
    private var primaryAddressEditText: EditText? = null
    private var secondaryAddressEditText: EditText? = null
    private var zipCodeEditText: EditText? = null
    private var cityEditText: EditText? = null
    private var stateEditText: EditText? = null
    private var notificationUrlEditText: EditText? = null
    private var currenciesSpinner: Spinner? = null
    private var countriesSpinner: Spinner? = null

    // Shared preferences
    private val sharedPreferences = GenesisSharedPreferences()

    fun initUIControls(activity: Activity) {
        // UI controls
        transactionIdEditText = activity.findViewById<View>(R.id.transactionIdText) as EditText
        amountEditText = activity.findViewById<View>(R.id.amountText) as EditText
        usageEditText = activity.findViewById<View>(R.id.usageText) as EditText
        customerEmailEditText = activity.findViewById<View>(R.id.customerEmailText) as EditText
        customerPhoneEditText = activity.findViewById<View>(R.id.customerPhoneText) as EditText
        firstnameEditText = activity.findViewById<View>(R.id.firstnameText) as EditText
        lastnameEditText = activity.findViewById<View>(R.id.lastnameText) as EditText
        primaryAddressEditText = activity.findViewById<View>(R.id.primaryAddressText) as EditText
        secondaryAddressEditText = activity.findViewById<View>(R.id.secondaryAddressText) as EditText
        zipCodeEditText = activity.findViewById<View>(R.id.zipcodeText) as EditText
        cityEditText = activity.findViewById<View>(R.id.cityText) as EditText
        stateEditText = activity.findViewById<View>(R.id.stateEditText) as EditText
        notificationUrlEditText = activity.findViewById<View>(R.id.notificationUrlText) as EditText
        currenciesSpinner = activity.findViewById<View>(R.id.currencySpinner) as Spinner
        countriesSpinner = activity.findViewById<View>(R.id.countrySpinner) as Spinner
    }

    fun setUIParams(activity: Activity) {
        initUIControls(activity)
        // Load UI params
        transactionType = activity.intent.extras!!
                .getString(IntentExtras.EXTRA_TRANSACTION_TYPE)

        transactionId = UUID.randomUUID().toString()

        transactionIdEditText!!.setText(transactionId)

        // Get shared preferences
        val amount = sharedPreferences.getString(activity, SharedPrefConstants.AMOUNT_KEY)

        // Amount check
        when {
            amount != null && !amount!!.isEmpty() -> amountEditText!!.setText(amount)
        }

        usageEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.USAGE_KEY))
        customerEmailEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.CUSTOMER_EMAIL_KEY))
        customerPhoneEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.CUSTOMER_PHONE_KEY))
        firstnameEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.FIRSTNAME_KEY))
        lastnameEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.LASTNAME_KEY))
        primaryAddressEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.ADDRESS1_KEY))
        secondaryAddressEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.ADDRESS2_KEY))
        zipCodeEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.ZIPCODE_KEY))
        cityEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.CITY_KEY))
        stateEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.STATE_KEY))
        notificationUrlEditText!!.setText(sharedPreferences.getString(activity, SharedPrefConstants.NOTIFICATIONURL_KEY))
    }

    fun getUIParams() {
        // Get String values from UI
        transactionId = transactionIdEditText!!.text.toString()
        amount = amountEditText!!.text.toString()
        currency = currenciesSpinner!!.selectedItem.toString()
        usage = usageEditText!!.text.toString()
        country = countriesSpinner!!.selectedItem.toString()
        customerEmail = customerEmailEditText!!.text.toString()
        customerPhone = customerPhoneEditText!!.text.toString()
        firstname = firstnameEditText!!.text.toString()
        lastname = lastnameEditText!!.text.toString()
        address1 = primaryAddressEditText!!.text.toString()
        address2 = secondaryAddressEditText!!.text.toString()
        state = stateEditText!!.text.toString()
        zipCode = zipCodeEditText!!.text.toString()
        city = cityEditText!!.text.toString()
        notificationUrl = notificationUrlEditText!!.text.toString()
    }

    @Throws(IllegalAccessException::class)
    fun addSpinners(context: Context, adapterMap: HashMap<String, ArrayAdapter<String>>) {
        for (key in adapterMap.keys) {
            if (key == "currency") {
                currenciesSpinner!!.adapter = adapterMap[key]
            } else if (key == "country") {
                countriesSpinner!!.adapter = adapterMap[key]
            }
        }

        val currencyCompareValue = sharedPreferences.getString(context, SharedPrefConstants.CURRENCY_KEY)
        val countryCompareValue = sharedPreferences.getString(context, SharedPrefConstants.COUNTRY_NAME_KEY)

        var spinnerPosition: Int

        // Currencies
        when {
            currencyCompareValue != null && !currencyCompareValue!!.isEmpty() -> {
                spinnerPosition = adapterMap["currency"]?.getPosition(currencyCompareValue)!!
                currenciesSpinner!!.setSelection(spinnerPosition)
            }
            else -> {
                spinnerPosition = adapterMap["currency"]?.getPosition(Currency.USD.currency)!!
                currenciesSpinner!!.setSelection(spinnerPosition)
            }
        }

        // Countries
        if (countryCompareValue != null && !countryCompareValue!!.isEmpty()) {
            spinnerPosition = adapterMap["country"]?.getPosition(countryCompareValue)!!
            countriesSpinner!!.setSelection(spinnerPosition)
        }
    }
}
