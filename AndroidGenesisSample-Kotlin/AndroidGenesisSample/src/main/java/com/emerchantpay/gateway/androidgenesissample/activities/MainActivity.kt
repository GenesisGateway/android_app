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

class MainActivity : Activity(), View.OnClickListener {
    private var transactionTypesListView: ListView? = null
    private val transactionTypes = TransactionTypes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        loadView()
    }

    private fun loadView() {
        transactionTypesListView = findViewById<View>(R.id.transactionTypesListView) as ListView

        findViewById<TextView>(R.id.privacy_policy)?.setOnClickListener(this)
        findViewById<TextView>(R.id.terms_and_conditions)?.setOnClickListener(this)

        // Load  transaction types to ListView
        transactionTypesListView?.adapter =
            try {
                ArrayAdapter(this, android.R.layout.simple_list_item_1, transactionTypes.listNames)
        } catch (e: IllegalAccessException) {
            Log.e("Illegal Exception", e.toString())
                null
        }

        val intentTransactionDetails = Intent(this, TransactionDetailsActivity::class.java)

        transactionTypesListView!!.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            transactionTypes.listValues[i].apply {
                intentTransactionDetails.putExtra(IntentExtras.EXTRA_TRANSACTION_TYPE, this)
                startActivity(intentTransactionDetails)
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
