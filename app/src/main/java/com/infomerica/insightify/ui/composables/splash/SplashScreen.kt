package com.infomerica.insightify.ui.composables.splash

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Copyright
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.GREEN_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infomerica.insightify.R
import com.infomerica.insightify.manager.PreferencesManager
import com.infomerica.insightify.ui.components.InstfyCompanyLogo
import com.infomerica.insightify.ui.components.SetSystemSettings
import com.infomerica.insightify.ui.components.text.InstfyMediumText
import com.infomerica.insightify.ui.components.text.InstfyRegularText
import com.infomerica.insightify.ui.navigation.Graphs
import com.infomerica.insightify.ui.navigation.on_boarding.OnBoardingScreens
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.Constants
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    SplashScreenBody {
        SplashScreenContent(it, navController)
    }
}

@Composable
private fun SplashScreenBody(
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
            ) {
                content(it)
            }
        }
    }
}

@Composable
private fun SplashScreenContent(
    paddingValues: PaddingValues,
    navController: NavController
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val isLoggedIn by remember {
        mutableStateOf(preferencesManager.getData(Constants.IS_USER_LOGGED_IN, false))
    }
    LaunchedEffect(key1 = Unit) {
        delay(700L)
        if (isLoggedIn) {
            navController.navigate(
                Graphs.BOTTOM_NAV_GRAPH
            ) {
                this.popUpTo(OnBoardingScreens.SplashScreen.route) {
                    this.inclusive = true
                }
            }
        } else {
            navController.navigate(
                OnBoardingScreens.WelcomeScreen.route
            ) {
                this.popUpTo(OnBoardingScreens.SplashScreen.route) {
                    this.inclusive = true
                }
            }
        }
    }


    SetSystemSettings()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        val (logo, companyLogo) = createRefs()
        InstfyMediumText(
            text = stringResource(id = R.string.str_splash_text),
            modifier = Modifier.constrainAs(logo) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent, bias = .5f)
            },
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._40ssp).value.toInt(),
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._55ssp).value.toInt(),
        )
        InstfyCompanyLogo(
            imageId = R.drawable.company_logo,
            modifier = Modifier
                .constrainAs(companyLogo) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(
                        parent.bottom,
                        margin = 60.dp
                    )
                }
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
    device = Devices.PIXEL_C,
    uiMode = UI_MODE_NIGHT_YES,
    wallpaper = GREEN_DOMINATED_EXAMPLE
)
private fun HomeScreenPreview() {
    InsightifyTheme {
        SplashScreen(navController = rememberNavController())
    }
}