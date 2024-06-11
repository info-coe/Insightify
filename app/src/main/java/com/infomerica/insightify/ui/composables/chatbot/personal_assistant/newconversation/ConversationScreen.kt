package com.infomerica.insightify.ui.composables.chatbot.personal_assistant.newconversation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.infomerica.insightify.ui.components.dialog.InstfyAlertDialog
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationMessageModel
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationModel
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationNumberModel
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationOptionModel
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationType
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.DEMO_CONVERSATION
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.DropDownItem
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.MessageOrigin
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.MessageType
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ResponseType
import com.infomerica.insightify.ui.composables.home.recentsessions.RecentSessionModel
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * This composable used for displaying New conversation
 */
@Composable
fun ConversationScreen(
    navController: NavController,
    chatUiState: ConversationResponseUiState,
    conversationSavingUiState: ConversationSavingUiState,
    conversations: List<ConversationModel>,
    onConversationEvent: (ConversationEvent) -> Unit
) {

    var sessionTitle by remember {
        mutableStateOf("")
    }
    var onBackClick by remember {
        mutableStateOf(false)
    }

    ConversationBody(
        navController,
        onBackClick = {
            onBackClick = !onBackClick
        },
        sessionTitle = sessionTitle.ifEmpty { "Untitled" }
    ) {
        ConversationContent(
            paddingValues = it,
            conversationResponseUiState = chatUiState,
            conversationSavingUiState = conversationSavingUiState,
            navController = navController,
            sessionTitle = { titleFromContent ->
                sessionTitle = titleFromContent
            },
            onBackClick = onBackClick,
            conversations = conversations,
            onConversationEvent = onConversationEvent
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationBody(
    navController: NavController,
    onBackClick: () -> Unit,
    sessionTitle: String,
    content: @Composable (PaddingValues) -> Unit
) {
    InsightifyTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.dark_bg_1 else R.drawable.bright_bg_1),
                contentDescription = "conversation_background",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.5f),
                contentScale = ContentScale.Crop
            )
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = sessionTitle,
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
                        },
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.surface
                        ),
                        actions = {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = "",
                                modifier = Modifier.padding(horizontal = 15.dp)
                            )
                        }
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

