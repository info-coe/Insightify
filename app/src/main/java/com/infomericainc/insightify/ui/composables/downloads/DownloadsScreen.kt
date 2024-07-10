package com.infomericainc.insightify.ui.composables.downloads

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeveloperMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.GREEN_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily

@Composable
fun DownloadsScreen(
    navController: NavController
) {
    DownloadsScreenBody {
        DownloadsScreenContent(
            it,
            navController
        )
    }
}

@Composable
private fun DownloadsScreenBody(
    content: @Composable (PaddingValues) -> Unit
) {
    InsightifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {

            var isRecomposed by remember {
                mutableStateOf(false)
            }
            DisposableEffect(key1 = Unit) {
                isRecomposed = true
                onDispose {
                    isRecomposed = false
                }
            }

            AnimatedVisibility(
                visible = isRecomposed,
                enter = fadeIn(tween(500))
                        + slideInVertically(tween(800), initialOffsetY = { -it / 8 })
                        + scaleIn(initialScale = .8f, transformOrigin = TransformOrigin.Center),
                exit = fadeOut(tween(500))
                        + slideOutVertically(tween(800), targetOffsetY = { -it / 9 })
                        + scaleOut(targetScale = .9f, transformOrigin = TransformOrigin.Center)
            ) {
                content(it)
            }
        }
    }
}

@Composable
private fun DownloadsScreenContent(
    paddingValues: PaddingValues,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.DeveloperMode,
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
        )
        Text(
            text = "In development, Will available soon in next releases.",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                    vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                )
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    device = Devices.PIXEL_7,
    wallpaper = GREEN_DOMINATED_EXAMPLE
)
@Preview(
    showBackground = true,
    device = Devices.PIXEL,
    uiMode = UI_MODE_NIGHT_YES,
    wallpaper = GREEN_DOMINATED_EXAMPLE
)
private fun DownloadsScreenPreview() {
    InsightifyTheme {
        DownloadsScreen(
            navController = rememberNavController()
        )
    }
}

private const val RECENT_SESSIONS_ANIMATION = "recent_session_animation"