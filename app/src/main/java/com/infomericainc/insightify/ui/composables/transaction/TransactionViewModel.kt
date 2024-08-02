package com.infomericainc.insightify.ui.composables.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.infomericainc.insightify.api.StripeRepository
import com.infomericainc.insightify.api.dto.CustomerDto
import com.infomericainc.insightify.db.dao.UserProfileDao
import com.infomericainc.insightify.manager.PreferencesManager
import com.infomericainc.insightify.util.Constants.TABLE_NUMBER
import com.infomericainc.insightify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val stripeRepository: StripeRepository,
    private val userProfileDao: UserProfileDao,
    private val fireStore: FirebaseFirestore,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        firebaseCrashlytics
            .recordException(throwable)
    }

    private val mutableTransactionUIState: MutableStateFlow<TransactionUiState> =
        MutableStateFlow(TransactionUiState())
    val transactionUiState = mutableTransactionUIState.asStateFlow()

    private val mutableTransactionUpdateUIState: MutableStateFlow<TransactionUpdateUIState> =
        MutableStateFlow(TransactionUpdateUIState())
    val transactionUpdateUIState = mutableTransactionUpdateUIState.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null


    fun onEvent(transactionEvent: TransactionEvent) {
        when (transactionEvent) {
            is TransactionEvent.StartPayment -> startPayment(
                transactionEvent.amount,
                transactionEvent.currency,
                transactionEvent.description
            )

            is TransactionEvent.UpdateTransaction -> updateTransaction()
        }
    }

    //TODO- Need to fetch API from firebase.

    private fun startPayment(
        amount: Double,
        currency: String,
        description: String,
    ) {
        mutableTransactionUIState
            .update {
                it.copy(
                    isLoading = true
                )
            }
        viewModelScope.launch(exceptionHandler) {
            val userProfileEntity = userProfileDao.getUserProfile().firstOrNull()

            val createdCustomer = userProfileEntity?.username?.let { userName ->
                userProfileEntity.email?.let { email ->
                    createCustomer(
                        name = userName,
                        email = email
                    )
                }
            } ?: run {
                mutableTransactionUIState
                    .update {
                        it.copy(
                            isLoading = false,
                            error = "Unable to create Customer."
                        )
                    }
                return@launch
            }
            val customerId = createdCustomer.data?.customerId ?: run {
                mutableTransactionUIState
                    .update {
                        it.copy(
                            isLoading = false,
                            error = "Unable to create Customer."
                        )
                    }
                return@launch
            }

            val generatedEphemeralKey = generateEphemeralKey(
                "Bearer sk_test_51PWGJQ03rPs1nuQKbMTMnWqJAOpkggkuFQD8hda3qhC61u5ZS2VRvQZWn9ZxZ5oRSwUhC0mZru2uRrsGPEtjTKJX00EpWr1Pf2",
                customerId
            )
            val ephemeralKeySecret = generatedEphemeralKey?.data?.ephemeralKeySecret ?: run {
                mutableTransactionUIState
                    .update {
                        it.copy(
                            isLoading = false,
                            error = generatedEphemeralKey?.message
                        )
                    }
                return@launch
            }

            val createPaymentIntent = createPaymentIntent(
                apiKey = "Bearer sk_test_51PWGJQ03rPs1nuQKbMTMnWqJAOpkggkuFQD8hda3qhC61u5ZS2VRvQZWn9ZxZ5oRSwUhC0mZru2uRrsGPEtjTKJX00EpWr1Pf2",
                currency = currency,
                amount = amount,
                description = description,
                customerID = customerId
            )
            val clientSecret = createPaymentIntent?.data?.clientSecret ?: run {
                mutableTransactionUIState
                    .update {
                        it.copy(
                            isLoading = false,
                            error = createPaymentIntent?.message
                        )
                    }
                return@launch
            }

            if (customerId.isNotEmpty() && ephemeralKeySecret.isNotEmpty() && clientSecret.isNotEmpty()) {
                mutableTransactionUIState
                    .update {
                        it.copy(
                            isLoading = false,
                            canShowPaymentSheet = true,
                            customerId = customerId,
                            ephemeralKeySecret = ephemeralKeySecret,
                            clientSecret = clientSecret
                        )
                    }
            }
        }
    }

    private suspend fun createCustomer(
        name: String,
        email: String
    ): Resource<CustomerDto>? {
        return withContext(Dispatchers.IO) {
            stripeRepository
                .createCustomer(
                    "Bearer sk_test_51PWGJQ03rPs1nuQKbMTMnWqJAOpkggkuFQD8hda3qhC61u5ZS2VRvQZWn9ZxZ5oRSwUhC0mZru2uRrsGPEtjTKJX00EpWr1Pf2",
                    name = name,
                    email = email
                )
                .lastOrNull()
        }
    }

    private suspend fun generateEphemeralKey(
        apiKey: String,
        customerID: String
    ) = withContext(Dispatchers.Default) {
        stripeRepository
            .generateEphemeralKey(
                apiKey, customerID
            )
            .lastOrNull()
    }

    private suspend fun createPaymentIntent(
        apiKey: String,
        customerID: String,
        amount: Double,
        currency: String,
        description: String
    ) = withContext(Dispatchers.Default) {
        stripeRepository
            .createPaymentIntent(
                apiKey, customerID, amount, currency, description
            )
            .lastOrNull()
    }


    private fun updateTransaction() {
        mutableTransactionUpdateUIState
            .update {
                it.copy(
                    isUpdating = true
                )
            }
        viewModelScope.launch(exceptionHandler) {
            val tableNumber = preferencesManager
                .getInt(TABLE_NUMBER, 0)
            if (tableNumber == 0) {
                mutableTransactionUpdateUIState
                    .update {
                        it.copy(
                            error = "Table number not set"
                        )
                    }
                return@launch
            }
            val ref = fireStore
                .collection("ORDERS")
                .whereEqualTo("tableNumber", tableNumber)
                .whereEqualTo("orderStatus", "ACCEPTED")
                .orderBy("orderStatus", Query.Direction.DESCENDING)
                .orderBy("orderTime", Query.Direction.DESCENDING)
                .limit(1)

            listenerRegistration = ref.addSnapshotListener { value, _ ->
                if (value == null) {
                    mutableTransactionUpdateUIState
                        .update {
                            it.copy(
                                isUpdating = false,
                                error = "Unable to update the Payment status."
                            )
                        }
                    return@addSnapshotListener
                }
                if (value.documents.isEmpty()) {
                    mutableTransactionUpdateUIState
                        .update {
                            it.copy(
                                isUpdating = false,
                                error = "Unable to update the Payment status."
                            )
                        }
                    return@addSnapshotListener
                }
                val document = value.documents[0]

                document.reference
                    .update(
                        "paymentStatus", "ACCEPTED",
                        "paymentType","online"
                    ).addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            mutableTransactionUpdateUIState
                                .update {
                                    it.copy(
                                        isUpdating = false,
                                        isUpdated = true
                                    )
                                }
                        } else {
                            mutableTransactionUpdateUIState
                                .update {
                                    it.copy(
                                        isUpdating = false,
                                        error = "Unable to update the Payment status."
                                    )
                                }
                        }
                    }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}

private const val TRANSACTION_VIEWMODEL = "TransactionViewModel"