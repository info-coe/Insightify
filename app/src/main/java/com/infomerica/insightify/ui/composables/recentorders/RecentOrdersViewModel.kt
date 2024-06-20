package com.infomerica.insightify.ui.composables.recentorders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.infomerica.insightify.db.dao.UserProfileDao
import com.infomerica.insightify.ui.composables.generic_assistant.order.RecentOrderDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecentOrdersViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val userProfileDao: UserProfileDao,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber
            .tag("RECENT_ORDER_SCREEN")
            .d("Exception caught ${throwable.cause}")
    }

    init {
        Timber.tag(RECENT_ORDER_VIEW_MODEL)
            .i("Created")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(RECENT_ORDER_VIEW_MODEL)
            .i("Destroyed")
    }

    private val mutableRecentOrdersUiState =
        MutableStateFlow(RecentOrdersUiState())
    val recentOrderUiState = mutableRecentOrdersUiState.asStateFlow()


    fun onEvent(event: RecentOrdersEvent) {
        when (event) {
            is RecentOrdersEvent.GetRecentOrders -> getPreviousOrders()
        }
    }


    private fun getPreviousOrders() {
        Timber
            .tag("RECENT_ORDER_SCREEN")
            .d("called")
        mutableRecentOrdersUiState
            .update {
                it.copy(
                    isLoading = true
                )
            }

        viewModelScope.launch {
            fireStore
                .collection("ORDERS")
                .whereEqualTo("tableNumber", 7)
                .whereNotEqualTo("paymentStatus","PAID")
                .orderBy("paymentStatus")
                .orderBy("orderTime", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    Timber
                        .tag(RECENT_ORDER_VIEW_MODEL)
                        .d(value.toString())
                    if (value == null) {
                        mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    noOrders = true
                                )
                            }
                        return@addSnapshotListener
                    }
                    if(value.documents.isEmpty()) {
                        mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    noOrders = true
                                )
                            }
                        return@addSnapshotListener
                    }
                     val recentOrder = value.documents[0].toObject(RecentOrderDto::class.java)
                    Timber
                        .tag(RECENT_ORDER_VIEW_MODEL)
                        .d(recentOrder.toString())
                    when (recentOrder?.orderStatus) {
                        "PENDING" -> mutableRecentOrdersUiState
                            .update {
                                it.copy(
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
                                    isPending = false,
                                    isAccepted = true,
                                    isRejected = false,
                                    orders = recentOrder.orders,
                                    orderID = recentOrder.orderID,
                                    amount = recentOrder.amount,
                                    taxes = recentOrder.taxes,
                                    totalAmount = recentOrder.totalAmount,
                                    customerName = recentOrder.customerName
                                )
                            }

                        "REJECTED" -> mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    isPending = false,
                                    isAccepted = false,
                                    isRejected = true,
                                    rejectReason = recentOrder.orderRejectReason,
                                )
                            }

                        else -> mutableRecentOrdersUiState
                            .update {
                                it.copy(
                                    isPending = true
                                )
                            }
                    }

                }
        }
    }

}

private const val RECENT_ORDER_VIEW_MODEL = "RecentOrderViewModel"