package com.infomericainc.insightify.ui.composables.login

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.infomericainc.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomericainc.insightify.ui.composables.login.varinets.CompactLoginScreenContent
import com.infomericainc.insightify.ui.composables.login.varinets.MediumLoginScreenContent
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.util.CalculateWindowSize

@Composable
fun LoginScreen(
    navController: NavController,
    windowWidthSizeClass: WindowWidthSizeClass,
    userRegistrationUiState: UserRegistrationUiState,
    event: (LoginEvents) -> Unit
) {
    LoginScreenBody {
        CalculateWindowSize(
            windowWidthSizeClass = windowWidthSizeClass,
            compactContent = {
                CompactLoginScreenContent(
                    paddingValues = it,
                    navController = navController,
                    userRegistrationUiState = userRegistrationUiState
                ) { loginEvents ->
                    event(loginEvents)
                }
            },
            mediumContent = {
                MediumLoginScreenContent(
                    paddingValues = it,
                    navController = navController,
                    userRegistrationUiState = userRegistrationUiState
                ) { loginEvents ->
                    event(loginEvents)
                }
            },
            unSupportedContent = {
                UnSupportedResolutionPlaceHolder()
            }
        )
    }
}

@Composable
private fun LoginScreenBody(
    content: @Composable (PaddingValues) -> Unit
) {
    InsightifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            content(it)
        }
    }
}
