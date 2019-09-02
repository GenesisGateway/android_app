package com.emerchantpay.gateway.androidgenesissample.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

import com.emerchantpay.gateway.androidgenesissample.R
import com.emerchantpay.gateway.androidgenesissample.models.TransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras

class MainActivity : Activity() {

    private var privacyTextView: TextView? = null
    private var termsandconditionsTextView: TextView? = null

    private var transactionTypesListView: ListView? = null

    private val transactionTypes = TransactionTypes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadView()
    }

    override fun onResume() {
        super.onResume()

        loadView()
    }

    protected fun loadView() {
        privacyTextView = findViewById<View>(R.id.privacyPolicyLabel) as TextView
        termsandconditionsTextView = findViewById<View>(R.id.termsandconditionsLabel) as TextView

        transactionTypesListView = findViewById<View>(R.id.transactionTypesListView) as ListView

        privacyTextView!!.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resources
                    .getString(R.string.privacy_policy_url)))
            startActivity(browserIntent)
        }

        termsandconditionsTextView!!.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse(resources
                            .getString(R.string.terms_and_conditions_url)))
            startActivity(browserIntent)
        }

        // Load  transaction types to ListView
        var listAdapter: ArrayAdapter<String>? = null
        try {
            listAdapter = ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, transactionTypes.listNames)
        } catch (e: IllegalAccessException) {
            Log.e("Illigal Exception", e.toString())
        }

        transactionTypesListView!!.adapter = listAdapter

        val intentTransactionDetails = Intent(this, TransactionDetailsActivity::class.java)

        transactionTypesListView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            var itemValue: String? = null
            try {
                itemValue = transactionTypes.listValues[i]

                intentTransactionDetails.putExtra(IntentExtras.EXTRA_TRANSACTION_TYPE, itemValue)
                startActivity(intentTransactionDetails)
            } catch (e: IllegalAccessException) {
                Log.e("Illigal Exception", e.toString())
            }
        }
    }
}
