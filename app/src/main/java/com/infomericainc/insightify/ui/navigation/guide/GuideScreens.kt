package com.infomericainc.insightify.ui.navigation.guide


sealed class GuideScreens(val route : String) {
    data object GuideScreen : GuideScreens("guide_screen")
}
