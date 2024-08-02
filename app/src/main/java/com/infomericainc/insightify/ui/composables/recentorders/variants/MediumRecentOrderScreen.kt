package com.infomericainc.insightify.ui.composables.recentorders.variants

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import com.infomericainc.insightify.ui.composables.recentorders.RecentOrdersEvent
import com.infomericainc.insightify.ui.composables.recentorders.RecentOrdersUiState
import com.infomericainc.insightify.ui.composables.recentorders.components.medium.MediumPendingScreen
import com.infomericainc.insightify.ui.composables.recentorders.components.medium.MediumSuccessScreen
import com.infomericainc.insightify.ui.navigation.home.HomeScreens
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.dark_Pending
import com.infomericainc.insightify.ui.theme.dark_PendingContainer
import com.infomericainc.insightify.ui.theme.dark_onPendingContainer
import com.infomericainc.insightify.ui.theme.light_Pending
import com.infomericainc.insightify.ui.theme.light_PendingContainer
import com.infomericainc.insightify.ui.theme.light_onPendingContainer
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.MediumThemedPreviewProvider

@Composable
fun MediumRecentOrderScreen(
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
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
                            )
                            Text(
                                text = "No Orders here\ntry ordering from assistant.",
                                textAlign = TextAlign.Center,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSystemInDarkTheme()) dark_onPendingContainer else light_onPendingContainer,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                                modifier = Modifier.padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                            )
                        }
                    }
                }
            }

            it.isPending -> {
                MediumPendingScreen(
                    paddingValues = paddingValues,
                    recentOrdersUiState = recentOrdersUiState
                )
            }

            it.isAccepted -> {
                MediumSuccessScreen(
                    paddingValues = paddingValues,
                    recentOrdersUiState = recentOrdersUiState,
                    onRateOrder = {
                        val recentOrders = arrayOf("test", "hello")
                        navController.navigate(
                            HomeScreens.RecentOrderReviewScreen.navigateWithOrders(
                                recentOrders
                            )
                        )
                    },
                    onMakePayment = {
                        navController.navigate(HomeScreens.PaymentScreen.route)
                    }
                )
            }

            it.isRejected -> {

            }
        }
    }
}


@MediumThemedPreviewProvider
@Composable
private fun MediumRecentOrderScreenPreview() {
    InsightifyTheme {
        Surface {
            MediumRecentOrderScreen(
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
                paddingValues = PaddingValues()
            )
        }
    }
}