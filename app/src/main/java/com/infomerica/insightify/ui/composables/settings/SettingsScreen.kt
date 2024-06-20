package com.infomerica.insightify.ui.composables.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.infomerica.insightify.R
import com.infomerica.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomerica.insightify.ui.composables.settings.varients.CompactSettingsScreen
import com.infomerica.insightify.ui.composables.settings.varients.MediumSettingsScreen
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CalculateWindowSize
import com.infomerica.insightify.util.CompactThemedPreviewProvider
import com.infomerica.insightify.util.MediumThemedPreviewProvider

@Composable
fun SettingsScreen(
    navController: NavController,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    val uiController = rememberSystemUiController()
    uiController
        .setStatusBarColor(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            darkIcons = !isSystemInDarkTheme()
        )
    SettingsScreenBody(navController, windowWidthSizeClass) {
        CalculateWindowSize(
            windowWidthSizeClass = windowWidthSizeClass,
            compactContent = {
                CompactSettingsScreen(
                    it,
                    navController
                )
            },
            mediumContent = {
                MediumSettingsScreen(
                    it,
                    navController
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
fun SettingsScreenBody(
    navController: NavController,
    windowWidthSizeClass: WindowWidthSizeClass,
    content: @Composable (PaddingValues) -> Unit
) {
    val isCompact = windowWidthSizeClass == WindowWidthSizeClass.Compact
    InsightifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                if (isCompact) {
                    LargeTopAppBar(
                        title = {
                            Text(
                                text = "Settings",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "")
                            }
                        },
                        colors = TopAppBarDefaults
                            .largeTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                        windowInsets = WindowInsets(
                            left = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                        )
                    )
                } else {
                    LargeTopAppBar(
                        title = {
                            Text(
                                text = "Settings",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                                )
                            }
                        },
                        colors = TopAppBarDefaults
                            .largeTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                        windowInsets = WindowInsets(
                            left = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                        )
                    )
                }
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

@CompactThemedPreviewProvider
@Composable
private fun CompactSettingsScreenContentPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SettingsScreen(
                navController = rememberNavController(),
                windowWidthSizeClass = WindowWidthSizeClass.Compact
            )
        }
    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumSettingsScreenContentPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SettingsScreen(
                navController = rememberNavController(),
                windowWidthSizeClass = WindowWidthSizeClass.Medium
            )
        }
    }
}