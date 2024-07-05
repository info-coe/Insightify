package com.infomericainc.insightify.ui.composables.genericassistant

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.infomericainc.insightify.extension.makeToast
import com.infomericainc.insightify.ui.components.dialog.InstfyAlertDialog
import com.infomericainc.insightify.ui.components.dialog.InstfyProgressDialog
import com.infomericainc.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomericainc.insightify.ui.composables.genericassistant.varients.CompactGenericAssistantScreenContent
import com.infomericainc.insightify.ui.composables.genericassistant.varients.MediumGenericAssistantScreenContent
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CalculateWindowSize
import com.infomericainc.insightify.util.MediumThemedPreviewProvider

@Composable
fun GenericAssistantScreen(
    navController: NavController,
    assistantResponseUiState: AssistantResponseUiState,
    previousConversationUiState: PreviousConversationUiState,
    deletionUiState: ConversationDeletionUiState,
    assistantConversation: List<AssistantConversationModel>,
    windowWidthSizeClass: WindowWidthSizeClass,
    onAssistantEvent: (AssistantEvent) -> Unit,
) {

    DisposableEffect(key1 = Unit) {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Assistant Screen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
        onDispose { 

        }
    }
    val systemUiController = rememberSystemUiController()
    val isInDarkMode = isSystemInDarkTheme()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        darkIcons = !isInDarkMode
    )
    systemUiController.setNavigationBarColor(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        darkIcons = !isInDarkMode
    )

    GenericAssistantScreenBody(
        navController,
        sessionTitle = "Assistant",
        deletionUiState,
        onAssistantEvent
    ) {
        CalculateWindowSize(
            windowWidthSizeClass = windowWidthSizeClass,
            compactContent = {
                CompactGenericAssistantScreenContent(
                    paddingValues = it,
                    navController = navController,
                    assistantResponseUiState,
                    previousConversationUiState,
                    assistantConversation,
                    onAssistantEvent = onAssistantEvent
                )
            },
            mediumContent = {
                MediumGenericAssistantScreenContent(
                    paddingValues = it,
                    navController = navController,
                    assistantResponseUiState,
                    previousConversationUiState,
                    assistantConversation,
                    onAssistantEvent = onAssistantEvent
                )
            },
            unSupportedContent = {
                UnSupportedResolutionPlaceHolder()
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericAssistantScreenBody(
    navController: NavController,
    sessionTitle: String,
    deletionUiState: ConversationDeletionUiState,
    onAssistantEvent: (AssistantEvent) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {

    var showMenu by remember {
        mutableStateOf(false)
    }

    var showWarningDialog by remember {
        mutableStateOf(false)
    }

    var showDeletionDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = deletionUiState) {
        when {
            deletionUiState.isDeleting -> {
                showWarningDialog = false
                showDeletionDialog = true
            }

            deletionUiState.deleted -> {
                showDeletionDialog = false
                navController.popBackStack()
            }

            deletionUiState.error != null -> {
                showDeletionDialog = false
                context.makeToast(deletionUiState.error)
            }
        }
    }


    InsightifyTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (showWarningDialog) {
                InstfyAlertDialog(
                    onPositiveFeedBack = { onAssistantEvent(AssistantEvent.DeleteConversationFromAssistant) },
                    title = "Clear conversation",
                    description = "Are you sure you want to clear this conversation.",
                    positiveText = "Yes! clear",
                    negativeText = "No Keep",
                    onNegativeFeedBack = { showWarningDialog = false }
                )
            }
            if (showDeletionDialog) {
                InstfyProgressDialog(
                    title = "Clearing your conversation",
                    description = "We are clearing your conversation, This may take while depends on the conversation length.",
                )
            }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                topBar = {
                    TopAppBar(
                        windowInsets = WindowInsets(
                            top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                            bottom = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp)
                        ),
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                                )
                            ) {
                                Text(
                                    text = sessionTitle,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                                    modifier = Modifier
                                        .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                },
                                modifier = Modifier
                                    .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(
                                        dimensionResource(id = com.intuit.sdp.R.dimen._23sdp)
                                    )
                                )
                            }
                        },
                        colors = TopAppBarDefaults
                            .topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                        actions = {
                            IconButton(
                                onClick = { showMenu = !showMenu },
                                modifier = Modifier
                                    .padding(end = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._22sdp))
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Settings,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu != showMenu },
                                properties = PopupProperties(
                                    excludeFromSystemGesture = false,
                                    dismissOnClickOutside = true,
                                    dismissOnBackPress = true
                                )
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "Clear Conversation",
                                            fontFamily = poppinsFontFamily,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Rounded.Clear,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    },
                                    onClick = { showWarningDialog = true }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "About Assistant",
                                            fontFamily = poppinsFontFamily
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Rounded.Info,
                                            contentDescription = "",
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowRight,
                                            contentDescription = "",
                                        )
                                    },
                                    onClick = { showMenu = false }
                                )
                            }
                        },
                    )
                }
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
                            + slideInVertically(tween(800), initialOffsetY = { it / 8 })
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

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
@MediumThemedPreviewProvider
fun MediumRecentConversationScreenPreview() {
    InsightifyTheme {
        GenericAssistantScreen(
            navController = rememberNavController(),
            assistantResponseUiState = AssistantResponseUiState(
                isLoading = false,
                assistantResponse = "INFO9900"
            ),
            assistantConversation = listOf(),
            deletionUiState = ConversationDeletionUiState(),
            windowWidthSizeClass = WindowWidthSizeClass.Medium,
            previousConversationUiState = PreviousConversationUiState(
                isLoading = false,
                previousConversation = null,
                emptyConversation = true
            )
        ) {

        }
    }
}


