package com.infomericainc.insightify.ui.composables.login.varinets

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.Identity
import com.infomericainc.insightify.R
import com.infomericainc.insightify.extension.makeToast
import com.infomericainc.insightify.manager.PreferencesManager
import com.infomericainc.insightify.ui.components.dialog.InstfyAlertDialog
import com.infomericainc.insightify.ui.components.dialog.InstfyProgressDialog
import com.infomericainc.insightify.ui.composables.login.LoginEvents
import com.infomericainc.insightify.ui.composables.login.UserRegistrationUiState
import com.infomericainc.insightify.ui.composables.login.google_sign_in.GoogleAuthUIClient
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserProfileDto
import com.infomericainc.insightify.ui.navigation.Graphs
import com.infomericainc.insightify.ui.navigation.on_boarding.OnBoardingScreens
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.Constants
import com.infomericainc.insightify.util.MediumThemedPreviewProvider
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MediumLoginScreenContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavController,
    userRegistrationUiState: UserRegistrationUiState,
    event: (LoginEvents) -> Unit,
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    val googleAuthUIClient by lazy {
        GoogleAuthUIClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    var showProgressDialog by remember {
        mutableStateOf(false)
    }

    var showConsetDialog by remember {
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

    if (showConsetDialog) {
        InstfyAlertDialog(
            title = "Permissions Required",
            description = "We need to access your primary account information to provide personalized features. This includes your username, email address, and profile information. Your data will be securely stored in Firestore. Do you consent to this?",
            onPositiveFeedBack = {
                showConsetDialog = false
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
            onNegativeFeedBack = {
                showConsetDialog = false
                context
                    .makeToast("Accept the permissions to continue.")
            },
            positiveText = "Agree",
            negativeText = "Decline"
        )
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_art),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.4f),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Welcome Back!\nYou been missed.",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Let's sign you in to explore wide range of possibles and options.",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .alpha(.6f)
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
        )
        Row(
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalIconButton(
                onClick = {
                    showConsetDialog = true
                },
                modifier = Modifier
                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_google),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
                )
            }
            FilledTonalIconButton(
                onClick = { context.makeToast("In development, Will avail soon") },
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.apple),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
                )
            }
            FilledTonalIconButton(
                onClick = { context.makeToast("In development, Will avail soon") },
                modifier = Modifier
                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.github),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
                )
            }
        }
        Text(
            text = "By signing in you agree to our",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
            textAlign = TextAlign.Center
        )
        Row {
            Text(
                text = "terms",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        uriHandler
                            .openUri(Constants.TERMS_URL)
                    }
            )
            Text(
                text = " and ",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "privacy",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        uriHandler
                            .openUri(Constants.PRIVACY_URL)
                    }
            )
        }
    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumLoginScreenPreview() {
    InsightifyTheme {
        Surface {
            MediumLoginScreenContent(
                paddingValues = PaddingValues(),
                navController = rememberNavController(),
                userRegistrationUiState = UserRegistrationUiState(
                    userDataDto = UserProfileDto(userId = "878")
                )
            ) {

            }
        }
    }
}