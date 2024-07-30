package com.infomericainc.insightify.ui.composables.home

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.infomericainc.insightify.R
import com.infomericainc.insightify.extension.makeToast
import com.infomericainc.insightify.ui.components.InstfyLottie
import com.infomericainc.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomericainc.insightify.ui.components.text.InstfyRegularText
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserProfileDto
import com.infomericainc.insightify.ui.composables.shared.SharedEvent
import com.infomericainc.insightify.ui.navigation.home.HomeScreens
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CalculateWindowSize
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.Constants
import com.infomericainc.insightify.util.MediumThemedPreviewProvider
import com.infomericainc.insightify.util.redirectToWebsite
import com.intuit.sdp.R.dimen as DP
import com.intuit.ssp.R.dimen as SP

@Composable
fun RestaurantHomeScreen(
    navController: NavController,
    userConfigurationUiState: UserConfigurationUiState,
    userProfileUiState: UserProfileUiState,
    windowWidthSizeClass: WindowWidthSizeClass,
    onEvent: (HomeEvent) -> Unit,
    onSharedEvent: (SharedEvent) -> Unit
) {
    RestaurantHomeScreenBody {
        CalculateWindowSize(
            windowWidthSizeClass = windowWidthSizeClass,
            compactContent = {
                CompactRestaurantHomeScreenContent(
                    it,
                    navController,
                    onEvent,
                    onSharedEvent,
                    userProfileUiState,
                    userConfigurationUiState
                )
            },
            mediumContent = {
                MediumRestaurantHomeScreenContent(
                    it,
                    navController,
                    onEvent,
                    onSharedEvent,
                    userProfileUiState,
                    userConfigurationUiState
                )
            },
            unSupportedContent = {
                UnSupportedResolutionPlaceHolder()
            }
        )
    }
}

@Composable
private fun RestaurantHomeScreenBody(
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
                visible = !isRecomposed,
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
private fun CompactRestaurantHomeScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    onEvent: (HomeEvent) -> Unit,
    onSharedEvent: (SharedEvent) -> Unit,
    userProfileUiState: UserProfileUiState,
    userConfigurationUiState: UserConfigurationUiState,
) {
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val isInDarkMode = isSystemInDarkTheme()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current


    var homeController by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = homeController) {
        if (homeController.not()) {
            onEvent(HomeEvent.FetchUserConfigurationFromRoom)
            onEvent(HomeEvent.FetchUserProfileFromRoom)
            homeController = true
        }
    }

    systemUiController.setStatusBarColor(
        color = surfaceColor,
        darkIcons = !isInDarkMode
    )
    systemUiController.setSystemBarsColor(
        color = surfaceColor,
        darkIcons = !isInDarkMode
    )




    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
            .fillMaxSize()
    ) {
        item {
            AnimatedVisibility(
                visible = userConfigurationUiState.isLoading,
                label = "home_loading"
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    InstfyLottie(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .width(80.dp)
                            .height(40.dp),
                        resource = R.raw.loading,
                        reverseOnRepeat = false,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.company_logo),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .height(
                        dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)
                    )
            )
        }
        item {
            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = DP._15sdp))
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(dimensionResource(id = DP._45sdp))
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        userProfileUiState.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp),
                                progress = { .5f },
                                strokeWidth = 3.dp,
                                trackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        }

                        userProfileUiState.userProfileDto != null -> {
                            AsyncImage(
                                model = userProfileUiState.userProfileDto.profileUrl ?: "",
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.Start
                ) {
                    InstfyRegularText(
                        text = "Hello,",
                        fontSize = dimensionResource(id = SP._16ssp).value.toInt()
                    )
                    Text(
                        text = userProfileUiState.userProfileDto?.username ?: "",
                        modifier = Modifier
                            .wrapContentSize(),
                        fontFamily = poppinsFontFamily,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = dimensionResource(id = SP._18ssp).value.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        lineHeight = dimensionResource(id = SP._22ssp).value.sp,
                        maxLines = 2,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.error
                                )
                            )
                        )
                    )
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val homeBannerTitle = buildAnnotatedString {
                    append("Step into our")
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(" restaurant ")
                    }
                    append("and let our")
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(" AI ")
                    }
                    append("be your culinary guide, where every bite is a taste of the")
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(" future")
                    }
                    append(".")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._180sdp))
                        .clip(MaterialTheme.shapes.large)
                        .clickable {
                            navController.navigate(
                                HomeScreens.GenericAssistantScreen.route
                            )
                        }
                        .alpha(.9f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_banner),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .blur(1.dp),
                        contentScale = ContentScale.Crop,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black)
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                            )
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.BottomEnd),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                    ) {
                        Text(
                            text = homeBannerTitle,
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .alpha(.8f),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Try now",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._17ssp).value.sp
                            )

                            Icon(
                                imageVector = Icons.Rounded.ArrowForward,
                                contentDescription = "",
                                tint = Color.White
                            )

                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp)))
        }
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                item {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable {
                                    navController
                                        .navigate(
                                            HomeScreens.RecentOrderScreen.route
                                        )
                                }
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Timelapse,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                            )
                        }
                        Text(
                            text = "Recent\norders",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable {
                                    uriHandler
                                        .openUri(Constants.PLAY_STORE_LINK)
                                }
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.StarBorder,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                            )
                        }
                        Text(
                            text = "Rate\nus",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable {
                                    navController.navigate(HomeScreens.AboutUsScreenSpec.route)
                                }
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Restaurant,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                            )
                        }
                        Text(
                            text = "About\nus",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable {
                                    context.makeToast("Will avail soon")
                                }
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.OpenInBrowser,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                            )
                        }
                        Text(
                            text = "Test\nin",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }
        item {
            Column(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.webiste_banner),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._120sdp))
                )
                Text(
                    text = "Insightify website is now here to manage your orders for restaurants",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp
                )
                Text(
                    text = "We introducing you, Insightify website interface for restaurant managemnent, To manage the orders form insightify application.",
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                        .alpha(.8f),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
                )
                Button(
                    onClick = { context.redirectToWebsite("http://44.197.221.249:5173/react_firebase") },
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                            bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults
                        .buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                ) {
                    Text(
                        text = "Explore Now",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                    )
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
                    )
                }
            }
        }
    }
}

