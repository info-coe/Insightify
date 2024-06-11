package com.infomerica.insightify.ui.composables.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.GREEN_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.infomerica.insightify.R
import com.infomerica.insightify.extension.makeToast
import com.infomerica.insightify.ui.activites.MainActivity
import com.infomerica.insightify.ui.components.InstfyLottie
import com.infomerica.insightify.ui.components.dialog.InstfyWarningDialog
import com.infomerica.insightify.ui.components.text.InstfyBoldText
import com.infomerica.insightify.ui.components.text.InstfyRegularText
import com.infomerica.insightify.ui.composables.home.recentsessions.RecentSessionListModel
import com.infomerica.insightify.ui.composables.home.recentsessions.RecentSessionModel
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserProfileDto
import com.infomerica.insightify.ui.composables.shared.SharedEvent
import com.infomerica.insightify.ui.navigation.home.HomeScreens
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import com.intuit.sdp.R.dimen as DP
import com.intuit.ssp.R.dimen as SP

@Composable
fun HomeScreen(
    navController: NavController,
    userConfigurationUiState: UserConfigurationUiState,
    recentSessionUiState: RecentSessionUiState,
    userProfileUiState: UserProfileUiState,
    updateFavouriteUiState: RecentSessionFavouriteUiState,
    onEvent: (HomeEvent) -> Unit,
    onSharedEvent: (SharedEvent) -> Unit
) {
    HomeScreenBody {
        HomeScreenContent(
            it,
            navController,
            onEvent,
            onSharedEvent,
            userProfileUiState,
            userConfigurationUiState,
            recentSessionUiState,
            updateFavouriteUiState
        )
    }
}

