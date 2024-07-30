package com.infomericainc.insightify.ui.composables.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.infomericainc.insightify.ui.composables.genericassistant.order.RecentOrderDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseCrashlytics: FirebaseCrashlytics
) : ViewModel() {

    private val mutableRecentOrdersUiState =
        MutableStateFlow(RecentPaymentOrderUiState())
    val recentOrderUiState = mutableRecentOrdersUiState.asStateFlow()


    fun onEvent(event: PaymentEvent) {
        when (event) {
            is PaymentEvent.FetchRecentOrders -> getPreviousOrders()
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        firebaseCrashlytics
            .recordException(throwable)
    }

    private fun getPreviousOrders() {
        mutableRecentOrdersUiState
            .update {
                it.copy(
                    isLoading = true
                )
            }

        viewModelScope.launch(exceptionHandler) {
            fireStore
                .collection("ORDERS")
                .whereEqualTo("tableNumber", 7)
                .whereEqualTo("orderStatus", "ACCEPTED")
                .orderBy("orderStatus", Query.Direction.DESCENDING)
                .orderBy("orderTime", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        firebaseCrashlytics
                            .recordException(error)
                        mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    isLoading = false,
                                    noOrders = true,
                                    error = error.message
                                )
                            }
                        return@addSnapshotListener
                    }
                    Timber
                        .tag("Test")
                        .d(value?.documents?.get(0).toString())
                    if (value == null) {
                        mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    isLoading = false,
                                    noOrders = true
                                )
                            }
                        return@addSnapshotListener
                    }
                    if (value.documents.isEmpty()) {
                        mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    isLoading = false,
                                    noOrders = true
                                )
                            }
                        return@addSnapshotListener
                    }
                    val recentOrder = value.documents[0].toObject(RecentOrderDto::class.java)
                    Timber
                        .tag("Test From VM")
                        .d(recentOrder?.paymentStatus)
                    when (recentOrder?.paymentStatus) {
                        "PENDING" -> mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    isLoading = false,
                                    isPending = true,
                                    orders = recentOrder.orders,
                                    orderID = recentOrder.orderID,
                                    amount = recentOrder.amount,
                                    taxes = recentOrder.taxes,
                                    totalAmount = recentOrder.totalAmount,
                                    customerName = recentOrder.customerName,
                                    orderedTime = recentOrder.orderTime
                                )
                            }

                        "ACCEPTED" -> mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    isLoading = false,
                                    isPending = false,
                                    isPaid = true,
                                    orders = recentOrder.orders,
                                    orderID = recentOrder.orderID,
                                    amount = recentOrder.amount,
                                    taxes = recentOrder.taxes,
                                    totalAmount = recentOrder.totalAmount,
                                    customerName = recentOrder.customerName
                                )
                            }

                        else -> mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    isPending = true,
                                    isLoading = false
                                )
                            }
                    }

                }
        }
    }
}