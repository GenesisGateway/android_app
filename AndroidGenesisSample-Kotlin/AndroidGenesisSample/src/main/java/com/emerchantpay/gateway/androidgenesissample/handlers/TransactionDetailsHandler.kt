package com.emerchantpay.gateway.androidgenesissample.handlers

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import com.emerchantpay.gateway.androidgenesissample.R
import com.emerchantpay.gateway.androidgenesissample.definitions.ExtraFieldName
import com.emerchantpay.gateway.androidgenesissample.definitions.ExtraFieldValue
import com.emerchantpay.gateway.androidgenesissample.utils.LayoutManager
import com.emerchantpay.gateway.androidgenesissample.utils.ViewParams
import com.emerchantpay.gateway.androidgenesissample.utils.ViewType
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras
import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringAmountType
import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringFrequency
import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringInterval
import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringMode
import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringPaymentType
import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants
import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.constants.recurring.RecurringCategory
import com.emerchantpay.gateway.genesisandroid.api.constants.recurring.RecurringType
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.GooglePayAttributes.Companion.PAYMENT_SUBTYPE
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.definitions.GooglePayPaymentSubtype
import com.emerchantpay.gateway.genesisandroid.api.models.Country
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.util.GenesisSharedPreferences
import java.lang.IllegalArgumentException
import java.security.InvalidParameterException
import java.util.*

class TransactionDetailsHandler {
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
    var googlePayPaymentToken: String? = null
        private set
    var googlePayPaymentSubtype: GooglePayPaymentSubtype? = null
        private set
    var recurringMode: RecurringMode? = null
        private set
    var recurringPaymentType: RecurringPaymentType? = null
        private set
    var recurringAmountType: RecurringAmountType? = null
        private set
    var recurringInterval: RecurringInterval? = null
        private set
    var recurringFrequency: RecurringFrequency? = null
        private set
    var recurringType: RecurringType? = null
        private set
    var recurringCategory: RecurringCategory? = null
        private set
    var recurringReferenceNumber: String? = null
        private set
    var recurringMaxCount: String? = null
        private set
    var recurringMaxAmount: String? = null
        private set
    var recurringFirstDate: String? = null
        private set
    var recurringTimeOfDay: String? = null
        private set
    var recurringPeriod: String? = null
        private set
    var recurringValidated: Boolean? = null
        private set

    private var transactionIdEditText: EditText? = null
    private var extraTransactionDetails: LinearLayout? = null
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
    private val extraTransactionFields = mutableMapOf<ExtraFieldName, View>()

    private val sharedPreferences = GenesisSharedPreferences()

    private fun initUIControls(activity: Activity) {
        transactionIdEditText = activity.findViewById(R.id.transactionIdText)
        extraTransactionDetails = activity.findViewById(R.id.extra_transaction_details)
        amountEditText = activity.findViewById(R.id.amountText)
        usageEditText = activity.findViewById(R.id.usageText)
        customerEmailEditText = activity.findViewById(R.id.customerEmailText)
        customerPhoneEditText = activity.findViewById(R.id.customerPhoneText)
        firstnameEditText = activity.findViewById(R.id.firstnameText)
        lastnameEditText = activity.findViewById(R.id.lastnameText)
        primaryAddressEditText = activity.findViewById(R.id.primaryAddressText)
        secondaryAddressEditText = activity.findViewById(R.id.secondaryAddressText)
        zipCodeEditText = activity.findViewById(R.id.zipcodeText)
        cityEditText = activity.findViewById(R.id.cityText)
        stateEditText = activity.findViewById(R.id.stateEditText)
        notificationUrlEditText = activity.findViewById(R.id.notificationUrlText)
        currenciesSpinner = activity.findViewById(R.id.currencySpinner)
        countriesSpinner = activity.findViewById(R.id.countrySpinner)
    }

