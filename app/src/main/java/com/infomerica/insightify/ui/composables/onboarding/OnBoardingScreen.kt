package com.infomerica.insightify.ui.composables.onboarding

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.infomerica.insightify.R
import com.infomerica.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomerica.insightify.ui.navigation.on_boarding.OnBoardingScreens
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider
import com.infomerica.insightify.util.MediumThemedPreviewProvider

@Composable
fun OnBoardingScreen(
    navController: NavController,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    val localConfiguration = LocalConfiguration.current
    OnBoardingScreenBody {
        when (windowWidthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                OnBoardingScreenPortraitContent(
                    it, navController
                )
            }

            WindowWidthSizeClass.Medium -> {
                if (localConfiguration.orientation == ORIENTATION_PORTRAIT) {
                    OnBoardingScreenTabletContent(
                        paddingValues = it,
                        navController = navController
                    )
                } else {
                    UnSupportedResolutionPlaceHolder()
                }
            }

            WindowWidthSizeClass.Expanded -> {
                if (localConfiguration.orientation == ORIENTATION_PORTRAIT) {
                    OnBoardingScreenTabletContent(
                        paddingValues = it,
                        navController = navController
                    )
                } else {
                    UnSupportedResolutionPlaceHolder()
                }
            }
        }
    }
}

@Composable
private fun OnBoardingScreenBody(
    content: @Composable (PaddingValues) -> Unit
) {
    InsightifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            content(it)
        }
    }
}

@Composable
private fun OnBoardingScreenPortraitContent(
    paddingValues: PaddingValues,
    navController: NavController
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colorScheme.surface,
        darkIcons = !isSystemInDarkTheme()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = "Welcome Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                .weight(.4f)
        )
        Column(
            modifier = Modifier
                .weight(.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Welcome to")
                    withStyle(
                        SpanStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.error
                                )
                            )
                        )
                    ) {
                        append(" Insightify")
                    }
                },
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    ),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._32ssp).value.sp
            )
            Text(
                text = buildAnnotatedString {
                    append("Your NxtGen AI Restaurant Chat bot designed, for your")
                    withStyle(
                        SpanStyle(
                            alpha = 1f,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.error
                                )
                            ),
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(" restaurant needs.")
                    }
                },
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp
            )
            Button(
                onClick = { navController.navigate(OnBoardingScreens.LoginScreen.route) },
                modifier = Modifier
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._50sdp))
            ) {
                Text(
                    text = "Get started",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                )
            }
        }
    }

}

@Composable
private fun OnBoardingScreenTabletContent(
    paddingValues: PaddingValues,
    navController: NavController
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colorScheme.surface,
        darkIcons = !isSystemInDarkTheme()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = "Welcome Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._60sdp))
                .weight(.5f)
        )
        Column(
            modifier = Modifier
                .weight(.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Welcome to")
                    withStyle(
                        SpanStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.error
                                )
                            )
                        )
                    ) {
                        append(" Insightify ")
                    }
                },
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    ),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp
            )
            Text(
                text = buildAnnotatedString {
                    append("Your NxtGen AI Restaurant Chat bot designed, for your")
                    withStyle(
                        SpanStyle(
                            alpha = 1f,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.error
                                )
                            ),
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(" restaurant needs.")
                    }
                },
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp
            )
            Button(
                onClick = { navController.navigate(OnBoardingScreens.LoginScreen.route) },
                modifier = Modifier
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._60sdp))
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._35sdp))
            ) {
                Text(
                    text = "Get started",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
                )
            }

        }
    }
}


@Composable
@CompactThemedPreviewProvider
private fun CompactPreview() {
    InsightifyTheme {
        OnBoardingScreen(
            navController = rememberNavController(),
            windowWidthSizeClass = WindowWidthSizeClass.Compact
        )
    }
}

@Composable
@MediumThemedPreviewProvider
private fun MediumPreview() {
    InsightifyTheme {
        OnBoardingScreen(
            navController = rememberNavController(),
            windowWidthSizeClass = WindowWidthSizeClass.Medium
        )
    }
}