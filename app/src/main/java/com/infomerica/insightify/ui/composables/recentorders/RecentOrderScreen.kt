package com.infomerica.insightify.ui.composables.recentorders

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Timestamp
import com.infomerica.insightify.ui.composables.recentorders.components.compact.CompactPendingScreen
import com.infomerica.insightify.ui.composables.recentorders.components.compact.CompactSuccessScreen
import com.infomerica.insightify.ui.navigation.home.HomeScreens
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.dark_Pending
import com.infomerica.insightify.ui.theme.dark_PendingContainer
import com.infomerica.insightify.ui.theme.dark_Success
import com.infomerica.insightify.ui.theme.dark_SuccessContainer
import com.infomerica.insightify.ui.theme.dark_onPendingContainer
import com.infomerica.insightify.ui.theme.dark_onSuccessContainer
import com.infomerica.insightify.ui.theme.light_Pending
import com.infomerica.insightify.ui.theme.light_PendingContainer
import com.infomerica.insightify.ui.theme.light_Success
import com.infomerica.insightify.ui.theme.light_SuccessContainer
import com.infomerica.insightify.ui.theme.light_onPendingContainer
import com.infomerica.insightify.ui.theme.light_onSuccessContainer
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import timber.log.Timber
import java.math.BigDecimal


@Composable
fun RecentOrderScreen(
    navController: NavController,
    onOrderEvent: (RecentOrdersEvent) -> Unit,
    recentOrdersUiState: RecentOrdersUiState
) {
    var isPending by remember {
        mutableStateOf(false)
    }

    var isAccepted by remember {
        mutableStateOf(false)
    }

    var isRejected by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = recentOrdersUiState) {
        when {
            recentOrdersUiState.isPending -> {
                isPending = true
            }

            recentOrdersUiState.isAccepted -> {
                isPending = false
                isAccepted = true
            }

            recentOrdersUiState.isRejected -> {
                isRejected = true
                isPending = false
                isAccepted = false
            }

            else -> {
                isRejected = false
                isPending = true
                isAccepted = false
            }
        }
    }
    val systemController = rememberSystemUiController()
    val systemBarColor = when (isSystemInDarkTheme()) {
        true -> {
            when {
                isPending -> dark_PendingContainer
                isAccepted -> dark_SuccessContainer
                isRejected -> MaterialTheme.colorScheme.errorContainer
                else -> dark_PendingContainer
            }
        }

        false -> {
            when {
                isPending -> light_PendingContainer
                isAccepted -> light_SuccessContainer
                isRejected -> MaterialTheme.colorScheme.errorContainer
                else -> light_PendingContainer
            }
        }
    }
    val animStatusBarColor =
        animateColorAsState(targetValue = systemBarColor, label = "statusBarColor")
    systemController.setStatusBarColor(animStatusBarColor.value)
    RecentOrderScreenBody(navController, recentOrdersUiState) {
        RecentOrderScreenContent(
            it,
            navController,
            onOrderEvent,
            recentOrdersUiState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecentOrderScreenBody(
    navController: NavController,
    recentOrdersUiState: RecentOrdersUiState,
    content: @Composable (PaddingValues) -> Unit
) {

    var isPending by remember {
        mutableStateOf(false)
    }

    var isAccepted by remember {
        mutableStateOf(false)
    }

    var isRejected by remember {
        mutableStateOf(false)
    }

    var noOrders by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = recentOrdersUiState) {
        when {
            recentOrdersUiState.isPending -> {
                isPending = true
            }

            recentOrdersUiState.isAccepted -> {
                isPending = false
                isAccepted = true
            }

            recentOrdersUiState.isRejected -> {
                isRejected = true
                isPending = false
                isAccepted = false
            }

            recentOrdersUiState.noOrders -> {
                noOrders = true
            }

            else -> {
                isRejected = false
                isPending = true
                isAccepted = false
            }
        }
    }

    val topAppBarColors = when (isSystemInDarkTheme()) {
        true -> {
            when {

                noOrders -> TopAppBarDefaults.topAppBarColors(
                    containerColor = dark_PendingContainer,
                    titleContentColor = dark_onPendingContainer,
                    navigationIconContentColor = dark_Pending
                )

                isPending -> TopAppBarDefaults.topAppBarColors(
                    containerColor = dark_PendingContainer,
                    titleContentColor = dark_onPendingContainer,
                    navigationIconContentColor = dark_Pending
                )

                isAccepted -> TopAppBarDefaults.topAppBarColors(
                    containerColor = dark_SuccessContainer,
                    titleContentColor = dark_onSuccessContainer,
                    navigationIconContentColor = dark_Success
                )

                isRejected -> TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    titleContentColor = MaterialTheme.colorScheme.error,
                    navigationIconContentColor = MaterialTheme.colorScheme.error
                )

                else -> TopAppBarDefaults.topAppBarColors(
                    containerColor = dark_PendingContainer,
                    titleContentColor = dark_onPendingContainer,
                    navigationIconContentColor = dark_Pending
                )
            }
        }

        false -> {
            when {
                noOrders -> TopAppBarDefaults.topAppBarColors(
                    containerColor = light_PendingContainer,
                    titleContentColor = light_onPendingContainer,
                    navigationIconContentColor = light_Pending
                )

                isPending -> TopAppBarDefaults.topAppBarColors(
                    containerColor = light_PendingContainer,
                    titleContentColor = light_onPendingContainer,
                    navigationIconContentColor = light_Pending
                )

                isAccepted -> TopAppBarDefaults.topAppBarColors(
                    containerColor = light_SuccessContainer,
                    titleContentColor = light_onSuccessContainer,
                    navigationIconContentColor = light_Success
                )

                isRejected -> TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    titleContentColor = MaterialTheme.colorScheme.error,
                    navigationIconContentColor = MaterialTheme.colorScheme.error
                )

                else -> TopAppBarDefaults.topAppBarColors(
                    containerColor = light_PendingContainer,
                    titleContentColor = light_onPendingContainer,
                    navigationIconContentColor = light_Pending
                )
            }
        }

    }

    InsightifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Your orders",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    },
                    colors = topAppBarColors
                )
            }
        ) {
            var isRecomposed by remember {
                mutableStateOf(false)
            }
            DisposableEffect(key1 = Unit) {
                isRecomposed = true
                onDispose {
                    isRecomposed = false
                }
            }

            AnimatedVisibility(
                visible = isRecomposed,
                enter = fadeIn(tween(500))
                        + slideInVertically(tween(800), initialOffsetY = { -it / 8 })
                        + scaleIn(initialScale = .8f, transformOrigin = TransformOrigin.Center),
                exit = fadeOut(tween(500))
                        + slideOutVertically(tween(800), targetOffsetY = { -it / 9 })
                        + scaleOut(targetScale = .9f, transformOrigin = TransformOrigin.Center)
            ) {
                content(it)
            }
        }
    }
}

