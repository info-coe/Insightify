package com.infomericainc.insightify.ui.composables.transaction.varients

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infomericainc.insightify.extension.logInfo
import com.infomericainc.insightify.extension.makeToast
import com.infomericainc.insightify.ui.composables.transaction.TransactionEvent
import com.infomericainc.insightify.ui.composables.transaction.TransactionUiState
import com.infomericainc.insightify.ui.composables.transaction.TransactionUpdateUIState
import com.infomericainc.insightify.ui.composables.transaction.components.compact.CompactLoadingScreen
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import timber.log.Timber

@Composable
fun CompactTransactionScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionUiState: TransactionUiState,
    transactionUpdateUIState: TransactionUpdateUIState,
    amount: Double,
    currency: String,
    onTransactionEvent: (TransactionEvent) -> Unit
) {

    val context = LocalContext.current
    val paymentSheet = rememberPaymentSheet(
        paymentResultCallback = { paymentSheetResult ->
            onPaymentSheetResult(
                paymentSheetResult,
                onPaymentSucceeded = {
                    onTransactionEvent(TransactionEvent.UpdateTransaction)
                },
                onPaymentCancelled = {
                    context
                        .makeToast("Payment Cancelled")
                    navController.popBackStack()
                },
                onPaymentFailed = {
                    context
                        .makeToast("Payment Failed")
                    navController.popBackStack()
                }
            )
        }
    )
    LaunchedEffect(Unit) {
        onTransactionEvent(
            TransactionEvent.StartPayment(
                amount = amount,
                currency = currency,
                description = "IndoChinese Payment"
            )
        )
    }

    LaunchedEffect(transactionUiState) {
        if (transactionUiState.canShowPaymentSheet) {
            presentPaymentSheet(
                paymentSheet = paymentSheet,
                customerConfig = PaymentSheet
                    .CustomerConfiguration(
                        id = transactionUiState.customerId!!,
                        ephemeralKeySecret = transactionUiState.ephemeralKeySecret!!
                    ),
                paymentIntentClientSecret = transactionUiState.clientSecret!!,
            )
        }
    }

    LaunchedEffect(transactionUpdateUIState) {
        if (transactionUpdateUIState.isUpdating) {
            logInfo(TRANSACTION_SCREEN,"updating transaction")
        }
        if (transactionUpdateUIState.isUpdated) {
            navController.popBackStack()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedContent(
            targetState = transactionUiState,
            label = "TransactionAnimation"
        ) { transactionUiState ->
            when {
                transactionUiState.isLoading -> {
                    CompactLoadingScreen()
                }

                transactionUiState.error != null -> {
//                    navController.popBackStack()
                }
            }
        }
    }
}


private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = "IndoChinese",
            customer = customerConfig,
            allowsDelayedPaymentMethods = true,
        )
    )
}

private fun onPaymentSheetResult(
    paymentSheetResult: PaymentSheetResult,
    onPaymentCancelled: () -> Unit,
    onPaymentFailed: () -> Unit,
    onPaymentSucceeded: () -> Unit
) {
    when (paymentSheetResult) {
        is PaymentSheetResult.Canceled -> {
            onPaymentCancelled()
        }

        is PaymentSheetResult.Failed -> {
            onPaymentFailed()
        }

        is PaymentSheetResult.Completed -> {
            onPaymentSucceeded()
        }
    }
}

@Preview
@Composable
private fun CompactTransactionScreenContentPreview() {
    InsightifyTheme {
        Surface {
            CompactTransactionScreenContent(
                navController = rememberNavController(),
                transactionUiState = TransactionUiState(
                    isLoading = true,
                    canShowPaymentSheet = false
                ),
                transactionUpdateUIState = TransactionUpdateUIState(),
                amount = 100.0,
                currency = "usd"
            ) {

            }
        }
    }
}

private const val TRANSACTION_SCREEN = "TransactionScreen"