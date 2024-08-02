package com.infomericainc.insightify.ui.composables.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.infomericainc.insightify.ui.composables.splash.variants.CompactSplashScreen
import com.infomericainc.insightify.ui.composables.splash.variants.MediumSplashScreen
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.util.CalculateWindowSize

@Composable
fun SplashScreen(
    navController: NavController,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    SplashScreenBody {
        CalculateWindowSize(
            windowWidthSizeClass = windowWidthSizeClass,
            compactContent = {
                CompactSplashScreen(
                    paddingValues = it,
                    navController = navController
                )
            },
            mediumContent = {
                MediumSplashScreen(
                    paddingValues = it,
                    navController = navController
                )
            },
            unSupportedContent = {}
        )
    }
}

@Composable
private fun SplashScreenBody(
    content: @Composable (PaddingValues) -> Unit
) {
    InsightifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface
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
            ) {
                content(it)
            }
        }
    }
}

