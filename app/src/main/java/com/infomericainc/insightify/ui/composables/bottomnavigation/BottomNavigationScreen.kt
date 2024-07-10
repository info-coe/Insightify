package com.infomericainc.insightify.ui.composables.bottomnavigation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.infomericainc.insightify.ui.composables.home.HomeViewModel
import com.infomericainc.insightify.ui.composables.shared.SharedViewModel
import com.infomericainc.insightify.ui.navigation.Graphs
import com.infomericainc.insightify.ui.navigation.downloads.DownloadScreens
import com.infomericainc.insightify.ui.navigation.downloads.DownloadsSpec
import com.infomericainc.insightify.ui.navigation.home.HomeScreens
import com.infomericainc.insightify.ui.navigation.home.HomeSpec
import com.infomericainc.insightify.ui.navigation.profile.ProfileScreens
import com.infomericainc.insightify.ui.navigation.profile.ProfileSpec
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.MediumThemedPreviewProvider
import timber.log.Timber

@Composable
fun BottomNavigationScreen(
    windowWidthSizeClass: WindowWidthSizeClass
) {
    val bottomNavController = rememberNavController()
    BottomNavigationBody(bottomNavController, windowWidthSizeClass) {
        NavigationScreenContent(it, windowWidthSizeClass, bottomNavController)
    }
}

@Composable
private fun BottomNavigationBody(
    navController: NavController,
    windowWidthSizeClass: WindowWidthSizeClass,
    content: @Composable (PaddingValues) -> Unit
) {
    val localConfiguration = LocalConfiguration.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navDestination = navBackStackEntry?.destination

    var selectedNavigationItem by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navigationItems = BottomNavigationBarItem.bottomNavigationItemsList()
    LaunchedEffect(
        key1 = navDestination?.route,
    ) {
        Timber
            .tag(NAVIGATION)
            .i("Changed to ${navDestination?.route} in bottom navigation Bar")
    }

    val canShownBottomBar = navDestination?.route in listOf(
        HomeScreens.HomeScreen.route,
        DownloadScreens.DownloadsScreen.route,
        ProfileScreens.ProfileScreen.route,
    )




    InsightifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            bottomBar = {
                AnimatedVisibility(
                    visible = canShownBottomBar,
                    enter = fadeIn(tween(700)) + expandIn(tween(500), clip = true)
                ) {
                    NavigationBar {
                        navigationItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = item.route == navDestination?.route,
                                icon = {
                                    Icon(
                                        if (index == selectedNavigationItem) item.selectedIcon else item.unSelectedIcon,
                                        contentDescription = item.name,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        item.name,
                                        fontFamily = poppinsFontFamily
                                    )
                                },
                                onClick = {
                                    selectedNavigationItem = index
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            },
            content = content
        )
    }
}

@Composable
private fun NavigationScreenContent(
    paddingValues: PaddingValues,
    windowWidthSizeClass: WindowWidthSizeClass,
    bottomNavController: NavHostController
) {
    val sharedViewModel: SharedViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    NavHost(
        navController = bottomNavController,
        route = Graphs.BOTTOM_NAV_GRAPH,
        startDestination = Graphs.HOME_GRAPH,
        modifier = Modifier.padding(paddingValues),
    ) {
        homeGraph(bottomNavController, homeViewModel, sharedViewModel, windowWidthSizeClass)
        downloadsGraph(bottomNavController)
        profileGraph(bottomNavController,windowWidthSizeClass)
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    homeViewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    navigation(
        route = Graphs.HOME_GRAPH,
        startDestination = HomeScreens.HomeScreen.route
    ) {
        HomeSpec.allScreens.forEach { homeScreenSpec ->
            composable(
                route = homeScreenSpec.route,
                arguments = homeScreenSpec.arguments,
                deepLinks = homeScreenSpec.deepLinks
            ) {
                homeScreenSpec.Content(
                    navController,
                    it,
                    homeViewModel,
                    sharedViewModel,
                    windowWidthSizeClass
                )
            }
        }
    }
}

fun NavGraphBuilder.downloadsGraph(navController: NavController) {
    navigation(
        route = Graphs.DOWNLOADS_GRAPH,
        startDestination = DownloadScreens.DownloadsScreen.route
    ) {
        DownloadsSpec.allScreens.forEach { downloadsScreenSpec ->
            composable(
                route = downloadsScreenSpec.route
            ) {
                downloadsScreenSpec.Content(
                    navController = navController,
                    navBackStackEntry = it
                )
            }
        }
    }
}

fun NavGraphBuilder.profileGraph(
    navController: NavController,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    navigation(
        route = Graphs.PROFILE_GRAPH,
        startDestination = ProfileScreens.ProfileScreen.route
    ) {
        ProfileSpec.allScreens.forEach { profileScreenSpec ->
            composable(
                route = profileScreenSpec.route
            ) {
                profileScreenSpec.Content(
                    navController = navController,
                    navBackStackEntry = it,
                    windowWidthSizeClass
                )
            }
        }
    }
}

@Composable
@CompactThemedPreviewProvider
private fun CompactHomeScreenPreview() {
    InsightifyTheme {
        BottomNavigationScreen(
            windowWidthSizeClass = WindowWidthSizeClass.Compact
        )
    }
}

@Composable
@MediumThemedPreviewProvider
private fun MediumHomeScreenPreview() {
    InsightifyTheme {
        BottomNavigationScreen(
            windowWidthSizeClass = WindowWidthSizeClass.Medium
        )
    }
}


private const val NAVIGATION = "NavigationDestination"