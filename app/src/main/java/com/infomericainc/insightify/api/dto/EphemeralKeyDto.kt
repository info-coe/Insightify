package com.infomericainc.insightify.api.dto

data class EphemeralKeyDto(
    val ephemeralKeyId: String? = null,
    val ephemeralKeySecret: String? = null,
    val ephemeralKeyCreated: Long? = null,
    val ephemeralKeyExpires: Long? = null,
    val ephemeralKeyLiveMode: Boolean? = null
)