@Composable
private fun HomeScreenBody(
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
private fun HomeScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    onEvent: (HomeEvent) -> Unit,
    onSharedEvent: (SharedEvent) -> Unit,
    userProfileUiState: UserProfileUiState,
    userConfigurationUiState: UserConfigurationUiState,
    recentSessionUiState: RecentSessionUiState,
    updateFavouriteUiState: RecentSessionFavouriteUiState,
) {
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val isInDarkMode = isSystemInDarkTheme()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var onFavoriteClick by remember {
        mutableStateOf(false)
    }

    var sessionAlertDialog by remember {
        mutableStateOf(false)
    }

    var errorAlertDialog by remember {
        mutableStateOf(false)
    }

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

    //TODO: Need to optimize this.
    LaunchedEffect(key1 = Unit) {
        onEvent(HomeEvent.FetchRecentSessionDataFromRoom)
    }

    var freeSessions by rememberSaveable {
        mutableStateOf(-1)
    }

    var premiumUser by rememberSaveable {
        mutableStateOf(false)
    }


    LaunchedEffect(
        key1 = userConfigurationUiState.userConfigurationDto,
        key2 = userConfigurationUiState.error
    ) {
        Timber.tag("HOME_UI_STATE")
            .d("user config changed and $userConfigurationUiState")
        when {
            userConfigurationUiState.isLoading -> {
                Timber.tag("HOME_UI_STATE").d("Loading")
            }

            userConfigurationUiState.userConfigurationDto?.freeSessions != null -> {
                if (freeSessions == -1) {
                    freeSessions = userConfigurationUiState.userConfigurationDto.freeSessions
                }
                premiumUser = userConfigurationUiState.userConfigurationDto.premiumUser.takeIf {
                    it != null
                } ?: false
            }

            userConfigurationUiState.error != null -> {
                userConfigurationUiState.error.takeIf { it.isNotEmpty() }?.let { _ ->
                    errorAlertDialog = true
                }
            }
        }
    }
    SideEffect {
        systemUiController.setStatusBarColor(
            color = surfaceColor,
            darkIcons = !isInDarkMode
        )
    }

    if (sessionAlertDialog) {
        InstfyWarningDialog(
            onPositiveFeedBack = {
                if (freeSessions == 0) {
                    freeSessions = 0
                    //TODO : ADD buy premium Dialog.
                    context.makeToast("Buy premium is in development.")
                } else {
                    sessionAlertDialog = false
                    freeSessions -= 1
                    coroutineScope.launch {
                        delay(300)
                        navController.navigate(HomeScreens.ConversationScreen.route)
                    }
                }
            },
            onNegativeFeedBack = { sessionAlertDialog = false },
            icon = Icons.Rounded.Warning,
            iconProperties = Pair(
                MaterialTheme.colorScheme.errorContainer,
                MaterialTheme.colorScheme.onErrorContainer
            ),
            title = buildAnnotatedString {
                append("Session limit warning")
            },
            description = buildAnnotatedString {
                append("You got")
                withStyle(
                    SpanStyle(color = MaterialTheme.colorScheme.error)
                ) {
                    append(" $freeSessions ")
                }
                append("left. Buy")
                withStyle(
                    SpanStyle(color = MaterialTheme.colorScheme.primary)
                ) {
                    append(" premium ")
                }
                append("to remove the limit.")
            },
            positiveText = "I will see later",
            negativeText = "never mind"
        )
    }

    if (errorAlertDialog) {
        InstfyWarningDialog(
            onPositiveFeedBack = {
                (context as MainActivity?)?.finish()
            },
            onNegativeFeedBack = { sessionAlertDialog = false },
            icon = Icons.Rounded.Error,
            iconProperties = Pair(
                MaterialTheme.colorScheme.errorContainer,
                MaterialTheme.colorScheme.onErrorContainer
            ),
            title = buildAnnotatedString {
                append("Error loading your data")
            },
            description = buildAnnotatedString {
                append("We are facing issue in loading")
                withStyle(
                    SpanStyle(color = MaterialTheme.colorScheme.error)
                ) {
                    append(" your data ")
                }
                append("try restarting the application or")
                withStyle(
                    SpanStyle(color = MaterialTheme.colorScheme.primary)
                ) {
                    append(" login ")
                }
                append("again to resolve this issue.")
            },
            positiveText = "Exit application",
            negativeText = ""
        )
    }

    val recentSessionsLoading: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InstfyLottie(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(120.dp)
                    .height(60.dp),
                resource = R.raw.loading,
                reverseOnRepeat = false,
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Loading things for you.",
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(.9f),
                fontSize = 22.sp,
                lineHeight = 30.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Text(
                text = "We looking for your recent sessions please it. This may take while depends on your internet connection.",
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 20.dp)
                    .fillMaxWidth(.9f)
                    .alpha(.7f),
                fontSize = 16.sp,
                lineHeight = 25.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }

    val recentSessionError: @Composable () -> Unit = {

    }
    val recentSessionListModel = remember {
        mutableListOf(
            RecentSessionListModel()
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
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
        //Lo
        item {
            Image(
                painter = painterResource(id = R.drawable.company_logo),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .height(
                        dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)
                    )
            )
        }
        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = DP._20sdp))
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
                                progress = .5f,
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
                        maxLines = 2
                    )
                }
            }
        }
        item {
            LazyRow(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                            .width(
                                dimensionResource(
                                    com.intuit.sdp.R.dimen._250sdp
                                )
                            )
                            .clickable {
                                if (premiumUser.not()) {
                                    if (freeSessions > 0) {
                                        sessionAlertDialog = true
                                    }
                                } else {
                                    navController.navigate(HomeScreens.ConversationScreen.route)
                                }
                            },
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    dimensionResource(
                                        id = com.intuit.sdp.R.dimen._20sdp
                                    )
                                ),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(
                                dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                        ) {
                            Text(
                                text = "NxtGen AI",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Yes! our NxtGen AI is free. Tap here to try now.",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Normal,
                                maxLines = 4,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(
                                            dimensionResource(id = com.intuit.sdp.R.dimen._40sdp)
                                        )
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceDim)
                                        .aspectRatio(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.company_logo),
                                        contentDescription = "",
                                        modifier = Modifier.width(
                                            dimensionResource(id = com.intuit.sdp.R.dimen._22sdp)
                                        )
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(
                                        dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                                    )
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Infomerica",
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "updated on 26/02",
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                    }
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                            )
                            .width(
                                dimensionResource(
                                    com.intuit.sdp.R.dimen._250sdp
                                )
                            )
                            .clickable {
                                navController.navigate(
                                    HomeScreens.GenericAssistantScreen.route
                                )
                            },
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    dimensionResource(
                                        id = com.intuit.sdp.R.dimen._20sdp
                                    )
                                ),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(
                                dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                        ) {
                            Text(
                                text = "Restaurant AI",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Yes! our NxtGen AI is free. Tap here to try now.",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Normal,
                                maxLines = 4,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(
                                            dimensionResource(id = com.intuit.sdp.R.dimen._40sdp)
                                        )
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceDim)
                                        .aspectRatio(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.company_logo),
                                        contentDescription = "",
                                        modifier = Modifier.width(
                                            dimensionResource(id = com.intuit.sdp.R.dimen._22sdp)
                                        )
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(
                                        dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                                    )
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Infomerica",
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "updated on 26/02",
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            InstfyBoldText(
                text = "Recent sessions",
                fontSize = 26,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                lineHeight = dimensionResource(id = SP._20ssp).value
            )
        }
        when {
            recentSessionUiState.isLoading -> {
                item {
                    recentSessionsLoading()
                }
            }

            recentSessionUiState.recentSessions != null -> {
                recentSessionUiState.recentSessions.takeIf { it.isNotEmpty() }
                    ?.let { recentSessionModels ->
                        recentSessionModels.chunked(2)[0].forEach {
                            recentSessionListModel.add(
                                RecentSessionListModel(
                                    it, false
                                )
                            )
                        }
                        itemsIndexed(
                            recentSessionModels.chunked(2)[0],
                        ) { index, item ->
                            LaunchedEffect(key1 = updateFavouriteUiState) {
                                when {
                                    updateFavouriteUiState.isUpdating -> {

                                    }

                                    updateFavouriteUiState.updatedSuccessfully -> {
                                        recentSessionListModel.getOrNull(index)?.let {
                                            it.isFavouriteLoading = false
                                        }
                                    }
                                }
                            }
                            ConstraintLayout(
                                modifier = Modifier
                                    .padding(
                                        top = if (index == 0) dimensionResource(id = com.intuit.sdp.R.dimen._18sdp) else dimensionResource(
                                            id = com.intuit.sdp.R.dimen._8sdp
                                        )
                                    )
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .clickable {
                                        onSharedEvent(
                                            SharedEvent.SaveRecentSessionId(
                                                recentSessionUiState.recentSessions[index].sessionId
                                            )
                                        )
                                        coroutineScope.launch {
                                            delay(400)
                                            navController.navigate(HomeScreens.RecentConversationScreen.route)
                                        }
                                    }
                            ) {
                                val (title, date, star) = createRefs()
                                Text(
                                    text = item.title,
                                    modifier = Modifier.constrainAs(title) {
                                        start.linkTo(parent.start, margin = 20.dp)
                                        top.linkTo(parent.top, margin = 15.dp)
                                    },
                                    fontFamily = poppinsFontFamily,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )

                                Text(
                                    text = "last seen ${item.lastSeen}",
                                    modifier = Modifier
                                        .constrainAs(date) {
                                            start.linkTo(parent.start, margin = 20.dp)
                                            top.linkTo(title.bottom, margin = 1.dp)
                                            bottom.linkTo(parent.bottom, margin = 15.dp)
                                        }
                                        .alpha(.7f),
                                    fontFamily = poppinsFontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )

                                FilledTonalIconButton(
                                    onClick = {
                                        onEvent(
                                            HomeEvent.UpdateFavouriteToFirebase(
                                                recentSessionUiState.recentSessions[index].sessionId
                                            )
                                        )
                                        recentSessionListModel[index].isFavouriteLoading = true
                                        context.makeToast("Added to Favourites")
                                    },
                                    modifier = Modifier.constrainAs(star) {
                                        end.linkTo(parent.end, margin = 20.dp)
                                        top.linkTo(title.top)
                                        bottom.linkTo(date.bottom)
                                    },
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    )
                                ) {
                                    if (recentSessionListModel.getOrNull(index)?.isFavouriteLoading == true) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(10.dp),
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    } else {
                                        Icon(
                                            imageVector = if (item.starred) ImageVector.vectorResource(
                                                R.drawable.star_filled
                                            ) else ImageVector.vectorResource(
                                                R.drawable.star_outline
                                            ),
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.onSecondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            if (recentSessionUiState.recentSessions.size > 2) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(horizontal = 30.dp)
                                        .padding(top = 10.dp, bottom = 15.dp)
                                        .clip(MaterialTheme.shapes.large)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .clickable {
                                            navController.navigate(HomeScreens.RecentConversationListScreen.route)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "see more",
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        fontFamily = poppinsFontFamily,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    } ?: run {
                    item {
                        ConstraintLayout(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 20.dp)
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        ) {
                            val (icon, title, description) = createRefs()

                            Icon(
                                imageVector = Icons.Rounded.Chat,
                                contentDescription = "chat",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(35.dp)
                                    .constrainAs(icon) {
                                        centerHorizontallyTo(parent)
                                        top.linkTo(parent.top, margin = 20.dp)
                                    }
                            )

                            Text(
                                text = "No conversation found.",
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .constrainAs(title) {
                                        top.linkTo(icon.bottom, margin = 10.dp)
                                        start.linkTo(icon.start)
                                        end.linkTo(icon.end)
                                    },
                                textAlign = TextAlign.Center,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "Your conversation with our NextGen AI will appear here.",
                                modifier = Modifier
                                    .padding(horizontal = 25.dp)
                                    .alpha(.8f)
                                    .constrainAs(description) {
                                        top.linkTo(title.bottom, margin = 5.dp)
                                        start.linkTo(title.start)
                                        end.linkTo(title.end)
                                        bottom.linkTo(parent.bottom, margin = 20.dp)
                                    },
                                textAlign = TextAlign.Center,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }

            recentSessionUiState.error != null -> {
                item {
                    recentSessionError()
                }
            }
        }
    }
}

@Composable
fun Composable.getSP(value: Int) {

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
private fun HomeScreenPreview() {
    InsightifyTheme {
        HomeScreen(
            navController = rememberNavController(),
            userConfigurationUiState = UserConfigurationUiState(isLoading = false),
            recentSessionUiState = RecentSessionUiState(
                isLoading = false,
                recentSessions = RecentSessionModel.demoData,
                error = ""
            ),
            userProfileUiState = UserProfileUiState(
                isLoading = false,
                userProfileDto = UserProfileDto(
                    profileUrl = "https://images.pexels.com/photos/11867612/pexels-photo-11867612.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                    username = "Bharadwaj\nsdhshdsjkd"
                )
            ),
            updateFavouriteUiState = RecentSessionFavouriteUiState(isUpdating = false),
            onEvent = {}
        ) {

        }
    }
}

private const val RECENT_SESSIONS_ANIMATION = "recent_session_animation"