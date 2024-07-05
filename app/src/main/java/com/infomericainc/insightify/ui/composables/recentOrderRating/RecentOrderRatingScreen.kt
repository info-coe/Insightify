package com.infomericainc.insightify.ui.composables.recentOrderRating

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.GREEN_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import timber.log.Timber

@Composable
fun RecentOrderReviewScreen(
    navController: NavController,
    recentOrderUiState: RecentOrderUiState,
    onEvent: (RecentOrderRatingEvent) -> Unit,
) {
    val systemController = rememberSystemUiController()
    RecentOrderReviewScreenBody(navController) {
        RecentOrderReviewScreenContent(
            it,
            navController,
            recentOrderUiState,
            onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecentOrderReviewScreenBody(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit,
) {
    InsightifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Review",
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
private fun RecentOrderReviewScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    recentOrderUiState: RecentOrderUiState,
    onEvent: (RecentOrderRatingEvent) -> Unit
) {
    var isReviewed by remember {
        mutableStateOf(false)
    }
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
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp
            )
        }
        item {
            AnimatedVisibility(visible = isReviewed) {
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
                        .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "",
                        modifier = Modifier
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                            .weight(.2f),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Thank you, for your review.",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(.8f),
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
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
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
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
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp
            )
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
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                            modifier = Modifier.weight(.6f)
                        )
                        Row(
                            modifier = Modifier.weight(.4f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = { onEvent(RecentOrderRatingEvent.UpdateItemLikeCount(recentOrders[index])) }) {
                                Icon(
                                    imageVector = Icons.Outlined.ThumbUp,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = { onEvent(RecentOrderRatingEvent.UpdateDisLikeCount(recentOrders[index])) }) {
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
private fun RecentOrderReviewScreenPreview() {
    InsightifyTheme {
        RecentOrderReviewScreen(
            navController = rememberNavController(),
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

private const val RECENT_SESSIONS_ANIMATION = "recent_session_animation"