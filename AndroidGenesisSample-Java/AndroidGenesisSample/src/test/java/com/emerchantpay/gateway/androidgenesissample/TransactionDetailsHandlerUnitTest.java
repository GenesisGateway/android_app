package com.emerchantpay.gateway.androidgenesissample;

import android.content.Context;
import android.test.mock.MockContext;

import com.emerchantpay.gateway.androidgenesissample.activities.TransactionDetailsActivity;
import com.emerchantpay.gateway.androidgenesissample.handlers.TransactionDetailsHandler;
import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest;
import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionDetailsHandlerUnitTest {
    private Context context;

    private TransactionDetailsHandler transactionDetails;
    private TransactionDetailsActivity transactionDetailsActivity;

    private PaymentRequest paymentRequest;

    private String transactionId;

    @Before
    public void setup() throws IllegalAccessException {
        context = new MockContext();
        transactionDetailsActivity = mock(TransactionDetailsActivity.class);
        transactionDetails = mock(TransactionDetailsHandler.class);

        transactionId = UUID.randomUUID().toString();


        // Create Billing PaymentAddress
        PaymentAddress billingAddress = new PaymentAddress("John", "Doe",
                "Fifth avenue 1", "Fifth avenue 1", "10000", "New York",
                "state", new Country().Companion.getUnitedStates());

        // Create Transaction types
        TransactionTypesRequest transactionTypes = new TransactionTypesRequest();
        transactionTypes.addTransaction(WPFTransactionTypes.SALE);

        // Init WPF API request
        paymentRequest = new PaymentRequest(context.getApplicationContext(), transactionId,
                new BigDecimal("2.00"), new Currency().Companion.getUSD(),
                "john@example.com", "+555555555", billingAddress,
                "https://example.com", transactionTypes);


        transactionDetails.setUIParams(transactionDetailsActivity);
        transactionDetails.getUIParams();
    }

    @Test
    public void testTransactionDetails() {
        when(transactionDetails.getTransactionType()).thenReturn(paymentRequest.getTransactionType());
        when(transactionDetails.getTransactionId()).thenReturn(paymentRequest.getTransactionId());
        when(transactionDetails.getAmount()).thenReturn(String.valueOf(paymentRequest.getAmount()));
        when(transactionDetails.getCurrency()).thenReturn(paymentRequest.getCurrency());
        when(transactionDetails.getCustomerEmail()).thenReturn(paymentRequest.getCustomerEmail());
        when(transactionDetails.getCustomerPhone()).thenReturn(paymentRequest.getCustomerPhone());
        when(transactionDetails.getAddress1()).thenReturn(paymentRequest.getPaymentAddress().getAddress1());
        when(transactionDetails.getCountry()).thenReturn(paymentRequest.getPaymentAddress().getCountryCode());
        when(transactionDetails.getNotificationUrl()).thenReturn(paymentRequest.getNotificationUrl());

        assertEquals(transactionDetails.getTransactionType(), "wpf_payment");
        assertEquals(transactionDetails.getTransactionId(), transactionId);
        assertEquals(transactionDetails.getAmount(), "2.00");
        assertEquals(transactionDetails.getCurrency(), new Currency().Companion.getUSD().getCurrency());
        assertEquals(transactionDetails.getCustomerEmail(), "john@example.com");
        assertEquals(transactionDetails.getCustomerPhone(), "+555555555");
        assertEquals(transactionDetails.getAddress1(), "Fifth avenue 1");
        assertEquals(transactionDetails.getCountry(), new Country().Companion.getUnitedStates().getCode());
        assertEquals(transactionDetails.getNotificationUrl(), "https://example.com");
    }
}
