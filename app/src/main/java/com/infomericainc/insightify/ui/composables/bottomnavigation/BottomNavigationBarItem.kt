package com.infomericainc.insightify.ui.composables.bottomnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.infomericainc.insightify.R
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
                    selectedIcon = ImageVector.vectorResource(R.drawable.home_filled),
                    unSelectedIcon = ImageVector.vectorResource(R.drawable.home_outline)
                ),
                BottomNavigationBarItem(
                    name = "Downloads",
                    route = Graphs.DOWNLOADS_GRAPH,
                    selectedIcon = ImageVector.vectorResource(R.drawable.downloads_filled),
                    unSelectedIcon = ImageVector.vectorResource(R.drawable.downloads_outline),
                ),
                BottomNavigationBarItem(
                    name = "Profile",
                    route = Graphs.PROFILE_GRAPH,
                    selectedIcon = ImageVector.vectorResource(R.drawable.user),
                    unSelectedIcon = ImageVector.vectorResource(R.drawable.user),
                ),
            )
        }
    }
}
