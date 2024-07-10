package com.infomericainc.insightify.ui.composables.payment

import com.google.firebase.Timestamp

sealed class PaymentEvent {
    data object FetchRecentOrders : PaymentEvent()
}

data class RecentPaymentOrderUiState(
    val isLoading : Boolean = false,
    val isPending : Boolean = false,
    val isPaid: Boolean = false,
    val noOrders : Boolean = false,
    val customerName: String? = null,
    val orders: Map<String,List<Map<String,Double>?>?>? = null,
    val orderedTime: Timestamp? = null,
    val orderID: String? = null,
    val amount: Map<String,Double?>? = null,
    val taxes: Double? = null,
    val totalAmount: Double? = null,
    val error: String? = null
)