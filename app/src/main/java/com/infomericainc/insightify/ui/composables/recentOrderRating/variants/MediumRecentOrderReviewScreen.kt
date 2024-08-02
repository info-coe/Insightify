package com.infomericainc.insightify.ui.composables.recentOrderRating.variants

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infomericainc.insightify.extension.makeToast
import com.infomericainc.insightify.ui.composables.recentOrderRating.RecentOrderRatingEvent
import com.infomericainc.insightify.ui.composables.recentOrderRating.RecentOrderUiState
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.MediumThemedPreviewProvider
import timber.log.Timber

@Composable
fun MediumRecentOrderReviewScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavController,
    recentOrderUiState: RecentOrderUiState,
    onEvent: (RecentOrderRatingEvent) -> Unit
) {
    var isReviewed by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        onEvent(RecentOrderRatingEvent.FetchRecentOrdersFromFirebase)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        item {
            Text(
                text = "How would you like to rate your conversation with our Assistant.",
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
            )
        }
        item {
            AnimatedVisibility(visible = !isReviewed) {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "",
                        modifier = Modifier
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
                            .weight(.2f),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Thank you, for your review.",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(.8f),
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
                    )
                }
            }
            if (!isReviewed) {
                LazyRow(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        ),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                ) {
                    items(5) {
                        IconButton(onClick = {
                            isReviewed = true
                            onEvent(
                                RecentOrderRatingEvent.SaveAssistantConversationRatingToFirebase(
                                    it
                                )
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.StarBorder,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
                                    .clip(CircleShape),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Like what you ate?",
                modifier = Modifier
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
            )
        }

        item {
            Box(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(
                        dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
            ) {
                Text(
                    text = "Rating functionality is still in development, We will fix this on next updates.",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }


        when {
            recentOrderUiState.isLoading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                        )
                    }
                }
            }


            recentOrderUiState.orders != null -> {
                val recentOrders = recentOrderUiState.orders
                Timber
                    .tag("RECENT_ORDER_RATING_UI")
                    .d(recentOrders.toString())
                itemsIndexed(recentOrders) { index, item ->
                    Row(
                        modifier = Modifier
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                            modifier = Modifier.weight(.6f)
                        )
                        Row(
                            modifier = Modifier.weight(.4f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                onEvent(
                                    RecentOrderRatingEvent.UpdateItemLikeCount(
                                        recentOrders[index]
                                    )
                                )
                                context.makeToast("Response collected for ${recentOrders[index]}")
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.ThumbUp,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = {
                                onEvent(
                                    RecentOrderRatingEvent.UpdateDisLikeCount(
                                        recentOrders[index]
                                    )
                                )
                                context.makeToast("Response collected for ${recentOrders[index]}")
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.ThumbDown,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            recentOrderUiState.error != null -> {

            }
        }
    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumRecentOrderReviewScreenPreview() {
    InsightifyTheme {
        Surface {
            MediumRecentOrderReviewScreen(
                navController = rememberNavController(),
                paddingValues = PaddingValues(),
                recentOrderUiState = RecentOrderUiState(
                    isLoading = false,
                    orders = listOf(
                        "Veg Biryani",
                        "Tomoato soup"
                    )
                )
            ) {

            }
        }
    }
}