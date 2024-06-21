package com.infomerica.insightify.ui.composables.login

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.Identity
import com.infomerica.insightify.extension.makeToast
import com.infomerica.insightify.manager.PreferencesManager
import com.infomerica.insightify.ui.components.dialog.InstfyProgressDialog
import com.infomerica.insightify.ui.components.login.CompactLoginForm
import com.infomerica.insightify.ui.components.login.CompactSignInOptions
import com.infomerica.insightify.ui.components.login.MediumLoginDivider
import com.infomerica.insightify.ui.components.login.MediumLoginForm
import com.infomerica.insightify.ui.components.login.MediumSignInOptions
import com.infomerica.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomerica.insightify.ui.composables.login.google_sign_in.GoogleAuthUIClient
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserProfileDto
import com.infomerica.insightify.ui.navigation.Graphs
import com.infomerica.insightify.ui.navigation.on_boarding.OnBoardingScreens
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider
import com.infomerica.insightify.util.Constants
import com.infomerica.insightify.util.MediumThemedPreviewProvider
import com.intuit.ssp.R
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun LoginScreen(
    navController: NavController,
    windowWidthSizeClass: WindowWidthSizeClass,
    userRegistrationUiState: UserRegistrationUiState,
    event: (LoginEvents) -> Unit
) {
    LoginScreenBody {
        val localConfiguration = LocalConfiguration.current
        LoginScreenBody {
            when (windowWidthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    CompactLoginScreenContent(
                        paddingValues = it,
                        navController = navController,
                        userRegistrationUiState = userRegistrationUiState
                    ) { loginEvents ->
                        event(loginEvents)
                    }
                }

                WindowWidthSizeClass.Medium -> {
                    if (localConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        MediumLoginScreenContent(
                            paddingValues = it,
                            navController = navController,
                            userRegistrationUiState = userRegistrationUiState
                        ) { loginEvents ->
                            event(loginEvents)
                        }
                    } else {
                        UnSupportedResolutionPlaceHolder()
                    }
                }

                WindowWidthSizeClass.Expanded -> {
                    if (localConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        MediumLoginScreenContent(
                            paddingValues = it,
                            navController = navController,
                            userRegistrationUiState = userRegistrationUiState
                        ) { loginEvents ->
                            event(loginEvents)
                        }
                    } else {
                        UnSupportedResolutionPlaceHolder()
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginScreenBody(
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CompactLoginScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    userRegistrationUiState: UserRegistrationUiState,
    event: (LoginEvents) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val isInDarkMode = isSystemInDarkTheme()
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = surfaceColor,
            darkIcons = !isInDarkMode
        )
    }

    val googleAuthUIClient by lazy {
        GoogleAuthUIClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }


    var showProgressDialog by remember {
        mutableStateOf(false)
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                lifeCycleOwner.lifecycleScope.launch {
                    val signInResult = googleAuthUIClient.loginWithIntent(
                        intent = activityResult.data ?: return@launch
                    )
                    showProgressDialog = true
                    event(LoginEvents.SendUserDataToFireStore(signInResult))
                }
            } else {
                Timber.tag("CODE").e(activityResult.resultCode.toString())
                context.makeToast("Google sign in error - ${activityResult.resultCode}")
            }
        }
    )

    LaunchedEffect(
        key1 = userRegistrationUiState.isLoading,
        key2 = userRegistrationUiState.userDataDto.userId,
        key3 = userRegistrationUiState.error
    ) {
        when {
            userRegistrationUiState.isLoading -> {
                showProgressDialog = true
            }

            userRegistrationUiState.userDataDto.userId != null -> {
                showProgressDialog = false
                preferencesManager.saveData(key = Constants.IS_USER_LOGGED_IN, true)
                navController.navigate(Graphs.BOTTOM_NAV_GRAPH) {
                    popUpTo(OnBoardingScreens.LoginScreen.route) {
                        inclusive = true
                    }
                }
            }

            userRegistrationUiState.error.isNotEmpty() -> {
                showProgressDialog = false
                context.makeToast("Error retrieving your data, try again later.")
            }
        }
    }

    if (showProgressDialog) {
        InstfyProgressDialog(
            title = "Setting up things for you.",
            description = "We saving your data, this may take while depends on your internet connection.",
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Top
    ) {
        stickyHeader {
            Text(
                text = buildAnnotatedString {
                    append("Let's")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(" sign")
                    }
                    append("\nyou in.")
                },
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = poppinsFontFamily,
                fontSize = dimensionResource(id = R.dimen._28ssp).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                    .padding(
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp),
                    )
                    .fillMaxWidth(),
                lineHeight = dimensionResource(id = R.dimen._35ssp).value.sp,
                letterSpacing = 1.sp
            )
        }
        item {
            CompactLoginForm {
                navController.navigate(Graphs.BOTTOM_NAV_GRAPH) {
                    popUpTo(OnBoardingScreens.LoginScreen.route) {
                        inclusive = true
                    }
                }
                preferencesManager.saveData(
                    key = Constants.IS_USER_LOGGED_IN,
                    true
                )
            }
        }
        item {
            Row(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.width(
                        dimensionResource(id = com.intuit.sdp.R.dimen._65sdp)
                    )
                )
                Text(
                    text = "or",
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._14sdp)
                    ),
                    textAlign = TextAlign.Center,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(
                    modifier = Modifier.width(
                        dimensionResource(id = com.intuit.sdp.R.dimen._65sdp)
                    )
                )
            }
        }
        item {
            CompactSignInOptions(
                onGoogleSignIn = {
                    context.makeToast("Processing your request.")
                    lifeCycleOwner.lifecycleScope.launch {
                        val signInIntentSender = googleAuthUIClient.logIn()
                        googleSignInLauncher.launch(
                            IntentSenderRequest
                                .Builder(
                                    signInIntentSender ?: return@launch
                                )
                                .build()
                        )
                    }
                },
                onAppleSignIn = {
                    context.makeToast("will avail soon.")
                },
                onGithubSignIn = {
                    context.makeToast("will avail soon.")
                }
            )
        }
        item {
            Text(
                text = "By singing in you agree to our terms & conditions.",
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp),
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp)
                    ),
                textAlign = TextAlign.Center,
                fontFamily = poppinsFontFamily,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                fontWeight = FontWeight.Light,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediumLoginScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    userRegistrationUiState: UserRegistrationUiState,
    event: (LoginEvents) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val isInDarkMode = isSystemInDarkTheme()
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = surfaceColor,
            darkIcons = !isInDarkMode
        )
    }

    val googleAuthUIClient by lazy {
        GoogleAuthUIClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }


    var showProgressDialog by remember {
        mutableStateOf(false)
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                lifeCycleOwner.lifecycleScope.launch {
                    val signInResult = googleAuthUIClient.loginWithIntent(
                        intent = activityResult.data ?: return@launch
                    )
                    showProgressDialog = true
                    event(LoginEvents.SendUserDataToFireStore(signInResult))
                }
            } else {
                Timber.tag("CODE").e(activityResult.resultCode.toString())
                context.makeToast("Google sign in error - ${activityResult.resultCode}")
            }
        }
    )

    LaunchedEffect(
        key1 = userRegistrationUiState.isLoading,
        key2 = userRegistrationUiState.userDataDto.userId,
        key3 = userRegistrationUiState.error
    ) {
        when {
            userRegistrationUiState.isLoading -> {
                showProgressDialog = true
            }

            userRegistrationUiState.userDataDto.userId != null -> {
                showProgressDialog = false
                preferencesManager.saveData(key = Constants.IS_USER_LOGGED_IN, true)
                navController.navigate(Graphs.BOTTOM_NAV_GRAPH) {
                    popUpTo(OnBoardingScreens.LoginScreen.route) {
                        inclusive = true
                    }
                }
            }

            userRegistrationUiState.error.isNotEmpty() -> {
                showProgressDialog = false
                context.makeToast("Error retrieving your data, try again later.")
            }
        }
    }

    if (showProgressDialog) {
        InstfyProgressDialog(
            title = "Setting up things for you.",
            description = "We saving your data, this may take while depends on your internet connection.",
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            Text(
                text = buildAnnotatedString {
                    append("Let's")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(" sign")
                    }
                    append("\nyou in.")
                },
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = poppinsFontFamily,
                fontSize = dimensionResource(id = R.dimen._16ssp).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                    .padding(
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp),
                    )
                    .fillMaxWidth(),
                lineHeight = dimensionResource(id = R.dimen._22ssp).value.sp,
                letterSpacing = 1.sp
            )
        }
        item {
            MediumLoginForm {
                navController.navigate(Graphs.BOTTOM_NAV_GRAPH) {
                    popUpTo(OnBoardingScreens.LoginScreen.route) {
                        inclusive = true
                    }
                }
                preferencesManager.saveData(
                    key = Constants.IS_USER_LOGGED_IN,
                    true
                )
            }
        }
        item {
            MediumLoginDivider()
        }
        item {
            MediumSignInOptions(
                onGoogleSignIn = {
                    lifeCycleOwner.lifecycleScope.launch {
                        val signInIntentSender = googleAuthUIClient.logIn()
                        googleSignInLauncher.launch(
                            IntentSenderRequest
                                .Builder(
                                    signInIntentSender ?: return@launch
                                )
                                .build()
                        )
                    }
                },
                onAppleSignIn = {
                    context.makeToast("will avail soon.")
                },
                onGithubSignIn = {
                    context.makeToast("will avail soon.")
                }
            )
        }
        item {
            Text(
                text = "By signing in you agree to our terms & conditions.",
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp),
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp)
                    ),
                textAlign = TextAlign.Center,
                fontFamily = poppinsFontFamily,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                fontWeight = FontWeight.Light,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
            )
        }
    }
}

@Composable
@CompactThemedPreviewProvider
private fun CompactLoginScreenPreview() {
    InsightifyTheme {
        LoginScreen(
            navController = rememberNavController(),
            windowWidthSizeClass = WindowWidthSizeClass.Compact,
            userRegistrationUiState = UserRegistrationUiState(
                userDataDto = UserProfileDto(userId = "878")
            )
        ) {

        }
    }
}

@Composable
@MediumThemedPreviewProvider
private fun MediumLoginScreenPreview() {
    InsightifyTheme {
        LoginScreen(
            navController = rememberNavController(),
            windowWidthSizeClass = WindowWidthSizeClass.Medium,
            userRegistrationUiState = UserRegistrationUiState(
                userDataDto = UserProfileDto(userId = "878")
            )
        ) {

        }
    }
}