@Composable
private fun RecentOrderScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    onOrderEvent: (RecentOrdersEvent) -> Unit,
    recentOrdersUiState: RecentOrdersUiState
) {

    val context = LocalContext.current
    val payPalConfiguration by remember {
        mutableStateOf(
            PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId("ARHwaCtPVdBjd3764AWCFSxuyqkJDOaExtzLbs98LlaKxfHjFbRMGzagLeJSbUfco9EqPoI9GNqyhoLC")
                .acceptCreditCards(true)
                .rememberUser(true)
                .forceDefaultsOnSandbox(true)
        )
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val paymentConfirmation =
                    result.data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (paymentConfirmation != null) {
                    Timber.tag("PAYMENT").d(paymentConfirmation.toString())
                } else {
                    Timber.tag("PAYMENT").e("Payment confirmation is null")
                }
            } else {
                Timber.tag("PAYMENT").e("Payment failed or was cancelled")
            }
        }

    AnimatedContent(
        targetState = recentOrdersUiState,
        label = "OrderContent",
    ) {
        when {
            it.noOrders -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(if (isSystemInDarkTheme()) dark_PendingContainer else light_PendingContainer),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.HourglassEmpty,
                                contentDescription = "",
                                tint = if (isSystemInDarkTheme()) dark_Pending else light_Pending,
                                modifier = Modifier
                                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._80sdp))
                            )
                            Text(
                                text = "No Orders here\ntry ordering from assistant.",
                                textAlign = TextAlign.Center,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSystemInDarkTheme()) dark_onPendingContainer else light_onPendingContainer,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp,
                                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._25ssp).value.sp,
                                modifier = Modifier.padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                            )
                        }
                    }
                }
            }

            it.isPending -> {
                CompactPendingScreen(
                    paddingValues = paddingValues,
                    recentOrdersUiState = recentOrdersUiState
                )
            }

            it.isAccepted -> {
                CompactSuccessScreen(
                    paddingValues = paddingValues,
                    onRateOrder = {
                        val recentOrders = arrayOf("test", "hello")
                        navController.navigate(
                            HomeScreens.RecentOrderReviewScreen.navigateWithOrders(
                                recentOrders
                            )
                        )
                    },
                    onMakePayment = {
                        makePayment(
                            context = context,
                            amount = recentOrdersUiState.totalAmount.toString(),
                            currencyType = "USD",
                            payPalConfiguration = payPalConfiguration,
                            billName = "${recentOrdersUiState.customerName} Restaurant bill",
                            launcher = launcher
                        )
                    }
                )
            }

            it.isRejected -> {
                
            }
        }
    }
}


private fun makePayment(
    context: Context,
    amount: String,
    currencyType: String,
    payPalConfiguration: PayPalConfiguration,
    billName: String,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val payment = PayPalPayment(
        BigDecimal(amount),
        currencyType,
        billName,
        PayPalPayment.PAYMENT_INTENT_SALE
    )

    Intent(context, PaymentActivity::class.java).apply {
        putExtra(
            PayPalService.EXTRA_PAYPAL_CONFIGURATION,
            payPalConfiguration
        )
        putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        launcher.launch(this)
    }
}

@Composable
@CompactThemedPreviewProvider
private fun RecentOrderScreenPreview() {
    InsightifyTheme {
        RecentOrderScreen(
            navController = rememberNavController(),
            onOrderEvent = {},
            recentOrdersUiState = RecentOrdersUiState(
                noOrders = true,
                isAccepted = true,
                isPending = false,
                orders = mapOf(
                    Pair("Appetizers", listOf(mapOf(Pair("Aloo Tikki", 2.22)))),
                    Pair("Beverages", null)
                ),
                orderID = "gfjsgdfusdgf",
                amount = mapOf("Dollars" to 22.33),
                totalAmount = 24.33,
                taxes = 1.22,
                customerName = "Bharadwaj.R",
                orderedTime = Timestamp.now()
            )
        )
    }
}

private const val RECENT_SESSIONS_ANIMATION = "recent_session_animation"