package com.infomericainc.insightify.ui.composables.payment.components.medium

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.components.InstfyLottie
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.dark_Success
import com.infomericainc.insightify.ui.theme.dark_SuccessContainer
import com.infomericainc.insightify.ui.theme.light_Success
import com.infomericainc.insightify.ui.theme.light_SuccessContainer
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.MediumThemedPreviewProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MediumPaymentSuccessScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {

    var showOptions by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var isReviewed by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            InstfyLottie(
                resource = R.raw.payment,
                foreverIteration = false,
                reverseOnRepeat = false,
                modifier = Modifier
                    .size(
                        if (showOptions) dimensionResource(
                            id = com.intuit.sdp.R.dimen._80sdp
                        ) else dimensionResource(id = com.intuit.sdp.R.dimen._150sdp)
                    )
                    .animateItem(
                        placementSpec = tween(1200)
                    ),
                speed = .9f,
                contentScale = ContentScale.Fit,
                onPlayFinished = {
                    coroutineScope.launch {
                        delay(3000)
                        showOptions = true
                    }
                }
            )
        }
        if (showOptions) {
            item {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = if (isSystemInDarkTheme()) dark_Success else light_Success,
                                fontWeight = FontWeight.SemiBold,
                            )
                        ) {
                            append("Payment Successful")
                        }
                        append(
                            " Thank you for choosing indochinese Restaurant."
                        )
                    },
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                        )
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                        .animateItem(
                            fadeInSpec = tween(delayMillis = 1200)
                        ),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        .animateItem(
                            fadeInSpec = tween(delayMillis = 1200)
                        )
                ) {
                    Text(
                        text = "Rate your payment experience",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
                    )
                    if (isReviewed) {
                        Text(
                            text = "Thank you for your review",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                                ),
                            textAlign = TextAlign.Start,
                            color = if (isSystemInDarkTheme()) dark_Success else light_Success,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(5) { count ->
                                Box(
                                    modifier = Modifier
                                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._35sdp))
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                        .clickable {
                                            isReviewed = true
                                        },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = (count + 1).toString(),
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Your rating is valuable to us, Please review it based on your experience",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
                                .alpha(.6f),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        )
                    }
                }
            }

            item {
                FilledTonalButton(
                    onClick = { onBackClick() },
                    modifier = Modifier
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._35sdp))
                        .animateItem(
                            fadeInSpec = tween(delayMillis = 1200)
                        ),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults
                        .filledTonalButtonColors(
                            containerColor = if (isSystemInDarkTheme()) dark_SuccessContainer else light_SuccessContainer,
                            contentColor = if (isSystemInDarkTheme()) dark_Success else light_Success
                        )
                ) {
                    Text(
                        text = "Go to Home Screen",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp
                    )
                }
            }
        }
    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumPaymentSuccessScreenPreview() {
    InsightifyTheme {
        Surface {
            MediumPaymentSuccessScreen()
        }
    }
}