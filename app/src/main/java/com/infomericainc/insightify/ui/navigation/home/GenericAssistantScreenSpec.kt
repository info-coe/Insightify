package com.infomericainc.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomericainc.insightify.ui.composables.genericassistant.AssistantViewModel
import com.infomericainc.insightify.ui.composables.genericassistant.GenericAssistantScreen
import com.infomericainc.insightify.ui.composables.home.HomeViewModel
import com.infomericainc.insightify.ui.composables.shared.SharedViewModel
import timber.log.Timber

data object GenericAssistantScreenSpec : HomeSpec {
    override val route: String
        get() = HomeScreens.GenericAssistantScreen.route

    override val arguments: List<NamedNavArgument>
        get() = super.arguments

    override val deepLinks: List<NavDeepLink>
        get() = super.deepLinks

    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry,
        homeViewModel: HomeViewModel,
        sharedViewModel: SharedViewModel,
        windowWidthSizeClass: WindowWidthSizeClass
    ) {
        DisposableEffect(key1 = Unit) {
            Timber
                .tag(GENERIC_ASSISTANT_SCREEN)
                .i("Created")
            onDispose {
                Timber
                    .tag(GENERIC_ASSISTANT_SCREEN)
                    .i("Recomposed")
            }
        }
        val assistantViewModel: AssistantViewModel = hiltViewModel()
        val assistantResponseUiState by assistantViewModel.assistantResponseUiState.collectAsStateWithLifecycle()
        val previousConversationUiState by assistantViewModel.previousConversationUiState.collectAsStateWithLifecycle()
        val assistantConversationUiState by assistantViewModel.conversationUiState.collectAsStateWithLifecycle()
        val deleteConversationUiState by assistantViewModel.conversationDeletionUiState.collectAsStateWithLifecycle()
        val conversationRestrictionUIState by assistantViewModel.conversationRestrictionUIState.collectAsStateWithLifecycle()


        GenericAssistantScreen(
            navController = navController,
            assistantResponseUiState,
            previousConversationUiState,
            deleteConversationUiState,
            assistantConversationUiState,
            conversationRestrictionUIState,
            windowWidthSizeClass,
            onAssistantEvent = { assistantEvent ->
                assistantViewModel.onEvent(
                    assistantEvent
                )
            },
        )
    }


    override fun enterTransition(): EnterTransition {
        return slideInHorizontally()
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally()
    }


}

private const val GENERIC_ASSISTANT_SCREEN = "GenericAssistantScreen"