package com.infomericainc.insightify.ui.composables.recentorders

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Timestamp
import com.infomericainc.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomericainc.insightify.ui.composables.recentorders.variants.CompactRecentOrderScreen
import com.infomericainc.insightify.ui.composables.recentorders.variants.MediumRecentOrderScreen
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.dark_Pending
import com.infomericainc.insightify.ui.theme.dark_PendingContainer
import com.infomericainc.insightify.ui.theme.dark_Success
import com.infomericainc.insightify.ui.theme.dark_SuccessContainer
import com.infomericainc.insightify.ui.theme.dark_onPendingContainer
import com.infomericainc.insightify.ui.theme.dark_onSuccessContainer
import com.infomericainc.insightify.ui.theme.light_Pending
import com.infomericainc.insightify.ui.theme.light_PendingContainer
import com.infomericainc.insightify.ui.theme.light_Success
import com.infomericainc.insightify.ui.theme.light_SuccessContainer
import com.infomericainc.insightify.ui.theme.light_onPendingContainer
import com.infomericainc.insightify.ui.theme.light_onSuccessContainer
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CalculateWindowSize
import com.infomericainc.insightify.util.MediumThemedPreviewProvider


@Composable
fun RecentOrderScreen(
    navController: NavController,
    onOrderEvent: (RecentOrdersEvent) -> Unit,
    recentOrdersUiState: RecentOrdersUiState,
    windowWidthSizeClass: WindowWidthSizeClass
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
        CalculateWindowSize(
            windowWidthSizeClass = windowWidthSizeClass,
            compactContent = {
                CompactRecentOrderScreen(
                    it,
                    navController,
                    onOrderEvent,
                    recentOrdersUiState
                )
            },
            mediumContent = {
                MediumRecentOrderScreen(
                    it,
                    navController,
                    onOrderEvent,
                    recentOrdersUiState
                )
            },
            unSupportedContent = {
                UnSupportedResolutionPlaceHolder()
            }
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
@MediumThemedPreviewProvider
private fun RecentOrderScreenPreview() {
    InsightifyTheme {
        RecentOrderScreen(
            navController = rememberNavController(),
            onOrderEvent = {},
            recentOrdersUiState = RecentOrdersUiState(
                noOrders = true,
                isAccepted = true,
                isPending = true,
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
            ),
            windowWidthSizeClass = WindowWidthSizeClass.Medium
        )
    }
}

private const val RECENT_SESSIONS_ANIMATION = "recent_session_animation"