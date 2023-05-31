package com.emerchantpay.gateway.androidgenesissample.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.emerchantpay.gateway.androidgenesissample.R
import com.emerchantpay.gateway.androidgenesissample.models.TransactionTypes
import com.emerchantpay.gateway.androidgenesissample.constants.Extras
import com.emerchantpay.gateway.androidgenesissample.models.Endpoints
import com.emerchantpay.gateway.androidgenesissample.models.Environments
import com.emerchantpay.gateway.androidgenesissample.models.Languages
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras


class MainActivity : Activity(), View.OnClickListener {
    private var transactionTypesListView: ListView? = null
    private val transactionTypes = TransactionTypes()

    private var usernameEditText: EditText? = null
    private var passwordEditText: EditText? = null

    private var environmentSpinner: Spinner? = null
    private val environments = Environments()
    private var endpointSpinner: Spinner? = null
    private val endpoints = Endpoints()
    private var languageSpinner: Spinner? = null
    private val languages = Languages()
    private lateinit var selectedLanguage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadView()
    }

    private fun loadView() {
        transactionTypesListView = findViewById<View>(R.id.transactionTypesListView) as ListView
        usernameEditText = findViewById(R.id.usernameText)
        passwordEditText = findViewById(R.id.passwordText)
        environmentSpinner = findViewById(R.id.environmentSpinner)
        endpointSpinner = findViewById(R.id.endpointSpinner)
        languageSpinner = findViewById(R.id.languageSpinner)

        findViewById<TextView>(R.id.privacy_policy)?.setOnClickListener(this)
        findViewById<TextView>(R.id.terms_and_conditions)?.setOnClickListener(this)

        val languagesMap = HashMap<String, String>()
        for (i in 0 until languages.names.size)
            languagesMap[languages.names[i]] = languages.values[i]


        val environmentsAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, environments.names.sortedDescending())
        val endpointsAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, endpoints.names.sortedDescending())
        val languagesAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages.names)

        // Load Spinners
        environmentSpinner?.adapter = environmentsAdapter
        endpointSpinner?.adapter = endpointsAdapter
        languageSpinner?.adapter = languagesAdapter

        languageSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {
                val name: String = adapterView?.getItemAtPosition(position).toString()
                selectedLanguage = languagesMap[name].toString()
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {

            }
        }

        // Load  transaction types to ListView
        transactionTypesListView?.adapter =
            try {
                ArrayAdapter(this, android.R.layout.simple_list_item_1, transactionTypes.names)
        } catch (e: IllegalAccessException) {
            Log.e("Illegal Exception", e.toString())
                null
        }

        val intentTransactionDetails = Intent(this, TransactionDetailsActivity::class.java)

        transactionTypesListView!!.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            transactionTypes.values[i].apply {
                intentTransactionDetails.putExtra(Extras.EXTRA_USERNAME, usernameEditText?.text.toString())
                intentTransactionDetails.putExtra(Extras.EXTRA_PASSWORD, passwordEditText?.text.toString())
                intentTransactionDetails.putExtra(Extras.EXTRA_ENVIRONMENT, environments.valueFrom(environmentSpinner?.selectedItem?.toString() ?: ""))
                intentTransactionDetails.putExtra(Extras.EXTRA_ENDPOINT, endpoints.valueFrom(endpointSpinner?.selectedItem?.toString() ?: ""))
                intentTransactionDetails.putExtra(Extras.EXTRA_LANGUAGE, selectedLanguage)
                intentTransactionDetails.putExtra(IntentExtras.EXTRA_TRANSACTION_TYPE, this)
                when {
                    TextUtils.isEmpty(usernameEditText?.text) ->
                        usernameEditText?.error = getString(R.string.username_is_required)
                    TextUtils.isEmpty(passwordEditText?.text) ->
                        passwordEditText?.error = getString(R.string.password_is_required)
                    endpointSpinner?.selectedItem == null
                            || environmentSpinner?.selectedItem == null
                            || languageSpinner?.selectedItem == null ->
                        Toast.makeText(
                            applicationContext,getString(R.string.please_select_environment_endpoint_and_language),
                            Toast.LENGTH_SHORT
                        ).show()
                    else -> {
                        startActivity(intentTransactionDetails)
                    }
                }
            }
        }
    }

    override fun onClick(v: View) {
        val browserIntent = when (v.id) {
            R.id.privacy_policy ->
                Intent(Intent.ACTION_VIEW, Uri.parse(resources
                        .getString(R.string.privacy_policy_url)))

            R.id.terms_and_conditions ->
                Intent(Intent.ACTION_VIEW, Uri.parse(resources
                                .getString(R.string.terms_and_conditions_url)))

            else -> return
        }

        startActivity(browserIntent)
    }
}
