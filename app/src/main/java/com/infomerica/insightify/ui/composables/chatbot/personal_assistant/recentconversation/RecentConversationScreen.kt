package com.infomerica.insightify.ui.composables.chatbot.personal_assistant.recentconversation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infomerica.insightify.R
import com.infomerica.insightify.extension.makeToast
import com.infomerica.insightify.manager.PDFManager
import com.infomerica.insightify.manager.ResponseFormatter
import com.infomerica.insightify.ui.components.dialog.InstfyProgressDialog
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationMessageModel
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationModel
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationType
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.DEMO_CONVERSATION
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.DropDownItem
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.MessageOrigin
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.MessageType
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ResponseType
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * This composable used for displaying Recent conversation.
 */
@Composable
fun RecentConversationScreen(
    navController: NavController,
    title: String,
    recentSessionId: String,
    recentConversationUIState: RecentConversationUIState,
    recentConversationResponseUiState: RecentConversationResponseUiState,
    conversations: List<ConversationModel>,
    recentConversationSavingUiState: RecentConversationSavingUiState,
    onEvent: (RecentConversationEvent) -> Unit
) {
    var onBackClickTriggered by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = recentSessionId) {
        recentSessionId.takeIf { it.isNotEmpty() }?.let { sessionId ->
            onEvent(
                RecentConversationEvent.FetchRecentConversation(
                    sessionId
                )
            )
        }
    }
    RecentConversationScreenBody(
        title = title,
        recentConversationUIState = recentConversationUIState,
        onBackClick = {
            onBackClickTriggered = !onBackClickTriggered

            Timber.tag(RECENT_CONVERSATION_SCREEN)
                .i("Back click triggered from Top bar")
        }
    ) { paddingValues ->
        RecentConversationScreenContent(
            paddingValues = paddingValues,
            onBackClick = onBackClickTriggered,
            navController = navController,
            recentConversationUiState = recentConversationUIState,
            recentConversationResponseUiState = recentConversationResponseUiState,
            conversations = conversations,
            recentSessionId = recentSessionId,
            recentConversationSavingUiState = recentConversationSavingUiState,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecentConversationScreenBody(
    onBackClick: () -> Unit,
    title: String,
    recentConversationUIState: RecentConversationUIState,
    content: @Composable (PaddingValues) -> Unit
) {

    val scaffoldBlur by remember {
        mutableStateOf(
            recentConversationUIState.takeIf { it.isLoading or it.error.isNotEmpty() }?.let {
                5.dp
            } ?: run { 0.dp }
        )
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .blur(scaffoldBlur),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title.ifEmpty { "Recent session" },
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        }
    ) {
        content(it)
    }
}

@Composable
private fun RecentConversationScreenContent(
    paddingValues: PaddingValues,
    onBackClick: Boolean,
    navController: NavController,
    recentConversationUiState: RecentConversationUIState,
    recentConversationResponseUiState: RecentConversationResponseUiState,
    conversations: List<ConversationModel>,
    recentSessionId: String,
    recentConversationSavingUiState: RecentConversationSavingUiState,
    onEvent: (RecentConversationEvent) -> Unit
) {
    var showLoadingDialog by remember {
        mutableStateOf(false)
    }
    var showErrorDialog by remember {
        mutableStateOf(false)
    }

    var savingDialog by remember {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.typing))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        reverseOnRepeat = true,
        speed = 1.6f
    )

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val pdfManager by remember {
        mutableStateOf(PDFManager(context))
    }

    var userResponse by rememberSaveable {
        mutableStateOf("")
    }

    val onSuccess: @Composable (PaddingValues) -> Unit =
        { paddingValue ->
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxSize()
                    .padding(paddingValue)
                    .imePadding()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    reverseLayout = true
                ) {
                    itemsIndexed(conversations.asReversed()) { index, conversationModel ->
                        index.takeIf { it == 0 }?.let {
                            recentConversationResponseUiState.takeIf { it.isLoading }?.let {
                                Box(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
                                        .wrapContentHeight()
                                        .clip(MaterialTheme.shapes.large)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LottieAnimation(
                                        composition = composition,
                                        progress = {
                                            progress
                                        },
                                        modifier = Modifier
                                            .padding(start = 15.dp, end = 15.dp)
                                            .padding(vertical = 10.dp)
                                            .width(50.dp)
                                            .height(30.dp)
                                            .scale(1.6f)
                                    )
                                }
                            } ?: run {
                                Spacer(modifier = Modifier.height(15.dp))
                            }
                        }
                        when (conversationModel.conversationType) {
                            ConversationType.TEXT -> {
                                ConversationMessageModel(
                                    conversationModel = conversationModel,
                                    onCopyText = {
                                        clipboardManager.setText(buildAnnotatedString { append(it) })
                                        context.makeToast("Content copied to clipboard")
                                    },
                                    onPdfDownload = {
                                        coroutineScope.launch {
                                        }
                                    },
                                    onAnswersGenerate = {
                                        onEvent(
                                            RecentConversationEvent.ExecuteQuery("generate answers for previously generated questions with references links for each answer")
                                        )
                                    }
                                )
                            }

                            else -> {

                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .animateContentSize(tween(700)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .padding(vertical = 15.dp)
                            .fillMaxWidth(.80f)
                            .wrapContentHeight()
                            .clip(MaterialTheme.shapes.extraLarge)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .animateContentSize(
                                animationSpec = tween(
                                    500
                                )
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(
                            visible = userResponse.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.cancel),
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(start = 13.dp)
                                    .size(22.dp)
                                    .clickable {
                                        userResponse = ""
                                    }
                            )
                        }
                        TextField(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .verticalScroll(state = rememberScrollState()),
                            value = userResponse,
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Normal,
                            ),
                            keyboardOptions = KeyboardOptions(
                                autoCorrect = true,
                            ),
                            maxLines = 6,
                            onValueChange = {
                                userResponse = it
                            },
                            placeholder = {
                                Text(
                                    text = "Type your response.",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.alpha(.8f),
                                    fontSize = 16.sp
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
                            ),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                if (userResponse.isNotEmpty()) {
                                    onEvent(
                                        RecentConversationEvent.ExecuteQuery(userResponse.trimEnd())
                                    )
                                    Timber.tag("userResponse").e(userResponse)
                                }
                                onEvent(
                                    RecentConversationEvent.AddMessageToConversation(
                                        ConversationModel(
                                            message = userResponse.trimEnd(),
                                            conversationType = ConversationType.TEXT,
                                            messageOrigin = MessageOrigin.USER
                                        )
                                    )
                                )
                                userResponse = ""
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(50.dp)
                                .background(MaterialTheme.colorScheme.primary),
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.send),
                                contentDescription = "",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }

    if (onBackClick) {
        onEvent(RecentConversationEvent.SaveConversationToFirebase(sessionId = recentSessionId))
    }

    BackHandler {
        onEvent(RecentConversationEvent.SaveConversationToFirebase(sessionId = recentSessionId))
    }
    LaunchedEffect(key1 = recentConversationResponseUiState.queryResponse) {
        recentConversationResponseUiState.queryResponse.takeIf { it != null }?.let { queryResponse ->

            val response = queryResponse.trim()

            Timber.tag("RESPOSNE").d(response)
            val answerRegexPatterns = Regex(
                """Question \d+: (.+?)Answer:([\s\S]+?)(Code: \s*```([\s\S]+?)```)?Reference:([\s\S]+?)\s*(?:ReferenceLink: (.+))?""",
                setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
            )


            val questionRegex = Regex("""^\d+\..*?\?$""")

            val responseIsQuestion = questionRegex.containsMatchIn(response)
            val responseIsQuestionAndAnswer = answerRegexPatterns.containsMatchIn(response)

            if (responseIsQuestionAndAnswer) {
                if (responseIsQuestion) {
                    onEvent(
                        RecentConversationEvent.AddMessageToConversation(
                            ConversationModel(
                                message = response,
                                conversationType = ConversationType.TEXT,
                                messageType = MessageType.GENERATED,
                                messageOrigin = MessageOrigin.ASSISTANT
                            )
                        )
                    )
                } else {

                    val responseFormatter = ResponseFormatter()

                    responseFormatter
                        .setResponse(response)
                        .parseResponse { result ->
                            result.forEach {
                                onEvent(
                                    RecentConversationEvent.AddMessageToConversation(
                                        ConversationModel(
                                            message = it.question,
                                            conversationType = ConversationType.TEXT,
                                            messageType = MessageType.GENERATED,
                                            messageOrigin = MessageOrigin.ASSISTANT,
                                            responseType = ResponseType.DROPDOWN,
                                            dropDownItem = DropDownItem(
                                                it.question,
                                                it.answer,
                                                codeBlock = it.code,
                                                it.reference,
                                                it.referenceLink
                                            )
                                        )
                                    )
                                )
                            }
                        }
                }
            } else {
                onEvent(
                    RecentConversationEvent.AddMessageToConversation(
                        ConversationModel(
                            message = response,
                            conversationType = ConversationType.TEXT,
                            messageType = MessageType.GENERATED,
                            messageOrigin = MessageOrigin.ASSISTANT
                        )
                    )
                )
            }
        }

    }

    LaunchedEffect(key1 = recentConversationSavingUiState) {
        when {
            recentConversationSavingUiState.isSaving -> {
                savingDialog = true
            }

            recentConversationSavingUiState.saveCompleted -> {
                savingDialog = false
                navController.popBackStack()
            }
        }
    }
    Crossfade(
        targetState = recentConversationUiState,
        label = RECENT_CONVERSATION_CROSS_FADE
    ) { animatedRecentConversationUiState ->
        when {
            animatedRecentConversationUiState.isLoading -> {
                showLoadingDialog = true
            }

            animatedRecentConversationUiState.recentConversations != null -> {
                //Making the error dialog disappear
                showLoadingDialog = false
                //this will triggers the error dialog if conversation is empty
                showErrorDialog =
                    animatedRecentConversationUiState.recentConversations.takeIf { it.isNotEmpty() }
                        ?.let { _ -> false } ?: true
                onSuccess(
                    paddingValues
                )
            }

            animatedRecentConversationUiState.error.isNotEmpty() -> {
                showLoadingDialog = false
                showErrorDialog = true
            }
        }
    }

    AnimatedVisibility(visible = showLoadingDialog) {
        InstfyProgressDialog(
            title = "Remembering our old conversations ✨ ",
            description = "please wait! This may take while depends on your internet connection."
        )
    }

    AnimatedVisibility(visible = savingDialog) {
        InstfyProgressDialog(
            title = "Saving changes please wait.",
            description = "This may take while depends on your internet connection."
        )
    }

    AnimatedVisibility(visible = showErrorDialog) {

    }
}

@CompactThemedPreviewProvider
@Composable
fun RecentConversationScreenPreview() {
    InsightifyTheme {
        RecentConversationScreen(
            navController = rememberNavController(),
            title = "Java",
            recentSessionId = "",
            recentConversationUIState = RecentConversationUIState(
                isLoading = false,
                recentConversations = DEMO_CONVERSATION
            ),
            conversations = DEMO_CONVERSATION,
            recentConversationSavingUiState = RecentConversationSavingUiState(isSaving = false),
            recentConversationResponseUiState = RecentConversationResponseUiState(),
            onEvent = {}
        )
    }
}

internal const val RECENT_CONVERSATION_SCREEN = "Recent conversation"
internal const val BACK_ARROW = "Back Arrow"

//Animations
internal const val RECENT_CONVERSATION_CROSS_FADE = "Recent conversation"