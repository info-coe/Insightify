package com.infomericainc.insightify.ui.composables.transaction


sealed class TransactionEvent {

    data class StartPayment(
        val amount: Double = 0.0,
        val currency: String = "",
        val description: String = "",
    ) : TransactionEvent()

    data object UpdateTransaction : TransactionEvent()
}

data class TransactionUiState(
    val isLoading: Boolean = false,
    val canShowPaymentSheet: Boolean = false,
    val customerId: String? = null,
    val ephemeralKeySecret: String? = null,
    val clientSecret: String? = null,
    val error: String? = ""
)

data class TransactionUpdateUIState(
    val isUpdating: Boolean = false,
    val isUpdated: Boolean = false,
    val error: String? = ""
)