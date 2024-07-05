package com.infomericainc.insightify.ui.composables.transaction


sealed class TransactionEvent {

    data object StartPayment : TransactionEvent()
}

data class TransactionUiState(
    val isLoading : Boolean = false,
    val canShowPaymentSheet : Boolean = false,
    val customerId : String? = null,
    val ephemeralKeySecret : String? = null,
    val clientSecret : String? = null,
    val error : String? = ""
)