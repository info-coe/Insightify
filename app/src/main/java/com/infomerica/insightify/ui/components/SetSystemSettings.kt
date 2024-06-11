package com.infomerica.insightify.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Helps to set the System bars Colors
 * according to the device Mode.
 */
@Composable
fun SetSystemSettings() {
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val systemIsInDarkModel = isSystemInDarkTheme()
    SideEffect {
        with(systemUiController) {
            setStatusBarColor(
                color = surfaceColor,
                darkIcons = !systemIsInDarkModel
            )
            setSystemBarsColor(
                color = surfaceColor,
                darkIcons = !systemIsInDarkModel
            )
        }
    }
}