package com.infomericainc.insightify.ui.composables.profile

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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.infomericainc.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserProfileDto
import com.infomericainc.insightify.ui.composables.profile.variants.CompactProfileScreenContent
import com.infomericainc.insightify.ui.composables.profile.variants.MediumProfileScreenContent
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.util.CalculateWindowSize
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun ProfileScreen(
    navController: NavController,
    userProfileUiState: UserProfileUiState,
    widthSizeClass: WindowWidthSizeClass
) {
    val systemUIController = rememberSystemUiController()
    systemUIController.setStatusBarColor(
        MaterialTheme.colorScheme.surface,
        darkIcons = !isSystemInDarkTheme()
    )
    ProfileScreenBody {
        CalculateWindowSize(
            windowWidthSizeClass = widthSizeClass,
            compactContent = {
                CompactProfileScreenContent(
                    it,
                    navController,
                    userProfileUiState
                )
            },
            mediumContent = {
                MediumProfileScreenContent(
                    it,
                    navController,
                    userProfileUiState
                )
            },
            unSupportedContent = {
                UnSupportedResolutionPlaceHolder()
            }
        )
    }
}

@Composable
private fun ProfileScreenBody(
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
@CompactThemedPreviewProvider
private fun ProfileScreenPreview() {
    InsightifyTheme {
        ProfileScreen(
            navController = rememberNavController(),
            UserProfileUiState(
                userProfile = UserProfileDto(
                    username = "Lisa",
                    email = "Lisa@gmail.com"
                )
            ),
            widthSizeClass = WindowWidthSizeClass.Compact
        )
    }
}
