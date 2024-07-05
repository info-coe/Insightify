package com.infomericainc.insightify.ui.composables.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infomericainc.insightify.api.StripeRepository
import com.infomericainc.insightify.api.dto.CustomerDto
import com.infomericainc.insightify.extension.logError
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
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }

    private val mutableTransactionUIState: MutableStateFlow<TransactionUiState> =
        MutableStateFlow(TransactionUiState())
    val transactionUiState = mutableTransactionUIState.asStateFlow()
    fun onEvent(transactionEvent: TransactionEvent) {
        when (transactionEvent) {
            is TransactionEvent.StartPayment -> startPayment()
        }
    }


    private fun startPayment() {
        mutableTransactionUIState
            .update {
                it.copy(
                    isLoading = true
                )
            }
        viewModelScope.launch(exceptionHandler) {
            val createdCustomer = createCustomer()
            val customerId = createdCustomer?.data?.customerId ?: run {
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
                currency = "USD",
                amount = 1700.0,
                description = "",
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

    private suspend fun createCustomer(): Resource<CustomerDto>? {
        return withContext(Dispatchers.IO) {
            stripeRepository
                .createCustomer(
                    "Bearer sk_test_51PWGJQ03rPs1nuQKbMTMnWqJAOpkggkuFQD8hda3qhC61u5ZS2VRvQZWn9ZxZ5oRSwUhC0mZru2uRrsGPEtjTKJX00EpWr1Pf2",
                    "Test user",
                    "Testemail@gmail.com"
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
}

private const val TRANSACTION_VIEWMODEL = "TransactionViewModel"