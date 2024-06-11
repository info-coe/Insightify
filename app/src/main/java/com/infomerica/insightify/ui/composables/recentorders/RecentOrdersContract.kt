package com.infomerica.insightify.ui.composables.recentorders

import com.google.firebase.Timestamp

sealed class RecentOrdersEvent {
    data object GetRecentOrders : RecentOrdersEvent()

}

data class RecentOrdersUiState(
    val isLoading : Boolean = false,
    val isPending : Boolean = false,
    val isAccepted : Boolean = false,
    val isRejected : Boolean = false,
    val noOrders : Boolean = false,
    val rejectReason : String? = null,
    val customerName: String? = null,
    val orders: Map<String,List<Map<String,Double>?>?>? = null,
    val orderedTime: Timestamp? = null,
    val orderID: String? = null,
    val amount: Map<String,Double?>? = null,
    val taxes: Double? = null,
    val totalAmount: Double? = null,
    val error: String? = null
)