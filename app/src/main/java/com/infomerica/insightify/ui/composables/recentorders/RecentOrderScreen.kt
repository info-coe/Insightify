package com.infomerica.insightify.ui.composables.recentorders

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.AvTimer
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.NotInterested
import androidx.compose.material.icons.rounded.Pending
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.GREEN_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Timestamp
import com.infomerica.insightify.R
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
import okhttp3.internal.notify

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecentOrderScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    onOrderEvent: (RecentOrdersEvent) -> Unit,
    recentOrdersUiState: RecentOrdersUiState
) {
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
                                imageVector = Icons.Rounded.Timelapse,
                                contentDescription = "",
                                tint = if (isSystemInDarkTheme()) dark_Pending else light_Pending,
                                modifier = Modifier
                                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._80sdp))
                            )
                            Text(
                                text = "Waiting for approval\nfrom Kitchen.",
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
                    stickyHeader {
                        Text(
                            text = "Your order is in queue. We'll notify you once it's accepted.",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                            modifier = Modifier
                                .alpha(.8f)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .padding(
                                    vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                                ),
                            textAlign = TextAlign.Center,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    item {
                        Text(
                            text = "Your Order",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp,
                            modifier = Modifier.padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                            )
                        )
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                            ) {
                                Text(
                                    text = recentOrdersUiState.customerName ?: "",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = recentOrdersUiState.orderID ?: "",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)))
                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(.6f)
                                        .wrapContentHeight()
                                        .clip(MaterialTheme.shapes.large)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AccessTime,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._22sdp))
                                    )
                                    Text(
                                        text = recentOrdersUiState
                                            .orderedTime?.toDate().toString(),
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                        lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(modifier = Modifier.width(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)))
                                Column(
                                    modifier = Modifier
                                        .weight(.4f)
                                        .wrapContentHeight()
                                        .clip(MaterialTheme.shapes.large)
                                        .background(MaterialTheme.colorScheme.errorContainer)
                                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AttachMoney,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier
                                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._22sdp))
                                    )
                                    Text(
                                        text = recentOrdersUiState
                                            .totalAmount.toString(),
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            it.isAccepted -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(if (isSystemInDarkTheme()) dark_SuccessContainer else light_SuccessContainer),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = "",
                                tint = if (isSystemInDarkTheme()) dark_Success else light_Success,
                                modifier = Modifier
                                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._80sdp))
                            )
                            Text(
                                text = "Chef accepted \n" +
                                        "your order.",
                                textAlign = TextAlign.Center,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSystemInDarkTheme()) dark_onSuccessContainer else light_onPendingContainer,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp,
                                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._25ssp).value.sp,
                                modifier = Modifier.padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                            )

                        }
                    }
                    item {
                        Text(
                            text = "Your order is confirmed! Our chef is preparing your delicious meal, and it will be at your table soon.",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                            modifier = Modifier
                                .alpha(.8f)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .padding(
                                    vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                                ),
                            textAlign = TextAlign.Center,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    item {
                        Text(
                            text = "Thank you for choosing\n indochinese Restaurant.",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
                            textAlign = TextAlign.Center,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._28ssp).value.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    item {
                        Button(
                            onClick = {
                                val recentOrders = arrayOf("test","hello")
                                navController.navigate(HomeScreens.RecentOrderReviewScreen.navigateWithOrders(recentOrders))
                            },
                            modifier = Modifier
                                .fillMaxWidth(.8f)
                                .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                            colors = ButtonDefaults
                                .buttonColors(
                                    containerColor = light_SuccessContainer,
                                    contentColor = light_Success
                                )
                        ) {
                            Icon(imageVector = Icons.Rounded.Star, contentDescription = "")
                            Text(
                                text = "Rate your order.",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                                modifier = Modifier
                                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                            )
                        }
                    }
                }
            }

            it.isRejected -> {

            }
        }
    }
}


@Composable
@Preview(
    showBackground = true,
    device = Devices.PIXEL_7,
    wallpaper = GREEN_DOMINATED_EXAMPLE,
    showSystemUi = true
)
@Preview(
    showBackground = true,
    device = Devices.PIXEL,
    uiMode = UI_MODE_NIGHT_YES,
    wallpaper = GREEN_DOMINATED_EXAMPLE,
    showSystemUi = true
)
private fun RecentOrderScreenPreview() {
    InsightifyTheme {
        RecentOrderScreen(
            navController = rememberNavController(),
            onOrderEvent = {},
            recentOrdersUiState = RecentOrdersUiState(
                noOrders = true,
                isAccepted = false,
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