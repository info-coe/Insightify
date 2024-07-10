package com.infomericainc.insightify.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.infomericainc.insightify.ui.composables.home.HomeViewModel
import com.infomericainc.insightify.ui.composables.shared.SharedViewModel
import com.infomericainc.insightify.ui.composables.transaction.TransactionScreen
import com.infomericainc.insightify.ui.composables.transaction.TransactionViewModel
import timber.log.Timber

data object TransactionScreenSpec : HomeSpec {
    override val route: String
        get() = HomeScreens.TransactionScreen.route

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
        val transactionViewModel: TransactionViewModel = hiltViewModel()
        val transactionUiState by transactionViewModel.transactionUiState.collectAsStateWithLifecycle()
        val transactionUpdateUIState by transactionViewModel.transactionUpdateUIState.collectAsStateWithLifecycle()
        val amount = navBackStackEntry.arguments?.getString("amount")?.toDoubleOrNull()
        val currency = navBackStackEntry.arguments?.getString("currency")
        TransactionScreen(
            windowWidthSizeClass = windowWidthSizeClass,
            navController = navController,
            transactionUiState = transactionUiState,
            transactionUpdateUIState = transactionUpdateUIState,
            amount = amount!!,
            currency = currency!!,
            onTransactionEvent = transactionViewModel::onEvent
        )
    }


    override fun enterTransition(): EnterTransition {
        return slideInHorizontally()
    }

    override fun exitTransition(): ExitTransition {
        return slideOutHorizontally()
    }


}