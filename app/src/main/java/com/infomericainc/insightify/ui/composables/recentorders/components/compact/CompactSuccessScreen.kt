package com.infomericainc.insightify.ui.composables.recentorders.components.compact

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.ui.composables.recentorders.RecentOrdersUiState
import com.infomericainc.insightify.ui.theme.dark_Success
import com.infomericainc.insightify.ui.theme.dark_SuccessContainer
import com.infomericainc.insightify.ui.theme.dark_onSuccessContainer
import com.infomericainc.insightify.ui.theme.light_Success
import com.infomericainc.insightify.ui.theme.light_SuccessContainer
import com.infomericainc.insightify.ui.theme.light_onPendingContainer
import com.infomericainc.insightify.ui.theme.light_onSuccessContainer
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun CompactSuccessScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    recentOrdersUiState: RecentOrdersUiState,
    onRateOrder: () -> Unit,
    onMakePayment: () -> Unit
) {
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
            Row(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .fillMaxWidth()
            ) {
                if(recentOrdersUiState.paymentStatus != "ACCEPTED") {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(max = dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                            .clip(MaterialTheme.shapes.large)
                            .background(light_SuccessContainer)
                            .clickable { onMakePayment() }
                            .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Payment,
                            contentDescription = "",
                            tint = light_onSuccessContainer,
                            modifier = Modifier
                                .size(dimensionResource(id = com.intuit.sdp.R.dimen._22sdp))
                        )
                        Text(
                            text = "Make payment",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            modifier = Modifier
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                            color = light_onSuccessContainer,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(max = dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                        .clip(MaterialTheme.shapes.large)
                        .background(light_SuccessContainer)
                        .clickable { onRateOrder() }
                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "",
                        tint = light_onSuccessContainer,
                        modifier = Modifier
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._22sdp))
                    )
                    Text(
                        text = "Rate your order",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        modifier = Modifier
                            .padding(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                        color = light_onSuccessContainer,
                        textAlign = TextAlign.Center
                    )
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
        CompactSuccessScreen(
            paddingValues = PaddingValues(),
            recentOrdersUiState = RecentOrdersUiState(),
            onRateOrder = { /*TODO*/ },
            onMakePayment = {

            })
    }
}