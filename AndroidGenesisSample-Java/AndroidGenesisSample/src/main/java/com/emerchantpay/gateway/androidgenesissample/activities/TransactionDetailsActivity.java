package com.emerchantpay.gateway.androidgenesissample.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.emerchantpay.gateway.androidgenesissample.R;
import com.emerchantpay.gateway.androidgenesissample.handlers.AlertDialogHandler;
import com.emerchantpay.gateway.androidgenesissample.handlers.TransactionDetailsHandler;
import com.emerchantpay.gateway.genesisandroid.api.constants.Endpoints;
import com.emerchantpay.gateway.genesisandroid.api.constants.Environments;
import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages;
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras;
import com.emerchantpay.gateway.genesisandroid.api.constants.Locales;
import com.emerchantpay.gateway.genesisandroid.api.internal.Genesis;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response;
import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.models.DynamicDescriptorParams;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;
import com.emerchantpay.gateway.genesisandroid.api.models.RiskParams;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;

import java.math.BigDecimal;
import java.util.HashMap;

public class TransactionDetailsActivity extends Activity {

    // Alert dialog
    private AlertDialogHandler dialogHandler;

    // Genesis Handler error
    private GenesisError error;

    // Transaction details
    TransactionDetailsHandler transactionDetails = new TransactionDetailsHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction_details);

        // Load params to UI
        transactionDetails.setUIParams(this);

        try {
            // Load currencies and countries
            Currency currencyObject = new Currency();
            Country countryObject = new Country();

            ArrayAdapter<String> currenciesAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, currencyObject.getCurrencies());
            currenciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ArrayAdapter<String> countriesAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, countryObject.getCountryNames());
            countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Adapters Hash map
            HashMap<String, ArrayAdapter<String>> adapters = new HashMap<String, ArrayAdapter<String>>();
            adapters.put("currency", currenciesAdapter);
            adapters.put("country", countriesAdapter);

            // Add spinners
            transactionDetails.addSpinners(this, adapters);

        } catch (IllegalAccessException e) {
            Log.e("Illegal Exception", e.toString());
        }
    }

    public void loadPaymentView(View view) throws IllegalAccessException {
        // Get param values from UI
        transactionDetails.getUIParams();

        // Create configuration
        Configuration configuration = new Configuration("SET_YOUR_USERNAME",
                "SET_YOUR_PASSWORD",
                Environments.Companion.getSTAGING(), Endpoints.Companion.getEMERCHANTPAY(),
                Locales.getEN());

        configuration.setDebugMode(true);

        // Create Billing PaymentAddress
        PaymentAddress billingAddress = new PaymentAddress(transactionDetails.getFirstname(),
                transactionDetails.getLastname(), transactionDetails.getAddress1(), transactionDetails.getAddress2(),
                transactionDetails.getZipCode(), transactionDetails.getCity(), transactionDetails.getState(),
                new Country().getCountry(transactionDetails.getCountry()));

        // Create Transaction types
        TransactionTypesRequest transactionTypes = new TransactionTypesRequest();
        transactionTypes.addTransaction(transactionDetails.getTransactionType());

        // Risk params
        RiskParams riskParams = new RiskParams("1002547", "1DA53551-5C60-498C-9C18-8456BDBA74A9",
                "987-65-4320", "12-34-56-78-9A-BC", "123456",
                "emil@example.com", "+49301234567", "245.253.2.12",
                "10000000000", "1234", "100000000", "John",
                "Doe", "US", "test", "245.25 3.2.12",
                "test", "test123456", "Bin name",
                "+49301234567");

        // Dynamic descriptor params
        DynamicDescriptorParams dynamicDescriptorParams = new DynamicDescriptorParams("eMerchantPay Ltd",
                "Sofia");

        // Init WPF API request
        PaymentRequest paymentRequest = new PaymentRequest(this,
                transactionDetails.getTransactionId(), new BigDecimal(transactionDetails.getAmount()),
                new Currency().findCurrencyByName(transactionDetails.getCurrency()),
                transactionDetails.getCustomerEmail(), transactionDetails.getCustomerPhone(),
                billingAddress, transactionDetails.getNotificationUrl(), transactionTypes);

        // Additionaly params
        paymentRequest.setUsage(transactionDetails.getUsage());
        paymentRequest.setLifetime(60);

        paymentRequest.setRiskParams(riskParams);

        Genesis genesis = new Genesis(this, configuration, paymentRequest);

        if (!genesis.isConnected(this)) {
            dialogHandler = new AlertDialogHandler(this, "Error",
                    ErrorMessages.CONNECTION_ERROR);
            dialogHandler.show();
        } else if (genesis.isConnected(this) && genesis.isValidData()) {
            //Execute WPF API request
            genesis.push();

            // Get response
            Response response = genesis.getResponse();

            // Check if response isSuccess
            if (!response.isSuccess()) {
                // Get Error Handler
                error = response.getError();

                dialogHandler = new AlertDialogHandler(this, "Failure",
                        "Code: " + error.getCode() + "\nMessage: "
                                + error.getMessage());
                dialogHandler.show();
            }
        } else if (!genesis.isValidData()) {
            // Get Error Handler
            error = genesis.getError();

            String message = error.getMessage();
            String technicalMessage = error.getTechnicalMessage() != null && !error.getTechnicalMessage().isEmpty()
                    ? error.getTechnicalMessage() : "";

            dialogHandler = new AlertDialogHandler(this, "Invalid",
                    technicalMessage + " " + message);

            dialogHandler.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RESULT_FIRST_USER) {
            // Make sure the request was successful
            switch (resultCode) {
                case RESULT_OK:
                    String technicalMessage = data.getStringExtra(IntentExtras.EXTRA_RESULT);
                    if (technicalMessage != null) {
                        dialogHandler = new AlertDialogHandler(this, "Failure",
                                technicalMessage);
                    } else {
                        dialogHandler = new AlertDialogHandler(this, "Success",
                                "Success");
                    }
                    dialogHandler.show();
                    break;
                case RESULT_CANCELED:
                    dialogHandler = new AlertDialogHandler(this, "Cancel", "Cancel");
                    if (data != null) dialogHandler.show();
                    break;
            }
        }
    }
}
