package com.infomericainc.insightify.api

import com.infomericainc.insightify.api.model.Customer
import com.infomericainc.insightify.api.model.EphemeralKey
import com.infomericainc.insightify.api.model.PaymentIntent
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface StripeService {

    @POST("customers")
    @FormUrlEncoded
    suspend fun createCustomer(
        @Header("Authorization") apiKey: String,
        @Field("name") name: String,
        @Field("email") email: String
    ): Customer?

    @POST("ephemeral_keys")
    @FormUrlEncoded
    suspend fun generateEphemeralKey(
        @Header("Authorization") apiKey: String,
        @Header("Stripe-Version") stripeVersion: String = "2024-06-20",
        @Field("customer") customerID: String,
    ): EphemeralKey?

    @POST("payment_intents")
    @FormUrlEncoded
    suspend fun createPaymentIntent(
        @Header("Authorization") apiKey: String,
        @Header("Stripe-Version") stripeVersion: String = "2024-06-20",
        @Field("customer") customerID: String,
        @Field("amount") amount: Long,
        @Field("currency") currency: String,
        @Field("description") description: String
    ): PaymentIntent?

}