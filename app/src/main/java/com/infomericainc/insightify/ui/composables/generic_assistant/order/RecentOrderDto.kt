package com.infomericainc.insightify.ui.composables.generic_assistant.order

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

@androidx.annotation.Keep
data class RecentOrderDto(
    @PropertyName(AMOUNT)
    val amount: Map<String, Double?>? = null,
    @PropertyName(TAXES)
    val taxes: Double? = null,
    @PropertyName(TOTAL_AMOUNT)
    val totalAmount: Double? = null,
    @PropertyName(CUSTOMER_NAME)
    val customerName: String? = null,
    @ServerTimestamp
    @PropertyName(ORDER_TIME)
    val orderTime: Timestamp? = null,
    @PropertyName(ORDERS)
    val orders: Map<String, List<Map<String, Double>?>?>? = null,
    @PropertyName(TABLE_NUMBER)
    val tableNumber: Int? = null,
    @PropertyName(ORDER_ID)
    val orderID: String? = null,
    @PropertyName(ORDER_STATUS)
    val orderStatus: String? = null,
    @PropertyName(PAYMENT_STATUS)
    val paymentStatus: String? = null,
    @PropertyName(ORDER_ACCEPTED_TIME)
    val orderAcceptedTime: Timestamp? = null,
    @PropertyName(ORDER_FINISHED_TIME)
    val orderFinishedTime: Timestamp? = null,
    @PropertyName(ORDER_REJECT_REASON)
    val orderRejectReason: String? = null
) {
    private companion object {
        const val AMOUNT = "amount"
        const val TAXES = "taxes"
        const val TOTAL_AMOUNT = "totalAmount"
        const val CUSTOMER_NAME = "customerName"
        const val ORDER_TIME = "orderTime"
        const val ORDERS = "orders"
        const val TABLE_NUMBER = "tableNumber"
        const val ORDER_ID = "orderID"
        const val ORDER_STATUS = "orderStatus"
        const val PAYMENT_STATUS = "paymentStatus"
        const val ORDER_ACCEPTED_TIME = "orderAcceptedTime"
        const val ORDER_FINISHED_TIME = "orderFinishedTime"
        const val ORDER_REJECT_REASON = "orderRejectReason"
    }
}

