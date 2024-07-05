package com.infomericainc.insightify.api.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Customer(
    @SerializedName("id")
    val id : String? = null,
    @SerializedName("name")
    val name : String? = null,
    @SerializedName("email")
    val email : String? = null,
    @SerializedName("created")
    val createdAt : String? = null,
    @SerializedName("invoice_prefix")
    val invoicePrefix : String? = null
)
