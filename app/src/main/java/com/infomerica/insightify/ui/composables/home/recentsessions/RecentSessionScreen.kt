package com.infomerica.insightify.ui.composables.home.recentsessions

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.infomerica.insightify.R
import com.infomerica.insightify.extension.makeToast
import com.infomerica.insightify.ui.components.InstfyLottie
import com.infomerica.insightify.ui.composables.home.HomeEvent
import com.infomerica.insightify.ui.composables.home.RecentSessionUiState
import com.infomerica.insightify.ui.composables.shared.SharedEvent
import com.infomerica.insightify.ui.navigation.home.HomeScreens
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun RecentSessionsScreen(
    navController: NavController,
    recentSessionUiState: RecentSessionUiState,
    onSharedEvent: (SharedEvent) -> Unit,
    onEvent: (HomeEvent) -> Unit
) {
    RecentSessionsScreenBody(navController) {
        RecentSessionScreenContent(
            it,
            navController,
            recentSessionUiState,
            onSharedEvent,
            onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecentSessionsScreenBody(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)
    InsightifyTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                LargeTopAppBar(
                    title = { Text(text = "Your Recent Sessions") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "")
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
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
private fun RecentSessionScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    recentSessionUiState: RecentSessionUiState,
    onSharedEvent: (SharedEvent) -> Unit,
    onEvent: (HomeEvent) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val isInDarkMode = isSystemInDarkTheme()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    SideEffect {
        systemUiController.setStatusBarColor(
            color = surfaceColor,
            darkIcons = !isInDarkMode
        )
    }

    var onFavoriteClick by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        onEvent(HomeEvent.FetchRecentSessionDataFromRoom)
    }

    LaunchedEffect(key1 = recentSessionUiState) {
        Timber.tag("RECENT_UI").d(recentSessionUiState.toString())
    }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            recentSessionUiState.isLoading -> {
                item {
                    InstfyLottie(
                        resource = R.raw.loading,
                        modifier = Modifier
                            .width(120.dp)
                            .height(60.dp)
                    )

                }
            }

            recentSessionUiState.recentSessions != null -> {
                itemsIndexed(
                    recentSessionUiState.recentSessions,
                ) { index, item ->
                    ConstraintLayout(
                        modifier = Modifier
                            .padding(top = if (index == 0) 20.dp else 10.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 20.dp)
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable {
                                onSharedEvent(
                                    SharedEvent.SaveRecentSessionId(
                                        recentSessionUiState.recentSessions[index].sessionId
                                    )
                                )
                                coroutineScope.launch {
                                    delay(400)
                                    navController.navigate(HomeScreens.RecentConversationScreen.route)
                                }
                            }
                    ) {
                        val (title, date, star) = createRefs()
                        Text(
                            text = item.title,
                            modifier = Modifier.constrainAs(title) {
                                start.linkTo(parent.start, margin = 20.dp)
                                top.linkTo(parent.top, margin = 15.dp)
                            },
                            fontFamily = poppinsFontFamily,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Text(
                            text = "last seen ${item.lastSeen}",
                            modifier = Modifier
                                .constrainAs(date) {
                                    start.linkTo(parent.start, margin = 20.dp)
                                    top.linkTo(title.bottom, margin = 1.dp)
                                    bottom.linkTo(parent.bottom, margin = 15.dp)
                                }
                                .alpha(.7f),
                            fontFamily = poppinsFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        FilledTonalIconButton(
                            onClick = {
                                onEvent(
                                    HomeEvent.UpdateFavouriteToFirebase(
                                        recentSessionUiState.recentSessions[index].sessionId
                                    )
                                )
                                //TODO: Fix this after test.
                                onFavoriteClick = !onFavoriteClick
                                context.makeToast("Added to Favourites")
                            },
                            modifier = Modifier.constrainAs(star) {
                                end.linkTo(parent.end, margin = 20.dp)
                                top.linkTo(title.top)
                                bottom.linkTo(date.bottom)
                            },
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Icon(
                                imageVector = if (item.starred or onFavoriteClick) Icons.Rounded.Star else Icons.Rounded.StarOutline,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            recentSessionUiState.error != null -> {

            }
        }
    }

}

@Composable
@Preview(
    showBackground = true,
    device = Devices.PIXEL_7,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
)
@Preview(
    showBackground = true,
    device = Devices.PIXEL,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
)
private fun HomeScreenPreview() {
    InsightifyTheme {
        RecentSessionsScreen(
            navController = rememberNavController(),
            RecentSessionUiState(isLoading = true),
            onSharedEvent = {},
            onEvent = {}
        )
    }
}