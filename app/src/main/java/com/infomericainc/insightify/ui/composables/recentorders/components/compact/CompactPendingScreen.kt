package com.infomericainc.insightify.ui.composables.recentorders.components.compact

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.infomericainc.insightify.ui.composables.recentorders.RecentOrdersUiState
import com.infomericainc.insightify.ui.theme.dark_Pending
import com.infomericainc.insightify.ui.theme.dark_PendingContainer
import com.infomericainc.insightify.ui.theme.dark_onPendingContainer
import com.infomericainc.insightify.ui.theme.light_Pending
import com.infomericainc.insightify.ui.theme.light_PendingContainer
import com.infomericainc.insightify.ui.theme.light_onPendingContainer
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompactPendingScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    recentOrdersUiState: RecentOrdersUiState,
) {
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

@CompactThemedPreviewProvider
@Composable
private fun CompactSuccessScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CompactPendingScreen(
            paddingValues = PaddingValues(),
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