@Composable
fun ConversationContent(
    paddingValues: PaddingValues,
    conversationResponseUiState: ConversationResponseUiState,
    conversationSavingUiState: ConversationSavingUiState,
    conversations: List<ConversationModel>,
    navController: NavController,
    sessionTitle: (String) -> Unit,
    onBackClick: Boolean,
    onConversationEvent: (ConversationEvent) -> Unit
) {

    var userResponse by rememberSaveable {
        mutableStateOf("")
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.typing))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        reverseOnRepeat = true,
        speed = 1.6f
    )

    var topic by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = conversations) {
        conversations.takeIf { it.size == 2 }?.let { conversations ->
            topic = conversations[1].message
        }
    }

    LaunchedEffect(key1 = topic) {
        Timber.tag("HOME_UI").d("topic called $topic")
        //Adding a little bit of delay to simulate the user experience
        delay(500)
        topic.takeIf { it.isNotEmpty() }?.let { topic ->
            sessionTitle(topic)
            onConversationEvent(
                ConversationEvent.AddMessageToConversation(
                    ConversationModel(
                        "Select your difficulty level for generating questions in $topic.",
                        ConversationType.OPTION,
                        messageOrigin = MessageOrigin.ASSISTANT
                    )
                )
            )
        }
    }

    var difficulty by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = difficulty) {
        delay(500)
        difficulty.takeIf { it.isNotEmpty() }?.let {
            onConversationEvent(
                ConversationEvent.AddMessageToConversation(
                    ConversationModel(
                        "How many question you would like to generate.",
                        ConversationType.NUMBER,
                        messageOrigin = MessageOrigin.ASSISTANT
                    )
                )
            )
        }
    }

    var finalQuery by rememberSaveable {
        mutableStateOf("")
    }
    var finalQuestionCount by rememberSaveable {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = finalQuestionCount) {
        delay(500)
        finalQuestionCount.takeIf { it != 0 }?.let { questionCount ->
            delay(500)
            onConversationEvent(
                ConversationEvent.AddMessageToConversation(
                    ConversationModel(
                        "Hang on! generating questions for you ✨.",
                        ConversationType.TEXT,
                        messageOrigin = MessageOrigin.ASSISTANT
                    )
                )
            )
            finalQuery =
                "Hey chatGpt generate $questionCount questions on $topic with difficulty $difficulty"

            onConversationEvent(
                ConversationEvent.ExecuteQuery(finalQuery)
            )
        }
    }

    //TODO : FIX It codeBlock not displaying
    LaunchedEffect(key1 = conversationResponseUiState.queryResponse) {
        Timber.tag("Conversation")
            .d(conversationResponseUiState.toString())
        when {
            conversationResponseUiState.queryResponse != null -> {

                val trimmedResponse = conversationResponseUiState.queryResponse.trim()

                Timber.tag("TRIMMED_RESPONSE").d(trimmedResponse)
                val answerRegexPatterns = Regex(
                    """Question \d+: (.+?)Answer:([\s\S]+?)(Code: \s*```([\s\S]+?)```)?Reference:([\s\S]+?)\s*(?:ReferenceLink: (.+))?""",
                    setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
                )


                val questionRegex = Regex("""^\d+\..*?\?$""")

                val responseIsQuestion = questionRegex.containsMatchIn(trimmedResponse)
                val responseIsQuestionAndAnswer =
                    answerRegexPatterns.containsMatchIn(trimmedResponse)

                if (responseIsQuestionAndAnswer) {
                    if (responseIsQuestion) {
                        onConversationEvent(
                            ConversationEvent.AddMessageToConversation(
                                ConversationModel(
                                    message = trimmedResponse,
                                    conversationType = ConversationType.TEXT,
                                    messageType = MessageType.GENERATED,
                                    messageOrigin = MessageOrigin.ASSISTANT
                                )
                            )
                        )
                    } else {

                        val responseFormatter = ResponseFormatter()

                        responseFormatter
                            .setResponse(trimmedResponse)
                            .parseResponse { result ->
                                result.forEach {
                                    onConversationEvent(
                                        ConversationEvent.AddMessageToConversation(
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
                    onConversationEvent(
                        ConversationEvent.AddMessageToConversation(
                            ConversationModel(
                                message = trimmedResponse,
                                conversationType = ConversationType.TEXT,
                                messageType = MessageType.GENERATED,
                                messageOrigin = MessageOrigin.ASSISTANT
                            )
                        )
                    )
                }
            }

            conversationResponseUiState.error.isNotEmpty() -> {
                onConversationEvent(
                    ConversationEvent.AddMessageToConversation(
                        ConversationModel(
                            message = "Unable to get your data",
                            conversationType = ConversationType.TEXT,
                            messageType = MessageType.GENERATED,
                            messageOrigin = MessageOrigin.ASSISTANT
                        )
                    )
                )
            }
        }
    }

    val context = LocalContext.current

    val pdfManager by remember {
        mutableStateOf(PDFManager(context))
    }

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .padding(paddingValues)
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
                    conversationResponseUiState.takeIf { it.isLoading }?.let {
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
                                    pdfManager.savePdf(
                                        topic,
                                        "$topic question with $difficulty difficulty",
                                        conversationModel.message
                                    ) { uri ->

                                    }
                                }
                            },
                            onAnswersGenerate = {
                                onConversationEvent(
                                    ConversationEvent.ExecuteQuery("generate answers for previously generated questions with code and reference links for each answer")
                                )
                            }
                        )
                    }

                    ConversationType.OPTION -> {
                        ConversationOptionModel(
                            conversationModel = conversationModel,
                            onEasy = { difficulty = "Easy" },
                            onMedium = { difficulty = "Medium" },
                            onHard = { difficulty = "Hard" },
                            enable = difficulty.isEmpty()
                        )
                    }

                    ConversationType.NUMBER -> {
                        ConversationNumberModel(
                            chatModel = conversationModel,
                            onGenerate = { questionToBeGenerated ->
                                finalQuestionCount = questionToBeGenerated
                            },
                            enable = finalQuestionCount == 0
                        )
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
                            },
                        tint = MaterialTheme.colorScheme.onSurface
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
                            if (finalQuestionCount != 0) {
                                onConversationEvent(
                                    ConversationEvent.ExecuteQuery(userResponse)
                                )
                                Timber.tag("userResponse").e(userResponse)
                            }
                            onConversationEvent(
                                ConversationEvent.AddMessageToConversation(
                                    ConversationModel(
                                        message = userResponse,
                                        conversationType = ConversationType.TEXT,
                                        messageOrigin = MessageOrigin.USER
                                    )
                                )
                            )
                            userResponse = ""
                        }

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

    LaunchedEffect(key1 = onBackClick) {
        showDialog = onBackClick
    }
    BackHandler {
        showDialog = !showDialog
    }
    AnimatedVisibility(visible = showDialog) {
        InstfyAlertDialog(
            title = "Would you like to save this session?",
            description = "By saving the session you can save this conversation.",
            positiveText = "Yes, save",
            negativeText = "No, Don't save",
            onPositiveFeedBack = {
                onConversationEvent(
                    ConversationEvent.SaveConversationToFirebase(
                        RecentSessionModel(
                            title = topic,
                            conversation = conversations
                        )
                    )
                )
            },
            onNegativeFeedBack = {
                showDialog = false
                coroutineScope.launch {
                    delay(500)
                    navController.popBackStack()
                }
            }
        )
    }

    when {
        conversationSavingUiState.isSaving -> {

        }

        conversationSavingUiState.saveCompleted -> {
            LaunchedEffect(key1 = Unit) {
                showDialog = false
                delay(500L)
                navController.popBackStack()
            }
        }
    }
}

@CompactThemedPreviewProvider
@Composable
fun ChatTestPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ConversationScreen(
                navController = rememberNavController(),
                ConversationResponseUiState(),
                ConversationSavingUiState(),
                DEMO_CONVERSATION
            ) {

            }
        }
    }
}