    fun setFieldsValues(activity: Activity) {
        initUIControls(activity)

        transactionType = activity.intent.extras?.getString(IntentExtras.EXTRA_TRANSACTION_TYPE)
        manageExtraTransactionDetails(activity, transactionType)
        transactionId = UUID.randomUUID().toString()
        transactionIdEditText?.setText(transactionId)

        val amount = sharedPreferences.getString(activity, SharedPrefConstants.AMOUNT_KEY) ?: ""

        if (amount.isNotBlank()) 
            amountEditText?.setText(amount)

        usageEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.USAGE_KEY))
        customerEmailEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.CUSTOMER_EMAIL_KEY))
        customerPhoneEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.CUSTOMER_PHONE_KEY))
        firstnameEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.FIRSTNAME_KEY))
        lastnameEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.LASTNAME_KEY))
        primaryAddressEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.ADDRESS1_KEY))
        secondaryAddressEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.ADDRESS2_KEY))
        zipCodeEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.ZIPCODE_KEY))
        cityEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.CITY_KEY))
        stateEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.STATE_KEY))
        notificationUrlEditText?.setText(sharedPreferences.getString(activity, SharedPrefConstants.NOTIFICATIONURL_KEY))

        setSpinnersValues(activity)
    }

    private fun setSpinnersValues(context: Context) {
        val preservedSelectedCurrency = sharedPreferences.getString(context, SharedPrefConstants.CURRENCY_KEY) ?: Currency.USD.currency
        val preservedSelectedCountry = sharedPreferences.getString(context, SharedPrefConstants.COUNTRY_NAME_KEY) ?: ""

        ArrayAdapter(context,
            android.R.layout.simple_spinner_item, Currency().currencies).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            currenciesSpinner?.adapter = this

            if (preservedSelectedCurrency.isNotBlank())
                currenciesSpinner?.setSelection(this.getPosition(preservedSelectedCurrency))
        }

        ArrayAdapter(context,
            android.R.layout.simple_spinner_item, Country().countryNames).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            countriesSpinner?.adapter = this

            if (preservedSelectedCountry.isNotBlank())
                countriesSpinner?.setSelection(this.getPosition(preservedSelectedCountry))
        }

        setExtraSpinnersValues(context)
    }

    private fun setExtraSpinnersValues(context: Context) {
        when (transactionType) {
            GOOGLE_PAY_TRANSACTION -> {
                ArrayAdapter(context, android.R.layout.simple_spinner_item,
                    GooglePayPaymentSubtype.values().map { it.toString().uppercase() }).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (extraTransactionFields.contains(GOOGLE_PAY_PAYMENT_SUBTYPE))
                            (extraTransactionFields[GOOGLE_PAY_PAYMENT_SUBTYPE] as? Spinner)?.adapter = this
                }
            }
        }
        when (transactionType) {
            WPFTransactionTypes.AUTHORIZE.value,
            WPFTransactionTypes.AUTHORIZE3D.value,
            WPFTransactionTypes.SALE.value,
            WPFTransactionTypes.SALE3D.value -> {
                ArrayAdapter(context, android.R.layout.simple_spinner_item,
                    listOf(context.getString(R.string.recurring_mode_hint)) + RecurringMode.values().map { it.toString().uppercase() } ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (extraTransactionFields.contains(MANAGING_RECURRING_MODE))
                        (extraTransactionFields[MANAGING_RECURRING_MODE] as? Spinner)?.adapter = this
                }
                ArrayAdapter(context, android.R.layout.simple_spinner_item,
                    listOf(context.getString(R.string.recurring_payment_type_hint)) + RecurringPaymentType.values().map { it.toString().uppercase() }).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (extraTransactionFields.contains(RECURRING_PAYMENT_TYPE))
                        (extraTransactionFields[RECURRING_PAYMENT_TYPE] as? Spinner)?.adapter = this
                }
                ArrayAdapter(context, android.R.layout.simple_spinner_item,
                    listOf(context.getString(R.string.recurring_amount_type_hint)) + RecurringAmountType.values().map { it.toString().uppercase() }).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (extraTransactionFields.contains(RECURRING_AMOUNT_TYPE))
                        (extraTransactionFields[RECURRING_AMOUNT_TYPE] as? Spinner)?.adapter = this
                }
                ArrayAdapter(context, android.R.layout.simple_spinner_item,
                    listOf(context.getString(R.string.recurring_interval_hint)) + RecurringInterval.values().map { it.toString().uppercase() }).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (extraTransactionFields.contains(RECURRING_INTERVAL))
                        (extraTransactionFields[RECURRING_INTERVAL] as? Spinner)?.adapter = this
                }
                ArrayAdapter(context, android.R.layout.simple_spinner_item,
                    listOf(context.getString(R.string.recurring_frequency_hint)) + RecurringFrequency.values().map { it.toString().uppercase() }).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (extraTransactionFields.contains(RECURRING_FREQUENCY))
                        (extraTransactionFields[RECURRING_FREQUENCY] as? Spinner)?.adapter = this
                }
                ArrayAdapter(context, android.R.layout.simple_spinner_item,
                    listOf(context.getString(R.string.recurring_type_hint)) + RecurringType.values().map { it.toString().uppercase() }).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (extraTransactionFields.contains(RECURRING_TYPE))
                        (extraTransactionFields[RECURRING_TYPE] as? Spinner)?.adapter = this
                }
                ArrayAdapter(context, android.R.layout.simple_spinner_item,
                    listOf(context.getString(R.string.recurring_category_hint)) + RecurringCategory.values().map { it.toString().uppercase() }).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (extraTransactionFields.contains(RECURRING_CATEGORY))
                        (extraTransactionFields[RECURRING_CATEGORY] as? Spinner)?.adapter = this
                }
                ArrayAdapter(context, android.R.layout.simple_spinner_item,
                    mapOf(context.getString(R.string.recurring_validated_hint) to "none", true to "yes", false to "no").values.map { it.uppercase() }).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (extraTransactionFields.contains(RECURRING_VALIDATED))
                        (extraTransactionFields[RECURRING_VALIDATED] as? Spinner)?.adapter = this
                }
            }
        }
    }

    private fun manageExtraTransactionDetails(context: Context, transactionType: String?) {
        when (transactionType) {
            GOOGLE_PAY_TRANSACTION -> {
                extraTransactionDetails?.visibility = View.VISIBLE
                createGooglePayFields(context)
            }
        }
        when (transactionType) {
            WPFTransactionTypes.AUTHORIZE.value,
            WPFTransactionTypes.AUTHORIZE3D.value,
            WPFTransactionTypes.SALE.value,
            WPFTransactionTypes.SALE3D.value -> {
                extraTransactionDetails?.visibility = View.VISIBLE
                createRecurringFields(context)
            }
        }
    }

    private fun createGooglePayFields(context: Context) {
        LayoutManager.createView(context, ViewType.TEXTABLE, ViewParams("Payment subtype"))
            .apply { extraTransactionDetails?.addView(this) }
        LayoutManager.createView(context, ViewType.SELECTABLE)
            .apply {
                extraTransactionFields[GOOGLE_PAY_PAYMENT_SUBTYPE] = this
                extraTransactionDetails?.addView(this)
            }
    }

    private fun createRecurringFields(context: Context) {
        LayoutManager.createView(context, ViewType.SELECTABLE)
            .apply {
                extraTransactionFields[MANAGING_RECURRING_MODE] = this
                extraTransactionDetails?.addView(this)
            }
        LayoutManager.createView(context, ViewType.SELECTABLE)
            .apply {
                extraTransactionFields[RECURRING_PAYMENT_TYPE] = this
                extraTransactionDetails?.addView(this)
            }
        LayoutManager.createView(context, ViewType.SELECTABLE)
            .apply {
                extraTransactionFields[RECURRING_AMOUNT_TYPE] = this
                extraTransactionDetails?.addView(this)
            }
        LayoutManager.createView(context, ViewType.SELECTABLE)
            .apply {
                extraTransactionFields[RECURRING_FREQUENCY] = this
                extraTransactionDetails?.addView(this)
            }
        LayoutManager.createView(context, ViewType.SELECTABLE)
            .apply {
                extraTransactionFields[RECURRING_INTERVAL] = this
                extraTransactionDetails?.addView(this)
            }
        LayoutManager.createView(context, ViewType.EDITABLE, ViewParams(context.getString(R.string.recurring_first_date_hint)))
            .apply {
                val text: EditText = this as EditText
                text.hint = context.getString(R.string.recurring_first_date_hint)
                text.inputType = InputType.TYPE_CLASS_DATETIME
                extraTransactionFields[RECURRING_FIRST_DATE] = text
                extraTransactionDetails?.addView(text)
            }
        LayoutManager.createView(context, ViewType.EDITABLE, ViewParams(context.getString(R.string.recurring_time_of_day_hint)))
            .apply {
                val text: EditText = this as EditText
                text.hint = context.getString(R.string.recurring_time_of_day_hint)
                text.inputType = InputType.TYPE_CLASS_NUMBER
                extraTransactionFields[RECURRING_TIME_OF_DAY] = text
                extraTransactionDetails?.addView(text)
            }
        LayoutManager.createView(context, ViewType.EDITABLE, ViewParams(context.getString(R.string.recurring_period_hint)))
            .apply {
                val text: EditText = this as EditText
                text.hint = context.getString(R.string.recurring_period_hint)
                text.inputType = InputType.TYPE_CLASS_NUMBER
                extraTransactionFields[RECURRING_PERIOD] = text
                extraTransactionDetails?.addView(text)
            }
        LayoutManager.createView(context, ViewType.EDITABLE, ViewParams(context.getString(R.string.recurring_reference_type_hint)))
            .apply {
                val text: EditText = this as EditText
                text.hint = context.getString(R.string.recurring_reference_type_hint)
                text.inputType = InputType.TYPE_CLASS_NUMBER
                extraTransactionFields[RECURRING_REFERENCE_NUMBER] = text
                extraTransactionDetails?.addView(text)
            }
        LayoutManager.createView(context, ViewType.EDITABLE, ViewParams(context.getString(R.string.recurring_max_count_hint)))
            .apply {
                val text: EditText = this as EditText
                text.hint = context.getString(R.string.recurring_max_count_hint)
                text.inputType = InputType.TYPE_CLASS_NUMBER
                extraTransactionFields[RECURRING_MAX_COUNT] = text
                extraTransactionDetails?.addView(text)
            }
        LayoutManager.createView(context, ViewType.EDITABLE, ViewParams(context.getString(R.string.recurring_max_amount_hint)))
            .apply {
                val text: EditText = this as EditText
                text.hint = context.getString(R.string.recurring_max_amount_hint)
                text.inputType = InputType.TYPE_CLASS_NUMBER
                extraTransactionFields[RECURRING_MAX_AMOUNT] = text
                extraTransactionDetails?.addView(text)
            }
        LayoutManager.createView(context, ViewType.SELECTABLE)
            .apply {
                extraTransactionFields[RECURRING_TYPE] = this
                extraTransactionDetails?.addView(this)
            }
        LayoutManager.createView(context, ViewType.SELECTABLE)
            .apply {
                extraTransactionFields[RECURRING_CATEGORY] = this
                extraTransactionDetails?.addView(this)
            }
        LayoutManager.createView(context, ViewType.SELECTABLE)
            .apply {
                extraTransactionFields[RECURRING_VALIDATED] = this
                extraTransactionDetails?.addView(this)
            }
    }

    fun getFieldsValues() {
        transactionId = transactionIdEditText?.text.toString()
        amount = amountEditText?.text.toString()
        currency = currenciesSpinner?.selectedItem.toString()
        usage = usageEditText?.text.toString()
        country = countriesSpinner?.selectedItem.toString()
        customerEmail = customerEmailEditText?.text.toString()
        customerPhone = customerPhoneEditText?.text.toString()
        firstname = firstnameEditText?.text.toString()
        lastname = lastnameEditText?.text.toString()
        address1 = primaryAddressEditText?.text.toString()
        address2 = secondaryAddressEditText?.text.toString()
        state = stateEditText?.text.toString()
        zipCode = zipCodeEditText?.text.toString()
        city = cityEditText?.text.toString()
        notificationUrl = notificationUrlEditText?.text.toString()

        fillExtraFieldsData()
    }

    private fun fillExtraFieldsData() {
        when (transactionType) {
            GOOGLE_PAY_TRANSACTION -> fillGooglePayTransactionFields()
        }
        when (transactionType) {
            WPFTransactionTypes.AUTHORIZE.value,
            WPFTransactionTypes.AUTHORIZE3D.value,
            WPFTransactionTypes.SALE.value,
            WPFTransactionTypes.SALE3D.value -> fillRecurringTransactionFields()
        }
    }

    private fun fillGooglePayTransactionFields() {
        val extraTransactionData = extraTransactionFields.entries.associate {
            it.key.removePrefix("${GOOGLE_PAY_TRANSACTION}${UNDERSCORE}") to getExtraFieldValue(it.value)
        }

        extraTransactionData.entries.firstOrNull{it.key == PAYMENT_SUBTYPE }?.let { googlePayPaymentSubtype = GooglePayPaymentSubtype.valueOf(it.value) }
    }

    private fun fillRecurringTransactionFields() {
        val extraTransactionData = extraTransactionFields.entries.associate {
            it.key.removePrefix("${RECURRING_TRANSACTION}${UNDERSCORE}") to getExtraFieldValue(it.value)
        }

        recurringMode = try {
            extraTransactionData?.get(MANAGING_RECURRING_MODE)?.let { RecurringMode.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            Log.e(ILLEGAL_ARGUMENT_EXCEPTION_TAG, e.toString())
            null
        }
        recurringPaymentType = try {
            extraTransactionData?.get(RECURRING_PAYMENT_TYPE)?.let { RecurringPaymentType.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            Log.e(ILLEGAL_ARGUMENT_EXCEPTION_TAG, e.toString())
            null
        }
        recurringAmountType = try {
            extraTransactionData?.get(RECURRING_AMOUNT_TYPE)?.let { RecurringAmountType.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            Log.e(ILLEGAL_ARGUMENT_EXCEPTION_TAG, e.toString())
            null
        }
        recurringFrequency = try {
            extraTransactionData?.get(RECURRING_FREQUENCY)?.let { RecurringFrequency.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            Log.e(ILLEGAL_ARGUMENT_EXCEPTION_TAG, e.toString())
            null
        }
        recurringInterval = try {
            extraTransactionData?.get(RECURRING_INTERVAL)?.let { RecurringInterval.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            Log.e(ILLEGAL_ARGUMENT_EXCEPTION_TAG, e.toString())
            null
        }
        recurringReferenceNumber = extraTransactionData?.get(RECURRING_REFERENCE_NUMBER)?.let { it }
        recurringFirstDate = extraTransactionData?.get(RECURRING_FIRST_DATE)?.let { it }
        recurringPeriod = extraTransactionData?.get(RECURRING_PERIOD)?.let { it }
        recurringTimeOfDay = extraTransactionData?.get(RECURRING_TIME_OF_DAY)?.let { it }
        recurringMaxCount = extraTransactionData?.get(RECURRING_MAX_COUNT)?.let { it }
        recurringMaxAmount = extraTransactionData?.get(RECURRING_MAX_AMOUNT)?.let { it }
        recurringType = try {
            extraTransactionData?.get(RECURRING_TYPE)?.let { RecurringType.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            resetSpinnerValue(e) as RecurringType
        }
        recurringCategory = try {
            extraTransactionData?.get(RECURRING_CATEGORY)?.let { RecurringCategory.valueOf(it) }
        } catch (e: IllegalArgumentException) {
           resetSpinnerValue(e) as RecurringCategory
        }
        recurringValidated = try {
            extraTransactionData?.get(RECURRING_VALIDATED)?.let { it.toBoolean() }
        } catch (e: IllegalArgumentException) {
            resetSpinnerValue(e).toString().toBoolean()
        }
    }

    private fun resetSpinnerValue(e: Exception): Any? {
        Log.e("Illegal Argument exception", e.toString())
        return null
    }

    private fun getExtraFieldValue(view: View): ExtraFieldValue {
        return when (view) {
            is EditText -> view.text.toString()
            is Spinner -> view.selectedItem.toString()
            else -> throw InvalidParameterException()
        }
    }

    fun generateNewTransactionId() {
        transactionId = UUID.randomUUID().toString()
        transactionIdEditText?.setText(transactionId)
    }

    companion object {
        const val GOOGLE_PAY_TRANSACTION = "google_pay"
        const val RECURRING_TRANSACTION = "recurring"
        private const val UNDERSCORE = "_"
        private const val GOOGLE_PAY_PAYMENT_TOKEN = "${GOOGLE_PAY_TRANSACTION}${UNDERSCORE}payment_token"
        private const val GOOGLE_PAY_PAYMENT_SUBTYPE = "${GOOGLE_PAY_TRANSACTION}${UNDERSCORE}payment_subtype"
        private const val MANAGING_RECURRING_MODE = "mode"
        private const val RECURRING_PAYMENT_TYPE = "payment_type"
        private const val RECURRING_AMOUNT_TYPE = "amount_type"
        private const val RECURRING_INTERVAL = "interval"
        private const val RECURRING_FREQUENCY = "frequency"
        private const val RECURRING_FIRST_DATE = "first_date"
        private const val RECURRING_TIME_OF_DAY = "time_of_day"
        private const val RECURRING_PERIOD = "period"
        private const val RECURRING_REFERENCE_NUMBER = "registration_reference_number"
        private const val RECURRING_MAX_COUNT = "max_count"
        private const val RECURRING_MAX_AMOUNT = "max_amount"
        private const val RECURRING_TYPE = "type"
        private const val RECURRING_CATEGORY = "category"
        private const val RECURRING_VALIDATED = "validated"

        private const val ILLEGAL_ARGUMENT_EXCEPTION_TAG = "Illegal Argument exception"
    }
}
