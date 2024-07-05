package com.infomericainc.insightify.ui.composables.transaction.varients

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infomericainc.insightify.ui.composables.transaction.TransactionEvent
import com.infomericainc.insightify.ui.composables.transaction.TransactionUiState
import com.infomericainc.insightify.ui.composables.transaction.components.compact.CompactLoadingScreen
import com.infomericainc.insightify.ui.composables.transaction.components.compact.CompactSuccessScreen
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@Composable
fun CompactTransactionScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionUiState: TransactionUiState,
    onTransactionEvent: (TransactionEvent) -> Unit
) {

    val context = LocalContext.current

    var showPaymentSheet by rememberSaveable {
        mutableStateOf(false)
    }
    var showCompletedPayment by remember {
        mutableStateOf(false)
    }
    var showFailedPayment by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        onTransactionEvent(TransactionEvent.StartPayment)
    }

    LaunchedEffect(transactionUiState) {
        if (transactionUiState.canShowPaymentSheet) {
            if (transactionUiState.customerId != null && transactionUiState.clientSecret != null && transactionUiState.ephemeralKeySecret != null) {
                showPaymentSheet = true
            }
        }
    }

    LaunchedEffect(showCompletedPayment) {
        showPaymentSheet = false
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(visible = showCompletedPayment) {
            CompactSuccessScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
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
        if (showPaymentSheet) {
            PresentPaymentSheet(
                customerConfig = PaymentSheet
                    .CustomerConfiguration(
                        id = transactionUiState.customerId!!,
                        ephemeralKeySecret = transactionUiState.ephemeralKeySecret!!
                    ),
                paymentIntentClientSecret = transactionUiState.clientSecret!!,
                onPaymentCompleted = {
                    showCompletedPayment = true
                },
                onPaymentFailed = {

                },
                onPaymentCancel = {

                }
            )
        }
    }
}


@Composable
private fun PresentPaymentSheet(
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String,
    onPaymentCompleted: () -> Unit,
    onPaymentFailed: () -> Unit,
    onPaymentCancel: () -> Unit
) {

    val paymentSheet = rememberPaymentSheet(
        paymentResultCallback = { paymentSheetResult ->
            when (paymentSheetResult) {
                is PaymentSheetResult.Canceled -> {
                    onPaymentCancel()
                }

                is PaymentSheetResult.Failed -> {
                    onPaymentFailed()
                }

                is PaymentSheetResult.Completed -> {
                    onPaymentCompleted()
                }
            }
        }
    )
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = "IndoChinese",
            customer = customerConfig,
            allowsDelayedPaymentMethods = true,
            googlePay = PaymentSheet.GooglePayConfiguration(
                environment = PaymentSheet.GooglePayConfiguration.Environment.Test,
                countryCode = "+1",
                currencyCode = "USD",
                amount = 1200
            )
        )
    )
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
                )
            ) {

            }
        }
    }
}