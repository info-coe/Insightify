package com.infomericainc.insightify.api.mapper

import com.infomericainc.insightify.api.dto.CustomerDto
import com.infomericainc.insightify.api.dto.EphemeralKeyDto
import com.infomericainc.insightify.api.dto.PaymentIntentDto
import com.infomericainc.insightify.api.model.Customer
import com.infomericainc.insightify.api.model.EphemeralKey
import com.infomericainc.insightify.api.model.PaymentIntent

object StripeMapper {

    fun Customer.toCustomerDto(): CustomerDto {
        return CustomerDto(
            customerId = id,
            customerName = name,
            customerEmail = email,
            customerInvoicePrefix = invoicePrefix
        )
    }

    fun EphemeralKey.toEphemeralKeyDto(): EphemeralKeyDto {
        return EphemeralKeyDto(
            ephemeralKeyId = id,
            ephemeralKeySecret = secret,
            ephemeralKeyCreated = created,
            ephemeralKeyExpires = expires,
            ephemeralKeyLiveMode = liveMode
        )
    }

    fun PaymentIntent.toPaymentIntentDto(): PaymentIntentDto {
        return PaymentIntentDto(
            paymentIntentId = id,
            payableAmount = amount,
            clientSecret = clientSecret,
            paymentIntentCreation = created,
            currency = currency,
            customer = customer,
            paymentIntentDescription = description,
            paymentIntentLiveMode = liveMode
        )
    }
}