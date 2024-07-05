package com.infomericainc.insightify.api

import com.infomericainc.insightify.api.dto.CustomerDto
import com.infomericainc.insightify.api.dto.EphemeralKeyDto
import com.infomericainc.insightify.api.dto.PaymentIntentDto
import com.infomericainc.insightify.api.mapper.StripeMapper.toCustomerDto
import com.infomericainc.insightify.api.mapper.StripeMapper.toEphemeralKeyDto
import com.infomericainc.insightify.api.mapper.StripeMapper.toPaymentIntentDto
import com.infomericainc.insightify.extension.logError
import com.infomericainc.insightify.extension.logInfo
import com.infomericainc.insightify.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.concurrent.CancellationException
import javax.inject.Inject


class StripeRepository @Inject constructor(
    private val stripeService: StripeService
) {
    init {
        logInfo(STRIPE_REPOSITORY, "initialized")
    }

    fun createCustomer(
        apiKey: String,
        name: String,
        email: String
    ): Flow<Resource<CustomerDto>> = flow {
        logInfo(STRIPE_REPOSITORY, "creating the customer")
        emit(Resource.Loading())
        val createdCustomer = stripeService
            .createCustomer(
                apiKey, name, email
            )
        if (createdCustomer != null) {
            val customerDto = createdCustomer.toCustomerDto()
            logInfo(STRIPE_REPOSITORY, "customer created $customerDto")
            emit(Resource.Success(createdCustomer.toCustomerDto()))
        } else {
            emit(Resource.Error("Unable to create Customer."))
            logInfo(
                STRIPE_REPOSITORY,
                "Unable to convert customer because created Customer it is null."
            )
        }
    }.catch {
        logError(
            STRIPE_REPOSITORY,
            "error creating the customer because ${it.message}"
        )
    }

    fun generateEphemeralKey(
        apiKey: String,
        customerID: String
    ): Flow<Resource<EphemeralKeyDto>> = flow {
        logInfo(STRIPE_REPOSITORY, "generating the Ephemeral Key")
        emit(Resource.Loading())
        val generatedEphemeralKey = stripeService
            .generateEphemeralKey(
                apiKey = apiKey,
                customerID = customerID
            )
        if (generatedEphemeralKey != null) {
            logInfo(STRIPE_REPOSITORY, "Ephemeral Key generated : $generatedEphemeralKey")
            emit(Resource.Success(generatedEphemeralKey.toEphemeralKeyDto()))
        } else {
            emit(Resource.Error("Unable to generate Ephemeral Key because created Ephemeral Key it is null."))
        }
    }.catch {
        logError(
            STRIPE_REPOSITORY,
            "Unable to generate Ephemeral Key because ${it.message}"
        )
    }

    fun createPaymentIntent(
        apiKey: String,
        customerID: String,
        amount: Double,
        currency: String,
        description: String
    ): Flow<Resource<PaymentIntentDto>> = flow {
        logInfo(STRIPE_REPOSITORY, "generating the Payment Intent")
        emit(Resource.Loading())
        val createdPaymentIntent = stripeService
            .createPaymentIntent(
                apiKey = apiKey,
                customerID = customerID,
                amount = amount.toInt(),
                currency = currency,
                description = description
            )
        if (createdPaymentIntent != null) {
            val paymentIntentDto = createdPaymentIntent.toPaymentIntentDto()
            logInfo(STRIPE_REPOSITORY, "payment Intent created : $paymentIntentDto")
            emit(Resource.Success(paymentIntentDto))
        } else {
            emit(Resource.Error("Unable to create PaymentIntent because payment Intent is null."))
        }
    }.catch {
        logError(
            STRIPE_REPOSITORY,
            "Unable to create Payment Intent because ${it.message}"
        )
    }
}


private const val STRIPE_REPOSITORY = "StripeRepository"