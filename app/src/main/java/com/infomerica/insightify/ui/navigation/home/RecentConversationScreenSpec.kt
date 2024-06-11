package com.infomerica.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.recentconversation.RecentConversationScreen
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.recentconversation.RecentConversationViewModel
import com.infomerica.insightify.ui.composables.home.HomeViewModel
import com.infomerica.insightify.ui.composables.shared.SharedViewModel

data object RecentConversationScreenSpec : HomeSpec {
    override val route: String
        get() = HomeScreens.RecentConversationScreen.route

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
        val recentSessionId by sharedViewModel.getRecentSessionId().collectAsStateWithLifecycle()

        val recentConversationViewModel : RecentConversationViewModel = hiltViewModel()
        val recentConversationUIState by recentConversationViewModel.recentConversationUIState.collectAsStateWithLifecycle()
        val conversationUIState by recentConversationViewModel.conversationUiState.collectAsStateWithLifecycle()
        val conversationResponseUiState by recentConversationViewModel.recentConversationResponseUiState.collectAsStateWithLifecycle()
        val conversationSavingUiState by recentConversationViewModel.recentConversationSavingUIState.collectAsStateWithLifecycle()


        RecentConversationScreen(
            navController,
            title = "",
            recentSessionId = recentSessionId,
            recentConversationUIState = recentConversationUIState,
            recentConversationResponseUiState = conversationResponseUiState,
            recentConversationSavingUiState = conversationSavingUiState,
            conversations = conversationUIState,
            onEvent = recentConversationViewModel::onTriggerEvent
        )
    }


    override fun enterTransition(): EnterTransition {
        return slideInHorizontally()
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally()
    }


}