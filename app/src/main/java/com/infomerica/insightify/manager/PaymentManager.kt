package com.infomerica.insightify.manager

import android.app.Activity
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import javax.inject.Inject

class PaymentManager @Inject constructor() {
    fun makeGooglePayPayment(
        activity: Activity,
        amount: String,
        upi: String,
        name: String,
        description: String,
        transactionID: String,
        transactionRefID: String,
        paymentStatusListener: PaymentStatusListener,
    ) {
        val easyUpiPayment = EasyUpiPayment(activity) {
            paymentApp = PaymentApp.GOOGLE_PAY
            payeeName = name
            payeeVpa = upi
            transactionId = transactionID
            transactionRefId = transactionRefID
            this.description = description
            this.amount = amount
        }
        easyUpiPayment.setPaymentStatusListener(paymentStatusListener)
        try {
            easyUpiPayment.startPayment()
        } catch (e : Exception) {
            if(e is AppNotFoundException) {
                easyUpiPayment.removePaymentStatusListener()
                throw e
            }
        }
    }
}