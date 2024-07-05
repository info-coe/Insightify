package com.infomericainc.insightify.api.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class EphemeralKey(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("secret")
    val secret: String? = null,
    @SerializedName("created")
    val created: Long? = null,
    @SerializedName("expires")
    val expires: Long? = null,
    @SerializedName("livemode")
    val liveMode: Boolean? = null
)