@Composable
private fun MediumRestaurantHomeScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    onEvent: (HomeEvent) -> Unit,
    onSharedEvent: (SharedEvent) -> Unit,
    userProfileUiState: UserProfileUiState,
    userConfigurationUiState: UserConfigurationUiState,
) {
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val isInDarkMode = isSystemInDarkTheme()
    val context = LocalContext.current


    var homeController by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = homeController) {
        if (homeController.not()) {
            onEvent(HomeEvent.FetchUserConfigurationFromRoom)
            onEvent(HomeEvent.FetchUserProfileFromRoom)
            homeController = true
        }
    }

    systemUiController.setStatusBarColor(
        color = surfaceColor,
        darkIcons = !isInDarkMode
    )
    systemUiController.setSystemBarsColor(
        color = surfaceColor,
        darkIcons = !isInDarkMode
    )




    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
            .fillMaxSize()
    ) {
        item {
            AnimatedVisibility(
                visible = userConfigurationUiState.isLoading,
                label = "home_loading",
                enter = slideInVertically(
                    tween(700),
                    initialOffsetY = {
                        it / 3
                    }
                ) + fadeIn(tween(700)),
                exit = slideOutVertically(
                    tween(700)
                ) + fadeOut(tween(700))

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    InstfyLottie(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .width(80.dp)
                            .height(40.dp),
                        resource = R.raw.loading,
                        reverseOnRepeat = false,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.company_logo),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .height(
                        dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
            )
        }
        item {
            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = DP._15sdp))
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(dimensionResource(id = DP._30sdp))
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        userProfileUiState.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(dimensionResource(id = DP._20sdp)),
                                progress = { .5f },
                                strokeWidth = 3.dp,
                                trackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        }

                        userProfileUiState.userProfileDto != null -> {
                            AsyncImage(
                                model = userProfileUiState.userProfileDto.profileUrl ?: "",
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(start = dimensionResource(id = DP._10sdp))
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.Start
                ) {
                    InstfyRegularText(
                        text = "Hello,",
                        fontSize = dimensionResource(id = SP._8ssp).value.toInt()
                    )
                    Text(
                        text = userProfileUiState.userProfileDto?.username ?: "",
                        modifier = Modifier
                            .wrapContentSize(),
                        fontFamily = poppinsFontFamily,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = dimensionResource(id = SP._10ssp).value.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        lineHeight = dimensionResource(id = SP._14ssp).value.sp,
                        maxLines = 2,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.error
                                )
                            )
                        )
                    )
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val homeBannerTitle = buildAnnotatedString {
                    append("Step into our")
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(" restaurant ")
                    }
                    append("and let our")
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(" AI ")
                    }
                    append("be your culinary guide, where every bite is a taste of the")
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(" future")
                    }
                    append(".")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._120sdp))
                        .clip(MaterialTheme.shapes.large)
                        .clickable {
                            navController.navigate(
                                HomeScreens.GenericAssistantScreen.route
                            )
                        }
                        .alpha(.9f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_banner),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .blur(1.dp),
                        contentScale = ContentScale.Crop,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black)
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                            )
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.BottomEnd),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                    ) {
                        Text(
                            text = homeBannerTitle,
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .alpha(.8f),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Try now",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                            )

                            Icon(
                                imageVector = Icons.Rounded.ArrowForward,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(dimensionResource(id = DP._12sdp))
                            )

                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp)))
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                ) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable {
                                navController
                                    .navigate(
                                        HomeScreens.RecentOrderScreen.route
                                    )
                            }
                            .height(dimensionResource(id = com.intuit.sdp.R.dimen._35sdp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Timelapse,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .width(maxWidth / (2.5).toInt())
                                .height(maxHeight / (2.5).toInt())
                        )
                    }
                    Text(
                        text = "Recent orders",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                        lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                    )
                }
                Spacer(modifier = Modifier.weight(.1f))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                ) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .padding(end = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable {
                                Intent(Intent.ACTION_MAIN).also {
                                    it.`package` = "com.google.android.googlequicksearchbox"
                                    try {
                                        context.startActivity(it)
                                    } catch (e: ActivityNotFoundException) {
                                        context.makeToast("Unable to launch playStore")
                                    }
                                }
                            }
                            .height(dimensionResource(id = com.intuit.sdp.R.dimen._35sdp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .width(maxWidth / (2.5).toInt())
                                .height(maxHeight / (2.5).toInt())
                        )
                    }
                    Text(
                        text = "Rate us",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                        lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                ) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable {
                                navController
                                    .navigate(
                                        HomeScreens.AboutUsScreenSpec.route
                                    )
                            }
                            .height(dimensionResource(id = com.intuit.sdp.R.dimen._35sdp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Restaurant,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .width(maxWidth / (2.5).toInt())
                                .height(maxHeight / (2.5).toInt())
                        )
                    }
                    Text(
                        text = "About us",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                        lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                    )
                }
                Spacer(modifier = Modifier.weight(.1f))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                ) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .padding(end = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable {
                                context.makeToast("Will avail soon.")
                            }
                            .height(dimensionResource(id = com.intuit.sdp.R.dimen._35sdp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .width(maxWidth / (2.5).toInt())
                                .height(maxHeight / (2.5).toInt())
                        )
                    }
                    Text(
                        text = "Ai Guide",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                        lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)))
        }
        item {
            Column(
                modifier = Modifier
                    .padding(
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.webiste_banner),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                )
                Text(
                    text = "Insightify website is now here to manage your orders for restaurants.",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._17ssp).value.sp
                )
                Text(
                    text = "We introducing you, Insightify website interface for restaurant managemnent, To manage the orders form insightify application.",
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                        .alpha(.8f),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                )
                Button(
                    onClick = { context.redirectToWebsite("http://44.197.221.249:5173/react_firebase") },
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                            bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults
                        .buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                ) {
                    Text(
                        text = "Explore Now",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
                    )
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp))
                    )
                }
            }
        }

    }
}


