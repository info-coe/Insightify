package com.infomericainc.insightify.api.dto


data class PaymentIntentDto(
    val paymentIntentId : String? = null,
    val payableAmount : Double? = null,
    val clientSecret : String? = null,
    val paymentIntentCreation : Long? = null,
    val currency : String? = null,
    val customer : String? = null,
    val paymentIntentDescription : String? = null,
    val paymentIntentLiveMode : Boolean? = null
)