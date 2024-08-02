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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.GREEN_DOMINATED_EXAMPLE
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infomericainc.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomericainc.insightify.ui.composables.recentOrderRating.variants.CompactRecentOrderReviewScreen
import com.infomericainc.insightify.ui.composables.recentOrderRating.variants.MediumRecentOrderReviewScreen
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CalculateWindowSize

@Composable
fun RecentOrderReviewScreen(
    navController: NavController,
    recentOrderUiState: RecentOrderUiState,
    windowWidthSizeClass: WindowWidthSizeClass,
    onEvent: (RecentOrderRatingEvent) -> Unit,
) {
    RecentOrderReviewScreenBody(navController) {
        CalculateWindowSize(
            windowWidthSizeClass = windowWidthSizeClass,
            compactContent = {
                CompactRecentOrderReviewScreen(
                    paddingValues = it,
                    navController = navController,
                    recentOrderUiState = recentOrderUiState,
                    onEvent = onEvent
                )
            },
            mediumContent = {
                MediumRecentOrderReviewScreen(
                    paddingValues = it,
                    navController = navController,
                    recentOrderUiState = recentOrderUiState,
                    onEvent = onEvent
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
            ),
            windowWidthSizeClass = WindowWidthSizeClass.Compact
        ) {

        }
    }
}
