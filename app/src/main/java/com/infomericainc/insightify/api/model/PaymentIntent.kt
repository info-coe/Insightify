package com.infomericainc.insightify.api.model

import com.google.gson.annotations.SerializedName

data class PaymentIntent(
    @SerializedName("id")
    val id : String? = null,
    @SerializedName("amount")
    val amount : Double? = null,
    @SerializedName("client_secret")
    val clientSecret : String? = null,
    @SerializedName("created")
    val created : Long? = null,
    @SerializedName("currency")
    val currency : String? = null,
    @SerializedName("customer")
    val customer : String? = null,
    @SerializedName("description")
    val description : String? = null,
    @SerializedName("livemode")
    val liveMode : Boolean? = null
)
