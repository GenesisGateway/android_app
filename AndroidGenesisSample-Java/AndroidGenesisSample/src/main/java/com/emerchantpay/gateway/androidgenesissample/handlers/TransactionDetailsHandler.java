package com.emerchantpay.gateway.androidgenesissample.handlers;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.emerchantpay.gateway.androidgenesissample.R;
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras;
import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.util.GenesisSharedPreferences;

import java.util.HashMap;
import java.util.UUID;

public class TransactionDetailsHandler {
    // String values
    private String transactionId;
    private String amount;
    private String customerEmail;
    private String customerPhone;
    private String firstname;
    private String lastname;
    private String address1;
    private String address2;
    private String zipcode;
    private String city;
    private String state;
    private String notificationUrl;
    private String currency;
    private String usage;
    private String country;
    private String transactionType;

    // UI controls
    private EditText transactionIdEditText;
    private EditText amountEditText;
    private EditText usageEditText;
    private EditText customerEmailEditText;
    private EditText customerPhoneEditText;
    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private EditText primaryAddressEditText;
    private EditText secondaryAddressEditText;
    private EditText zipCodeEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText notificationUrlEditText;
    private Spinner currenciesSpinner;
    private Spinner countriesSpinner;

    // Shared preferences
    private GenesisSharedPreferences sharedPreferences = new GenesisSharedPreferences();

    public void initUIControls(Activity activity) {
        // UI controls
        transactionIdEditText = (EditText) activity.findViewById(R.id.transactionIdText);
        amountEditText = (EditText) activity.findViewById(R.id.amountText);
        usageEditText = (EditText) activity.findViewById(R.id.usageText);
        customerEmailEditText = (EditText) activity.findViewById(R.id.customerEmailText);
        customerPhoneEditText = (EditText) activity.findViewById(R.id.customerPhoneText);
        firstnameEditText = (EditText) activity.findViewById(R.id.firstnameText);
        lastnameEditText = (EditText) activity.findViewById(R.id.lastnameText);
        primaryAddressEditText = (EditText) activity.findViewById(R.id.primaryAddressText);
        secondaryAddressEditText = (EditText) activity.findViewById(R.id.secondaryAddressText);
        zipCodeEditText = (EditText) activity.findViewById(R.id.zipcodeText);
        cityEditText = (EditText) activity.findViewById(R.id.cityText);
        stateEditText = (EditText) activity.findViewById(R.id.stateEditText);
        notificationUrlEditText = (EditText) activity.findViewById(R.id.notificationUrlText);
        currenciesSpinner = (Spinner) activity.findViewById(R.id.currencySpinner);
        countriesSpinner = (Spinner) activity.findViewById(R.id.countrySpinner);
    }

    public void setUIParams(Activity activity) {
        initUIControls(activity);
        // Load UI params
        transactionType = activity.getIntent().getExtras()
                .getString(IntentExtras.EXTRA_TRANSACTION_TYPE);

        transactionId = UUID.randomUUID().toString();

        transactionIdEditText.setText(transactionId);

        // Get shared preferences
        String amount = sharedPreferences.getString(activity, SharedPrefConstants.AMOUNT_KEY);

        // Amount check
        if (amount != null && !amount.isEmpty()) {
            amountEditText.setText(amount);
        }

        usageEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.USAGE_KEY));
        customerEmailEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.CUSTOMER_EMAIL_KEY));
        customerPhoneEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.CUSTOMER_PHONE_KEY));
        firstnameEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.FIRSTNAME_KEY));
        lastnameEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.LASTNAME_KEY));
        primaryAddressEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.ADDRESS1_KEY));
        secondaryAddressEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.ADDRESS2_KEY));
        zipCodeEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.ZIPCODE_KEY));
        cityEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.CITY_KEY));
        stateEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.STATE_KEY));
        notificationUrlEditText.setText(sharedPreferences.getString(activity, SharedPrefConstants.NOTIFICATIONURL_KEY));
    }

    public void getUIParams() {
        // Get String values from UI
        transactionId = transactionIdEditText.getText().toString();
        amount = amountEditText.getText().toString();
        currency = currenciesSpinner.getSelectedItem().toString();
        usage = usageEditText.getText().toString();
        country = countriesSpinner.getSelectedItem().toString();
        customerEmail = customerEmailEditText.getText().toString();
        customerPhone = customerPhoneEditText.getText().toString();
        firstname = firstnameEditText.getText().toString();
        lastname = lastnameEditText.getText().toString();
        address1 = primaryAddressEditText.getText().toString();
        address2 = secondaryAddressEditText.getText().toString();
        state = stateEditText.getText().toString();
        zipcode = zipCodeEditText.getText().toString();
        city = cityEditText.getText().toString();
        notificationUrl = notificationUrlEditText.getText().toString();
    }

    public void generateNewTransactionId() {
        transactionId = UUID.randomUUID().toString();
        transactionIdEditText.setText(transactionId);
    }

    public void addSpinners(Context context, HashMap<String, ArrayAdapter<String>> adapterMap) throws IllegalAccessException {
        for (String key : adapterMap.keySet()) {
            switch (key) {
                case "currency":
                    currenciesSpinner.setAdapter(adapterMap.get(key));
                    break;
                case "country":
                    countriesSpinner.setAdapter(adapterMap.get(key));
                    break;
            }
        }

        String currencyCompareValue = sharedPreferences.getString(context, SharedPrefConstants.CURRENCY_KEY);
        String countryCompareValue = sharedPreferences.getString(context, SharedPrefConstants.COUNTRY_NAME_KEY);

        int spinnerPosition = currencyCompareValue != null && !currencyCompareValue.isEmpty()
                ? adapterMap.get("currency").getPosition(currencyCompareValue) :
                adapterMap.get("currency").getPosition(Currency.Companion.getUSD().getCurrency());

        // Currencies
        currenciesSpinner.setSelection(spinnerPosition);

        // Countries
        if (countryCompareValue != null && !countryCompareValue.isEmpty()) {
            spinnerPosition = adapterMap.get("country").getPosition(countryCompareValue);
            countriesSpinner.setSelection(spinnerPosition);
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUsage() {
        return usage;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getZipCode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }
}
