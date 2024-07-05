package com.infomericainc.insightify.ui.composables.payment.varient

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.dark_Success
import com.infomericainc.insightify.ui.theme.dark_SuccessContainer
import com.infomericainc.insightify.ui.theme.light_Success
import com.infomericainc.insightify.ui.theme.light_SuccessContainer
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompactPaymentScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onMakePayment : () -> Unit
) {
    val isInDarkMode = isSystemInDarkTheme()
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                .weight(1f)
        ) {
            stickyHeader {
                Text(
                    text = "Your order",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._19ssp).value.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.small)
                            .background(if (isInDarkMode) dark_SuccessContainer else light_SuccessContainer)
                            .padding(
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Security,
                            contentDescription = "Security icon",
                            tint = if (isInDarkMode) dark_Success else light_Success,
                            modifier = Modifier
                                .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                        )
                        Text(
                            text = "Payments with insightify are safe and secure.",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = poppinsFontFamily,
                            color = if (isInDarkMode) dark_Success else light_Success,
                            modifier = Modifier
                                .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AttachMoney,
                            contentDescription = "Dollar icon",
                            modifier = Modifier
                                .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "22.00",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Text(
                        text = "Order ID",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                            .alpha(.4f)
                    )
                    Text(
                        text = "28973dhb837re",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                    )
                }
            }

            itemsIndexed(
                items = listOf(
                    Pair("Chicken Biryani", 20.0),
                    Pair("Chicken Noodles", 15.0),
                    Pair("Coke", 5.0),
                )
            ) { index, item ->
                if (index == 0) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}. ${item.first}",
                        fontFamily = poppinsFontFamily,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                            .weight(.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$ 20",
                        fontFamily = poppinsFontFamily,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        modifier = Modifier
                            .weight(.3f)
                            .padding(end = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textAlign = TextAlign.End
                    )
                }
            }

            item {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                )
            }
            itemsIndexed(
                items = listOf(
                    Pair("Taxes", 3.0),
                    Pair("Platform fee", 1.0),
                    Pair("Sub Total", 64.0),
                )
            ) { _, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.first,
                        fontFamily = poppinsFontFamily,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                            .weight(.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$ ${item.second}",
                        fontFamily = poppinsFontFamily,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        modifier = Modifier
                            .weight(.3f)
                            .padding(end = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textAlign = TextAlign.End
                    )
                }
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(
                    bottom = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                    top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                )
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { onMakePayment() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._45sdp))
            ) {
                Text(
                    text = "Make Payment",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                )
            }
            Text(
                text = "Powered by stripe",
                fontFamily = poppinsFontFamily,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .alpha(.6f),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@CompactThemedPreviewProvider
@Composable
private fun CompactPaymentScreenContentPreview() {
    InsightifyTheme {
        Surface {
            CompactPaymentScreen(
                paddingValues = PaddingValues()
            ) {

            }
        }
    }
}