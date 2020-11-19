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
import com.emerchantpay.gateway.androidgenesissample.R
import com.emerchantpay.gateway.androidgenesissample.models.TransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras
import kotlinx.android.synthetic.main.content_main.*

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

    protected fun loadView() {
        transactionTypesListView = findViewById<View>(R.id.transactionTypesListView) as ListView

        privacy_policy?.setOnClickListener(this)
        terms_and_conditions?.setOnClickListener(this)

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

    override fun onClick(v: View) {
        var browserIntent: Intent?
        when (v.id) {
            R.id.privacy_policy -> {
                browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resources
                        .getString(R.string.privacy_policy_url)))
                startActivity(browserIntent)
            }
            R.id.terms_and_conditions -> {
                browserIntent = Intent(Intent.ACTION_VIEW,
                        Uri.parse(resources
                                .getString(R.string.terms_and_conditions_url)))
                startActivity(browserIntent)
            }
        }
    }
}
