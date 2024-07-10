package com.infomericainc.insightify.ui.composables.splash.varients

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infomericainc.insightify.R
import com.infomericainc.insightify.manager.PreferencesManager
import com.infomericainc.insightify.ui.components.SetSystemSettings
import com.infomericainc.insightify.ui.components.text.InstfyMediumText
import com.infomericainc.insightify.ui.navigation.Graphs
import com.infomericainc.insightify.ui.navigation.on_boarding.OnBoardingScreens
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.util.Constants
import com.infomericainc.insightify.util.MediumThemedPreviewProvider
import kotlinx.coroutines.delay

@Composable
fun MediumSplashScreen(
    paddingValues: PaddingValues,
    navController: NavController
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val isLoggedIn by remember {
        mutableStateOf(preferencesManager.getData(Constants.IS_USER_LOGGED_IN, false))
    }
    var initialRotation by remember {
        mutableFloatStateOf(90f)
    }
    var initialScale by remember {
        mutableFloatStateOf(.6f)
    }
    val animation = animateFloatAsState(
        targetValue = initialRotation,
        animationSpec = tween(700),
        label = "logo_rotation"
    )
    val scaleAnimation = animateFloatAsState(
        targetValue = initialScale,
        animationSpec = tween(700),
        label = "logo_scale"
    )
    DisposableEffect(Unit) {
        initialRotation = 0f
        initialScale = 1f
        onDispose {
            initialRotation = 0f
            initialScale = 1.4f
        }
    }

    LaunchedEffect(key1 = Unit) {
        delay(1000L)
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
        val (logoSrc, logo) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier = Modifier
                .constrainAs(logoSrc) {
                    bottom.linkTo(logo.top)
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
                .fillMaxWidth()
                .size(dimensionResource(id = com.intuit.sdp.R.dimen._150sdp))
                .rotate(animation.value)
                .scale(scaleAnimation.value)
        )
        InstfyMediumText(
            text = stringResource(id = R.string.str_splash_text),
            modifier = Modifier.constrainAs(logo) {
                centerHorizontallyTo(parent)
                bottom.linkTo(
                    parent.bottom,
                    margin = 60.dp
                )
            },
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.toInt(),
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._55ssp).value.toInt(),
        )

    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumSplashScreenPreview() {
    InsightifyTheme {
        Surface {
            MediumSplashScreen(
                paddingValues = PaddingValues(),
                navController = rememberNavController()
            )
        }
    }
}