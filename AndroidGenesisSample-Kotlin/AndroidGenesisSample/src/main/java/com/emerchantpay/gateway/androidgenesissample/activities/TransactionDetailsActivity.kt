package com.emerchantpay.gateway.androidgenesissample.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.emerchantpay.gateway.androidgenesissample.R
import com.emerchantpay.gateway.androidgenesissample.handlers.AlertDialogHandler
import com.emerchantpay.gateway.androidgenesissample.handlers.TransactionDetailsHandler
import com.emerchantpay.gateway.genesisandroid.api.constants.Endpoints
import com.emerchantpay.gateway.genesisandroid.api.constants.Environments
import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras.EXTRA_RESULT
import com.emerchantpay.gateway.genesisandroid.api.constants.Locales
import com.emerchantpay.gateway.genesisandroid.api.internal.Genesis
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest
import com.emerchantpay.gateway.genesisandroid.api.models.*
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration
import java.math.BigDecimal
import java.util.*

class TransactionDetailsActivity : Activity() {

    // Alert dialog
    private var dialogHandler: AlertDialogHandler? = null

    // Genesis Handler error
    private var error: GenesisError? = null

    // Transaction details
    internal var transactionDetails = TransactionDetailsHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_transaction_details)

        // Load params to UI
        transactionDetails.setUIParams(this)

        try {
            // Load currencies and countries
            val currencyObject = Currency()
            val countryObject = Country()

            val currenciesAdapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, currencyObject.currencies)
            currenciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            val countriesAdapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, countryObject.countryNames)
            countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Adapters Hash map
            val adapters = HashMap<String, ArrayAdapter<String>>()
            adapters["currency"] = currenciesAdapter
            adapters["country"] = countriesAdapter

            // Add spinners
            transactionDetails.addSpinners(this, adapters)

        } catch (e: IllegalAccessException) {
            Log.e("Illegal Exception", e.toString())
        }

    }

    @Throws(IllegalAccessException::class)
    fun loadPaymentView(view: View) {
        // Get param values from UI
        transactionDetails.getUIParams()

        // Create configuration
        val configuration = Configuration("SET_YOUR_USERNAME",
                "SET_YOUR_PASSWORD",
                Environments.STAGING, Endpoints.EMERCHANTPAY,
                Locales.EN)

        configuration.setDebugMode(true)

        // Create Billing PaymentAddress
        val billingAddress = PaymentAddress(transactionDetails.firstname!!,
                transactionDetails.lastname!!, transactionDetails.address1!!, transactionDetails.address2!!,
                transactionDetails.zipCode!!, transactionDetails.city!!, transactionDetails.state!!,
                Country().getCountry(transactionDetails.country!!)!!)

        // Create Transaction types
        val transactionTypes = TransactionTypesRequest()
        transactionDetails.transactionType?.let { transactionTypes.addTransaction(it) }

        // Risk params
        val riskParams = RiskParams("1002547", "1DA53551-5C60-498C-9C18-8456BDBA74A9",
                "987-65-4320", "12-34-56-78-9A-BC", "123456",
                "emil@example.com", "+49301234567", "245.253.2.12",
                "10000000000", "1234", "100000000", "John",
                "Doe", "US", "test", "245.25 3.2.12",
                "test", "test123456", "Bin name",
                "+49301234567")

        // Dynamic descriptor params
        val dynamicDescriptorParams = DynamicDescriptorParams("eMerchantPay Ltd",
                "Sofia")

        // Init WPF API request
        val paymentRequest = transactionDetails.currency?.let {
            Currency().findCurrencyByName(it)?.let { it ->
                PaymentRequest(this,
                        transactionDetails.transactionId, BigDecimal(transactionDetails.amount),
                        it,
                        transactionDetails.customerEmail, transactionDetails.customerPhone,
                        billingAddress, transactionDetails.notificationUrl, transactionTypes)
            }
        }

        // Additionaly params
        transactionDetails?.usage?.let { paymentRequest?.setUsage(it) }
        paymentRequest?.setLifetime(60)

        paymentRequest?.setRiskParams(riskParams)

        val genesis = paymentRequest?.let { Genesis(this, configuration, it) }

        when {
            !genesis?.isConnected(this)!! -> {
                dialogHandler = AlertDialogHandler(this, "Error",
                        ErrorMessages.CONNECTION_ERROR)
                dialogHandler!!.show()
            }
            genesis.isConnected(this)!! && genesis?.isValidData!! -> {
                //Execute WPF API request
                genesis.push()

                // Get response
                val response = genesis?.response

                // Check if response isSuccess
                when {
                    !response?.isSuccess!! -> {
                        // Get Error Handler
                        error = response.error

                        dialogHandler = AlertDialogHandler(this, "Failure",
                                "Code: " + error!!.code + "\nMessage: "
                                        + error!!.message)
                        dialogHandler!!.show()
                    }
                }
            }
            !genesis?.isValidData!! -> {
                // Get Error Handler
                error = genesis.error

                val message = error!!.message
                val technicalMessage = if (error!!.technicalMessage != null && error!!.technicalMessage!!.isNotEmpty()) error!!.technicalMessage else ""

                dialogHandler = AlertDialogHandler(this, "Invalid",
                        "$technicalMessage $message")

                dialogHandler!!.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        when (requestCode) {
            RESULT_FIRST_USER -> {
                // Make sure the request was successful
                when (resultCode) {
                    RESULT_OK -> {
                        val technicalMessage = data!!.getStringExtra(EXTRA_RESULT)
                        dialogHandler = if (technicalMessage != null) {
                            AlertDialogHandler(this, "Failure",
                                    technicalMessage)
                        } else {
                            AlertDialogHandler(this, "Success",
                                    "Success")
                        }
                        dialogHandler!!.show()
                    }
                    RESULT_CANCELED -> {
                        dialogHandler = AlertDialogHandler(this, "Cancel", "Cancel")
                        if (data != null) dialogHandler!!.show()
                    }
                }
            }
        }
    }
}
