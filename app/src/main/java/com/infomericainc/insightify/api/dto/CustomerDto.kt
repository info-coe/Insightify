package com.infomericainc.insightify.api.dto

data class CustomerDto(
    val customerId: String? = null,
    val customerName: String? = null,
    val customerEmail: String? = null,
    val customerInvoicePrefix: String? = null
)