@Composable
@CompactThemedPreviewProvider
private fun CompactRestaurantHomeScreenPreview() {
    InsightifyTheme {
        RestaurantHomeScreen(
            navController = rememberNavController(),
            userConfigurationUiState = UserConfigurationUiState(isLoading = false),
            windowWidthSizeClass = WindowWidthSizeClass.Compact,
            userProfileUiState = UserProfileUiState(
                isLoading = false,
                userProfileDto = UserProfileDto(
                    profileUrl = "https://images.pexels.com/photos/11867612/pexels-photo-11867612.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                    username = "Bharadwaj\nsdhshdsjkd"
                )
            ),
            onEvent = {}
        ) {

        }
    }
}

@Composable
@MediumThemedPreviewProvider
private fun MediumRestaurantHomeScreenPreview() {
    InsightifyTheme {
        RestaurantHomeScreen(
            navController = rememberNavController(),
            userConfigurationUiState = UserConfigurationUiState(isLoading = false),
            windowWidthSizeClass = WindowWidthSizeClass.Medium,
            userProfileUiState = UserProfileUiState(
                isLoading = false,
                userProfileDto = UserProfileDto(
                    profileUrl = "https://images.pexels.com/photos/11867612/pexels-photo-11867612.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                    username = "Bharadwaj"
                )
            ),
            onEvent = {}
        ) {

        }
    }
}

private const val RECENT_SESSIONS_ANIMATION = "recent_session_animation"