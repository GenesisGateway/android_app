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
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2CardHolderAccountPasswordChangeIndicator;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2CardHolderAccountRegistrationIndicator;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2CardHolderAccountShippingAddressUsageIndicator;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2CardHolderAccountSuspiciousActivityIndicator;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2CardHolderAccountUpdateIndicator;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2MerchantRiskDeliveryTimeframe;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2MerchantRiskPreorderPurchaseIndicator;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2MerchantRiskReorderItemsIndicator;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2MerchantRiskShippingIndicator;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2PurchaseCategory;
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
import com.emerchantpay.gateway.genesisandroid.api.models.threedsv2.ThreeDsV2CardHolderAccountParams;
import com.emerchantpay.gateway.genesisandroid.api.models.threedsv2.ThreeDsV2MerchantRiskParams;
import com.emerchantpay.gateway.genesisandroid.api.models.threedsv2.ThreeDsV2Params;
import com.emerchantpay.gateway.genesisandroid.api.models.threedsv2.ThreeDsV2RecurringParams;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    @Override
    protected void onPostResume() {
        super.onPostResume();

        transactionDetails.generateNewTransactionId();
    }

    public void loadPaymentView(View view) throws IllegalAccessException, ParseException {
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

        // Dynamic descriptor params
        DynamicDescriptorParams dynamicDescriptorParams = new DynamicDescriptorParams("eMerchantPay Ltd",
                "Sofia");

        // Init WPF API request
        PaymentRequest paymentRequest = new PaymentRequest(this,
                transactionDetails.getTransactionId(), new BigDecimal(transactionDetails.getAmount()),
                new Currency().findCurrencyByName(transactionDetails.getCurrency()),
                transactionDetails.getCustomerEmail(), transactionDetails.getCustomerPhone(),
                billingAddress, transactionDetails.getNotificationUrl(), transactionTypes);

        // Additional params
        paymentRequest.setUsage(transactionDetails.getUsage());
        paymentRequest.setLifetime(60);

        // Risk params
        RiskParams riskParams = new RiskParams("1002547", "1DA53551-5C60-498C-9C18-8456BDBA74A9",
                "987-65-4320", "12-34-56-78-9A-BC", "123456",
                "emil@example.com", "+49301234567", "245.253.2.12",
                "10000000000", "1234", "100000000", "John",
                "Doe", "US", "test", "245.25 3.2.12",
                "test", "test123456", "Bin name",
                "+49301234567");

        paymentRequest.setRiskParams(riskParams);

        // 3DSv2 params
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 5);
        Date merchantRiskPreorderDate=calendar.getTime();

        // Create a builder
        ThreeDsV2Params.Builder threeDsV2ParamsBuilder = new ThreeDsV2Params.Builder();

        // Set 3DSv2 purchase params
        threeDsV2ParamsBuilder.setPurchaseCategory(ThreeDsV2PurchaseCategory.GOODS);

        // Set 3DSv2 merchant risk params
        ThreeDsV2MerchantRiskParams merchantRisk = new ThreeDsV2MerchantRiskParams(
                ThreeDsV2MerchantRiskShippingIndicator.DIGITAL_GOODS,
                ThreeDsV2MerchantRiskDeliveryTimeframe.SAME_DAY,
                ThreeDsV2MerchantRiskReorderItemsIndicator.REORDERED,
                ThreeDsV2MerchantRiskPreorderPurchaseIndicator.MERCHANDISE_AVAILABLE,
                merchantRiskPreorderDate,
                true,3);
        threeDsV2ParamsBuilder.setMerchantRisk(merchantRisk);

        // Set 3DSv2 card holder account params
        ThreeDsV2CardHolderAccountParams cardHolderAccount = new ThreeDsV2CardHolderAccountParams(
                new SimpleDateFormat("dd-MM-yyyy").parse("11-02-2021"),
                ThreeDsV2CardHolderAccountUpdateIndicator.UPDATE_30_TO_60_DAYS,
                new SimpleDateFormat("dd-MM-yyyy").parse("13-02-2021"),
                ThreeDsV2CardHolderAccountPasswordChangeIndicator.PASSWORD_CHANGE_NO_CHANGE,
                new SimpleDateFormat("dd-MM-yyyy").parse("10-01-2021"),
                ThreeDsV2CardHolderAccountShippingAddressUsageIndicator.ADDRESS_USAGE_MORE_THAN_60DAYS,
                new SimpleDateFormat("dd-MM-yyyy").parse("10-01-2021"),
                2,129,1,31,
                ThreeDsV2CardHolderAccountSuspiciousActivityIndicator.NO_SUSPICIOUS_OBSERVED,
                ThreeDsV2CardHolderAccountRegistrationIndicator.REGISTRATION_30_TO_60_DAYS,
                new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2021"));
        threeDsV2ParamsBuilder.setCardHolderAccount(cardHolderAccount);

        // Set 3DSv2 recurring params
        ThreeDsV2RecurringParams recurring = new ThreeDsV2RecurringParams();

        // Create an instance of ThreeDsV2Params via builder
        ThreeDsV2Params threeDsV2Params = threeDsV2ParamsBuilder.build();

        paymentRequest.setThreeDsV2Params(threeDsV2Params);
        // end of 3DSv2 Params

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
                                + error.getTechnicalMessage());
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
