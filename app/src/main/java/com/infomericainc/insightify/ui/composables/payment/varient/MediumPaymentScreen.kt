package com.infomericainc.insightify.ui.composables.payment.varient

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infomericainc.insightify.extension.logInfo
import com.infomericainc.insightify.ui.components.dialog.InstfyProgressDialog
import com.infomericainc.insightify.ui.composables.payment.PaymentEvent
import com.infomericainc.insightify.ui.composables.payment.RecentPaymentOrderUiState
import com.infomericainc.insightify.ui.composables.payment.components.compact.CompactPaymentPendingContent
import com.infomericainc.insightify.ui.composables.payment.components.compact.CompactPaymentSuccessScreen
import com.infomericainc.insightify.ui.composables.payment.components.compact.CompactReceptionPaymentScreen
import com.infomericainc.insightify.ui.composables.payment.components.medium.MediumPaymentPendingContent
import com.infomericainc.insightify.ui.composables.payment.components.medium.MediumPaymentSuccessScreen
import com.infomericainc.insightify.ui.composables.payment.components.medium.MediumReceptionPaymentScreen
import com.infomericainc.insightify.ui.navigation.home.HomeScreens
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun MediumPaymentScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    recentOrderUiState: RecentPaymentOrderUiState,
    navController: NavController,
    onPaymentEvent: (PaymentEvent) -> Unit
) {
    //Fetch recent orders
    LaunchedEffect(Unit) {
        onPaymentEvent(PaymentEvent.FetchRecentOrders)
    }

    //Show loading dialog
    var showLoadingDialog by remember {
        mutableStateOf(false)
    }

    //Log recent order
    LaunchedEffect(recentOrderUiState) {
        logInfo(
            COMPACT_PAYMENT_SCREEN,
            recentOrderUiState.toString()
        )
    }

    //Show content
    AnimatedContent(
        targetState = recentOrderUiState,
        label = "PaymentAnimation",
        transitionSpec = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(700),
                initialOffset = {
                    it / 4
                }
            ) togetherWith slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
        }
    ) { uiState ->
        when {
            //Loading
            uiState.isLoading -> {
                showLoadingDialog = true
            }

            uiState.isPending -> {
                showLoadingDialog = false
                recentOrderUiState.takeIf {
                    it.totalAmount != null
                            && it.taxes != null
                            && it.orderID != null
                            && it.orders != null
                            && it.customerName != null
                            && it.amount != null
                            && it.amount.map { currency -> currency.key }.first().isNotEmpty()
                            && it.amount.map { amount -> amount.value }.first() != null
                }?.let {
                    MediumPaymentPendingContent(
                        paddingValues = paddingValues,
                        orderId = recentOrderUiState.orderID!!,
                        orderItems = recentOrderUiState.orders,
                        subTotal = recentOrderUiState.amount?.map { it.value }?.first(),
                        taxes = recentOrderUiState.taxes!!,
                        totalCost = recentOrderUiState.totalAmount!!,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onMakePayment = {
                            navController
                                .navigate(
                                    HomeScreens.TransactionScreen.navigateWithAmountAndCurrency(
                                        amount = recentOrderUiState.totalAmount,
                                        currency = recentOrderUiState.amount?.map { it.key }
                                            ?.first()!!
                                    )
                                )
                        }
                    )
                } ?: run {
                    MediumReceptionPaymentScreen(
                        paddingValues = paddingValues,
                        onExitClick = {
                            navController.navigateUp()
                        }
                    )
                }
            }

            uiState.isPaid -> {
                showLoadingDialog = false
                MediumPaymentSuccessScreen(
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }

            uiState.noOrders -> {
                showLoadingDialog = false
                MediumReceptionPaymentScreen(
                    paddingValues = paddingValues,
                    onExitClick = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }

    if (showLoadingDialog) {
        InstfyProgressDialog(
            title = "Gathering your payment",
            description = "Please wait, This may take a while depends on your internet connection"
        )
    }
}

@CompactThemedPreviewProvider
@Composable
private fun MediumPaymentScreenContentPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            MediumPaymentScreen(
                paddingValues = PaddingValues(),
                recentOrderUiState = RecentPaymentOrderUiState(
                    isLoading = false,
                    isPending = false,
                    noOrders = true
                ),
                navController = rememberNavController()
            ) {

            }
        }
    }
}


private const val COMPACT_PAYMENT_SCREEN = "COMPACT_PAYMENT_SCREEN"