package com.infomerica.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.newconversation.ConversationScreen
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.newconversation.ConversationViewModel
import com.infomerica.insightify.ui.composables.home.HomeViewModel
import com.infomerica.insightify.ui.composables.shared.SharedViewModel
import timber.log.Timber

data object ConversationScreenSpec : HomeSpec {
    override val route: String
        get() = HomeScreens.ConversationScreen.route

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
        val conversationViewModel : ConversationViewModel = hiltViewModel()
        val chatUiState by conversationViewModel.conversationResponseUiState.collectAsStateWithLifecycle()
        val conversations by conversationViewModel.conversationUiState.collectAsStateWithLifecycle()
        val conversationUiState by conversationViewModel.conversationSavingUIState.collectAsStateWithLifecycle()
        DisposableEffect(key1 = Unit) {
            Timber.tag("TEST").d("composed")
            onDispose {

            }
        }
        ConversationScreen(
            navController,
            chatUiState,
            conversationUiState,
            conversations,
            onConversationEvent = conversationViewModel::onTriggerEvent
        )
    }


    override fun enterTransition(): EnterTransition {
        return slideInHorizontally()
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally()
    }


}