package com.emerchantpay.gateway.androidgenesissample

import android.content.Context

import com.emerchantpay.gateway.androidgenesissample.activities.TransactionDetailsActivity
import com.emerchantpay.gateway.androidgenesissample.handlers.TransactionDetailsHandler
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest
import com.emerchantpay.gateway.genesisandroid.api.models.Country
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress

import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertThat

import java.math.BigDecimal
import java.util.UUID

import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers
import org.mockito.Mockito.*

class TransactionDetailsHandlerUnitTest {
    private var context: Context? = null

    private var transactionDetails: TransactionDetailsHandler? = null
    private var transactionDetailsActivity: TransactionDetailsActivity? = null

    private var paymentRequest: PaymentRequest? = null

    private var transactionId: String? = null

    @Before
    @Throws(IllegalAccessException::class)
    fun setup() {
        context = mock(Context::class.java)
        transactionDetailsActivity = mock<TransactionDetailsActivity>(TransactionDetailsActivity::class.java)
        transactionDetails = mock<TransactionDetailsHandler>(TransactionDetailsHandler::class.java)

        transactionId = UUID.randomUUID().toString()


        // Create Billing PaymentAddress
        val billingAddress = PaymentAddress("John", "Doe",
                "Fifth avenue 1", "Fifth avenue 1", "10000", "New York",
                "state", Country.UnitedStates)

        // Create Transaction types
        val transactionTypes = TransactionTypesRequest()
        transactionTypes.addTransaction("sale")

        // Init WPF API request
        paymentRequest = PaymentRequest(context!!.applicationContext, transactionId,
                BigDecimal("2.00"), Currency.USD,
                "john@example.com", "+555555555", billingAddress,
                "https://example.com", transactionTypes)


        transactionDetails!!.setUIParams(transactionDetailsActivity!!)
        transactionDetails!!.getUIParams()
    }

    @Test
    fun testTransactionDetails() {
        `when`(transactionDetails!!.transactionType).thenReturn(paymentRequest!!.getTransactionType())
        `when`(transactionDetails!!.amount).thenReturn(paymentRequest!!.amount.toString())
        `when`(transactionDetails!!.currency).thenReturn(paymentRequest!!.currency)
        `when`(transactionDetails!!.customerEmail).thenReturn(paymentRequest!!.getConsumerId())
        `when`(transactionDetails!!.customerPhone).thenReturn(paymentRequest!!.getCustomerPhone())
        `when`(transactionDetails!!.address1).thenReturn(paymentRequest!!.getBillingAddress().toXML())
        `when`(transactionDetails!!.country).thenReturn(paymentRequest!!.getBillingAddress().toXML())
        `when`(transactionDetails!!.notificationUrl).thenReturn(paymentRequest!!.getNotificationUrl())

        assertEquals(paymentRequest!!.getTransactionType(), "wpf_payment")
        assertEquals(paymentRequest!!.amount?.toPlainString(), "2.00")
        assertEquals(paymentRequest!!.currency, Currency.USD.currency)
        assertEquals(paymentRequest!!.getCustomerEmail(), "john@example.com")
        assertEquals(paymentRequest!!.getCustomerPhone(), "+555555555")
        assertThat(paymentRequest!!.getBillingAddress().toXML(), CoreMatchers.containsString("Fifth avenue 1"))
        assertThat(paymentRequest!!.getBillingAddress().toXML(), CoreMatchers.containsString(Country.UnitedStates.code))
        assertEquals(paymentRequest!!.getNotificationUrl(), "https://example.com")
    }
}
