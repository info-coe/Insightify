package com.infomerica.insightify.util

import android.content.res.Configuration
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun CalculateWindowSize(
    windowWidthSizeClass: WindowWidthSizeClass,
    compactContent: @Composable () -> Unit,
    mediumContent: @Composable () -> Unit,
    unSupportedContent: @Composable () -> Unit
) {
    val localConfiguration = LocalConfiguration.current
    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> compactContent()

        WindowWidthSizeClass.Medium -> {
            if (localConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mediumContent()
            } else {
                unSupportedContent()
            }
        }

        WindowWidthSizeClass.Expanded -> {
            if (localConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mediumContent()
            } else {
                unSupportedContent()
            }
        }
    }

}