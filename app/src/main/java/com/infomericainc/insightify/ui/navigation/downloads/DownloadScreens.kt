package com.infomericainc.insightify.ui.navigation.downloads


sealed class DownloadScreens(val route : String) {
    data object DownloadsScreen : DownloadScreens("downloads_screen")
}
