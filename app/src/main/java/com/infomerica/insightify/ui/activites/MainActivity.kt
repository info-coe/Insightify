package com.infomerica.insightify.ui.activites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.infomerica.insightify.ui.composables.bottomnavigation.BottomNavigationScreen
import com.infomerica.insightify.ui.navigation.Graphs
import com.infomerica.insightify.ui.navigation.on_boarding.OnBoardingScreenSpec
import com.infomerica.insightify.ui.navigation.on_boarding.OnBoardingScreens
import com.infomerica.insightify.ui.theme.InsightifyTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAnalytics.getInstance(this)

        setContent {
            InsightifyTheme {
                val insightifyViewModel: InsightifyViewModel = hiltViewModel()
                val apiKey by insightifyViewModel.getApiKey.collectAsStateWithLifecycle()
                val navController = rememberNavController()
                val windowSize = calculateWindowSizeClass(this)
                LaunchedEffect(key1 = apiKey) {
                    if (apiKey.isEmpty()) {
                        Timber.tag(INSIGHTIFY_EVENTS)
                            .i("API KEY Fetched From DATABASE.")
                        insightifyViewModel
                            .onInsightifyEvents(
                                event = InsightifyEvent
                                    .FetchSystemKeys
                            )
                    }
                }
                NavHost(
                    navController = navController,
                    route = Graphs.ON_BOARDING_GRAPH,
                    startDestination = OnBoardingScreens.SplashScreen.route,
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    OnBoardingScreenSpec.allScreens.forEach { onBoardingScreenSpec ->
                        composable(
                            route = onBoardingScreenSpec.route,
                            arguments = onBoardingScreenSpec.arguments,
                            deepLinks = onBoardingScreenSpec.deepLinks,
                            enterTransition = {
                                fadeIn(tween(200)) + slideInHorizontally(tween(500))
                            },
                            exitTransition = null
                        ) {
                            onBoardingScreenSpec.Content(navController, it, windowSize)
                        }
                    }

                    composable(Graphs.BOTTOM_NAV_GRAPH) {
                        BottomNavigationScreen(windowSize.widthSizeClass)
                    }
                }
            }
        }
    }
}

private const val INSIGHTIFY_EVENTS = "InsightifyEvent"
