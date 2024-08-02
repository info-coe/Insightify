package com.infomericainc.insightify.ui.composables.bottomnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.infomericainc.insightify.ui.navigation.Graphs

data class BottomNavigationBarItem(
    val name : String = "",
    val route : String = "",
    val selectedIcon: ImageVector = Icons.Rounded.Home,
    val unSelectedIcon : ImageVector = Icons.Outlined.Home
) {
    companion object {
        @Composable
        fun bottomNavigationItemsList() : List<BottomNavigationBarItem>{
            return listOf(
                BottomNavigationBarItem(
                    name = "Home",
                    route = Graphs.HOME_GRAPH,
                    selectedIcon = Icons.Filled.Home,
                    unSelectedIcon = Icons.Outlined.Home
                ),
                BottomNavigationBarItem(
                    name = "Guide",
                    route = Graphs.GUIDE_GRAPH,
                    selectedIcon = Icons.Filled.Info,
                    unSelectedIcon = Icons.Outlined.Info,
                ),
                BottomNavigationBarItem(
                    name = "Profile",
                    route = Graphs.PROFILE_GRAPH,
                    selectedIcon = Icons.Filled.Person,
                    unSelectedIcon = Icons.Outlined.Person,
                ),
            )
        }
    }
}
