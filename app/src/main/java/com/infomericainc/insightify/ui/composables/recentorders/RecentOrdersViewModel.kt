package com.infomericainc.insightify.ui.composables.recentorders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.infomericainc.insightify.manager.PreferencesManager
import com.infomericainc.insightify.ui.composables.genericassistant.order.RecentOrderDto
import com.infomericainc.insightify.util.Constants.TABLE_NUMBER
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
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        firebaseCrashlytics
            .recordException(throwable)
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

        viewModelScope.launch(exceptionHandler) {
            val tableNumber = preferencesManager
                .getInt(TABLE_NUMBER, 0)
            if (tableNumber == 0) {
                mutableRecentOrdersUiState
                    .update {
                        it.copy(
                            error = "Table number not set"
                        )
                    }
                return@launch
            }
            fireStore
                .collection("ORDERS")
                .whereEqualTo("tableNumber", tableNumber)
                .whereNotEqualTo("paymentStatus", "ACCEPTED")
                .orderBy("paymentStatus")
                .orderBy("orderTime", Query.Direction.DESCENDING)
                .limit(1)
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
                    if (value.documents.isEmpty()) {
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
                                    customerName = recentOrder.customerName,
                                    paymentStatus = recentOrder.paymentStatus
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