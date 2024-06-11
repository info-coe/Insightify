package com.infomerica.insightify.ui.composables.chatbot.generic_assistant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AltRoute
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.SentimentVerySatisfied
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.infomerica.insightify.R
import com.infomerica.insightify.extension.makeToast
import com.infomerica.insightify.ui.components.dialog.InstfyProgressDialog
import com.infomerica.insightify.ui.components.placeholders.UnSupportedResolutionPlaceHolder
import com.infomerica.insightify.ui.composables.chatbot.generic_assistant.components.AssistantConversationMessage
import com.infomerica.insightify.ui.composables.chatbot.generic_assistant.components.AssistantConversationMessageError
import com.infomerica.insightify.ui.composables.chatbot.generic_assistant.components.AssistantConversationModel
import com.infomerica.insightify.ui.composables.chatbot.generic_assistant.menu.Item
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CalculateWindowSize
import com.infomerica.insightify.util.CompactThemedPreviewProvider
import com.infomerica.insightify.util.MediumThemedPreviewProvider
import kotlinx.coroutines.delay
import timber.log.Timber

@Composable
fun GenericAssistantScreen(
    navController: NavController,
    assistantResponseUiState: AssistantResponseUiState,
    previousConversationUiState: PreviousConversationUiState,
    assistantConversation: List<AssistantConversationModel>,
    windowWidthSizeClass: WindowWidthSizeClass,
    onAssistantEvent: (AssistantEvent) -> Unit,
) {

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
        windowWidthSizeClass,
        sessionTitle = "Assistant"
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
    windowWidthSizeClass: WindowWidthSizeClass,
    sessionTitle: String,
    content: @Composable (PaddingValues) -> Unit
) {

    var showMenu by remember {
        mutableStateOf(false)
    }
    InsightifyTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
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
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
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
                            IconButton(onClick = { showMenu = !showMenu }) {
                                Icon(
                                    imageVector = Icons.Rounded.Settings,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu != showMenu },
                                properties = PopupProperties(excludeFromSystemGesture = false)
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "About Assistant") },
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

@Composable
fun CompactGenericAssistantScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    assistantResponseUiState: AssistantResponseUiState,
    previousConversationUiState: PreviousConversationUiState,
    assistantConversation: List<AssistantConversationModel>,
    onAssistantEvent: (AssistantEvent) -> Unit,
) {
    var userResponse by rememberSaveable {
        mutableStateOf("")
    }

    var showLoadingDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        //Adding little bit of delay for Performance
        delay(500L)
        onAssistantEvent(AssistantEvent.GetPreviousConversationFromAssistant)
    }
    LaunchedEffect(key1 = assistantConversation) {
        Timber
            .tag(GENERIC_SCREEN)
            .i("Changed conversation - $assistantConversation")
    }


    DisposableEffect(key1 = Unit) {
        Timber
            .tag(GENERIC_SCREEN)
            .i("Generic screen created")
        onDispose {
            Timber
                .tag(GENERIC_SCREEN)
                .i("Generic screen Recomposed")
        }
    }


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.typing))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        reverseOnRepeat = true,
        speed = 1.6f
    )

    val selectedItems = remember {
        mutableStateListOf<Item>()
    }

    val emptyConversationContent: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp),
                    vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                )
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp
            )
            Text(
                text = "IndoChinese Restaurant",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp,
                modifier = Modifier.padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)),
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.SentimentVerySatisfied,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                        .weight(.2f),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Our IndoChinese Restaurant is one of the finest, In NC USA.",
                    modifier = Modifier
                        .weight(.8f)
                        .alpha(.8f),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                )
            }
            Row(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.AltRoute,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                        .weight(.2f),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "We deliver your food, Faster than your cat.",
                    modifier = Modifier
                        .weight(.8f)
                        .alpha(.8f),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.AttachMoney,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                        .weight(.2f),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "The cheapest and the best quality food, You ever seen. At your hands.",
                    modifier = Modifier
                        .weight(.8f)
                        .alpha(.8f),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                )
            }

            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hi_emoji),
                    contentDescription = "",
                    modifier = Modifier.size(
                        dimensionResource(id = com.intuit.sdp.R.dimen._60sdp)
                    )
                )
                Text(
                    text = buildAnnotatedString {
                        append("Start your conversation, By saying ")
                        withStyle(
                            SpanStyle(
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
                            append(
                                "Hi to our Assistant."
                            )
                        }
                    },
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        ),
                    textAlign = TextAlign.Center
                )
            }

        }
    }

    AnimatedVisibility(showLoadingDialog) {
        InstfyProgressDialog(
            title = "Remembering our old conversations ✨ ",
            description = "please wait! This may take while depends on your internet connection."
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        val (divider, messages, inputFiled) = createRefs()
        HorizontalDivider(
            modifier = Modifier
                .shadow(elevation = 3.dp)
                .constrainAs(divider) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                    bottom.linkTo(messages.top)
                },
            thickness = dimensionResource(id = com.intuit.sdp.R.dimen._1sdp),
            color = Color.DarkGray
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .constrainAs(messages) {
                    centerHorizontallyTo(parent)
                    top.linkTo(divider.bottom)
                    bottom.linkTo(inputFiled.top)
                    height = Dimension.fillToConstraints
                },
            reverseLayout = true
        ) {
            if (previousConversationUiState.previousConversation != null) {
                showLoadingDialog = false
            }
            itemsIndexed(assistantConversation.reversed()) { index: Int, item: AssistantConversationModel ->
                index.takeIf { it == 0 }?.let {
                    when {
                        assistantResponseUiState.isLoading -> {
                            Row(
                                modifier = Modifier
                                    .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                    .fillMaxWidth(.8f)
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.profile_place_holder),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .align(Alignment.Top)
                                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                                        .size(
                                            dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(bottom = 10.dp, start = 10.dp)
                                        .wrapContentHeight()
                                        .clip(MaterialTheme.shapes.large)
                                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
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
                            }
                        }


                        assistantResponseUiState.error != null -> {
                            AssistantConversationMessageError(
                                assistantConversationModel = AssistantConversationModel(
                                    message = assistantResponseUiState.error,
                                    isFromUser = false
                                )
                            )
                        }

                        else -> {

                        }
                    }
                }

                AssistantConversationMessage(
                    assistantConversationModel = item,
                    onItemClick = {
                        selectedItems.add(it!!)
                    },
                    onMenuTap = {
                        context.makeToast("Tap on the Button to confirm your order.")
                    },
                    onMenuOrderConfirmation = {
                        context.makeToast("Order confirmed. ")
                        onAssistantEvent(AssistantEvent.AddOrdersToFirebase(it))
                    }
                )
            }
            if (previousConversationUiState.emptyConversation == true) {
                showLoadingDialog = false
                item {
                    emptyConversationContent()
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp),
                    bottom = if (selectedItems.isEmpty()) dimensionResource(id = com.intuit.sdp.R.dimen._15sdp) else 0.dp
                )
                .fillMaxWidth()
                .wrapContentHeight()
                .background(if (selectedItems.isEmpty()) Color.Transparent else MaterialTheme.colorScheme.surfaceContainerHigh)
                .animateContentSize(tween(700))
                .constrainAs(inputFiled) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        ) {
            AnimatedVisibility(
                visible = selectedItems.isNotEmpty(),
                enter = fadeIn() + expandVertically(clip = false),
                exit = fadeOut() + shrinkVertically(clip = false),
            ) {
                //Shows the selected items.
                ConstraintLayout {
                    val (title, selectedItemsRow, totalAmount, remarkColumn, actionButton) = createRefs()
                    var showRemarkInputField by remember {
                        mutableStateOf(false)
                    }
                    var remarkText by remember {
                        mutableStateOf("")
                    }
                    Text(
                        text = "Your selections",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                        modifier = Modifier
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                            .constrainAs(title) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                    )

                    val totalBill = selectedItems.sumOf { it.amount ?: 0.0 }
                    Text(
                        text = "$ $totalBill",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                        modifier = Modifier
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                            .constrainAs(totalAmount) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                start.linkTo(title.end)
                            }
                    )

                    //Selected Items
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                            )
                            .constrainAs(selectedItemsRow) {
                                top.linkTo(title.bottom, margin = 15.dp)
                                start.linkTo(parent.start)
                            },
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)))
                        }
                        itemsIndexed(selectedItems) { index, item ->
                            ConstraintLayout(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .wrapContentSize()
                                    .padding(
                                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                                    )
                            ) {
                                val (itemNumber, itemName, itemAmount, cancelButton) = createRefs()
                                Text(
                                    text = item.itemNumber.toString().plus(" . "),
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                    modifier = Modifier.constrainAs(itemNumber) {
                                        start.linkTo(parent.start)
                                        top.linkTo(parent.top)
                                    }
                                )
                                Text(
                                    text = item.itemName ?: "",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                    modifier = Modifier
                                        .requiredWidthIn(max = dimensionResource(id = com.intuit.sdp.R.dimen._150sdp))
                                        .constrainAs(itemName) {
                                            start.linkTo(itemNumber.end)
                                            top.linkTo(parent.top)
                                        }
                                )
                                IconButton(
                                    onClick = {
                                        selectedItems.removeAt(index)
                                    },
                                    modifier = Modifier
                                        .constrainAs(cancelButton) {
                                            top.linkTo(parent.top)
                                            start.linkTo(itemName.end)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Cancel,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }

                                Text(
                                    text = " $${item.amount.toString()}",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                    modifier = Modifier.constrainAs(itemAmount) {
                                        start.linkTo(itemName.start)
                                        top.linkTo(itemName.bottom, margin = 5.dp)
                                    }
                                )
                            }
                        }
                    }


                    Column(
                        modifier = Modifier
                            .constrainAs(remarkColumn) {
                                top.linkTo(selectedItemsRow.bottom)
                                centerHorizontallyTo(parent)
                            }
                    ) {
                        AnimatedVisibility(visible = showRemarkInputField) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                                    .padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    .animateContentSize(
                                        animationSpec = tween(
                                            500
                                        )
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    10.dp,
                                    alignment = Alignment.End
                                )
                            ) {
                                AnimatedVisibility(
                                    visible = remarkText.isNotEmpty()
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.cancel),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .weight(.2f)
                                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._12sdp))
                                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                                            .clickable {
                                                remarkText = ""
                                            },
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                TextField(
                                    modifier = Modifier
                                        .weight(.6f)
                                        .padding(vertical = 5.dp)
                                        .wrapContentHeight()
                                        .verticalScroll(state = rememberScrollState()),
                                    value = remarkText,
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
                                        remarkText = it
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Type your customization.",
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
                        }
                        Button(
                            onClick = {
                                showRemarkInputField = !showRemarkInputField
                                if (!showRemarkInputField) {
                                    val itemNames =
                                        selectedItems.joinToString(",") { it.itemName ?: "" }
                                    val query =
                                        "i want to add $itemNames ${remarkText.ifEmpty { "" }}"
                                    //Execute Query
                                    onAssistantEvent(
                                        AssistantEvent.AddMessageToConversation(
                                            AssistantConversationModel(
                                                message = query,
                                                isFromUser = true
                                            )
                                        )
                                    )
                                    onAssistantEvent(
                                        AssistantEvent.GetResponseFromAssistant(
                                            query
                                        )
                                    )
                                    selectedItems.clear()
                                }
                            },
                            modifier = Modifier
                                .padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                .fillMaxWidth()
                                .height(dimensionResource(id = com.intuit.sdp.R.dimen._45sdp))
                                .padding(
                                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._40sdp),
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = "Add Customization",
                                fontFamily = poppinsFontFamily
                            )
                        }
                    }

                    if (remarkText.isEmpty()) {
                        Button(
                            onClick = {
                                val itemNames =
                                    selectedItems.joinToString(",") { it.itemName ?: "" }
                                val query = "i want to add $itemNames ${remarkText.ifEmpty { "" }}"
                                //Execute Query
                                onAssistantEvent(
                                    AssistantEvent.AddMessageToConversation(
                                        AssistantConversationModel(
                                            message = query,
                                            isFromUser = true
                                        )
                                    )
                                )
                                onAssistantEvent(
                                    AssistantEvent.GetResponseFromAssistant(
                                        query
                                    )
                                )
                                selectedItems.clear()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(id = com.intuit.sdp.R.dimen._45sdp))
                                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                                .constrainAs(actionButton) {
                                    top.linkTo(remarkColumn.bottom)
                                    bottom.linkTo(parent.bottom, margin = 15.dp)
                                }
                        ) {
                            Text(
                                text = "Add to order",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(visible = selectedItems.isEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp)
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .animateContentSize(
                            animationSpec = tween(
                                500
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.End)
                ) {
                    AnimatedVisibility(
                        visible = userResponse.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.cancel),
                            contentDescription = "",
                            modifier = Modifier
                                .weight(.2f)
                                .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._12sdp))
                                .size(dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                                .clickable {
                                    userResponse = ""
                                },
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    TextField(
                        modifier = Modifier
                            .weight(.6f)
                            .padding(vertical = 5.dp)
                            .wrapContentHeight()
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
                    IconButton(
                        onClick = {
                            if (userResponse.isNotEmpty()) {
                                //Execute Query
                                onAssistantEvent(
                                    AssistantEvent.AddMessageToConversation(
                                        AssistantConversationModel(
                                            message = userResponse,
                                            isFromUser = true
                                        )
                                    )
                                )
                                onAssistantEvent(
                                    AssistantEvent.GetResponseFromAssistant(
                                        userResponse
                                    )
                                )
                                userResponse = ""
                            }
                        },
                        modifier = Modifier
                            .weight(.2f)
                            .clip(CircleShape)
                            .size(
                                dimensionResource(id = com.intuit.sdp.R.dimen._40sdp)
                            ),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.send),
                            contentDescription = "",
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .size(
                                    dimensionResource(id = com.intuit.sdp.R.dimen._18sdp)
                                ),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = assistantResponseUiState.assistantResponse) {
        if (assistantResponseUiState.assistantResponse != null) {
            onAssistantEvent(
                AssistantEvent.AddMessageToConversation(
                    AssistantConversationModel(
                        assistantResponseUiState.assistantResponse,
                        isFromUser = false
                    )
                )
            )
            Timber
                .tag(GENERIC_SCREEN)
                .d("Message Added From Assistant : ${assistantResponseUiState.assistantResponse}")
        }
    }
    LaunchedEffect(key1 = previousConversationUiState) {
        if (previousConversationUiState.isLoading) {
            showLoadingDialog = true
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediumGenericAssistantScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    assistantResponseUiState: AssistantResponseUiState,
    previousConversationUiState: PreviousConversationUiState,
    assistantConversation: List<AssistantConversationModel>,
    onAssistantEvent: (AssistantEvent) -> Unit,
) {
    var userResponse by rememberSaveable {
        mutableStateOf("")
    }

    var showLoadingDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        //Adding little bit of delay for Performance
        delay(500L)
        onAssistantEvent(AssistantEvent.GetPreviousConversationFromAssistant)
    }
    LaunchedEffect(key1 = assistantConversation) {
        Timber
            .tag(GENERIC_SCREEN)
            .i("Changed conversation - $assistantConversation")
    }


    DisposableEffect(key1 = Unit) {
        Timber
            .tag(GENERIC_SCREEN)
            .i("Generic screen created")
        onDispose {
            Timber
                .tag(GENERIC_SCREEN)
                .i("Generic screen Recomposed")
        }
    }


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.typing))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        reverseOnRepeat = true,
        speed = 1.6f
    )

    val selectedItems = remember {
        mutableStateListOf<Item>()
    }

    val emptyConversationContent: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp),
                    vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                )
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
            )
            Text(
                text = "IndoChinese Restaurant",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                modifier = Modifier.padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)),
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.SentimentVerySatisfied,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                        .weight(.2f),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Our IndoChinese Restaurant is one of the finest, In NC USA.",
                    modifier = Modifier
                        .weight(.8f)
                        .alpha(.8f),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                )
            }
            Row(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.AltRoute,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                        .weight(.2f),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "We deliver your food, Faster than your cat.",
                    modifier = Modifier
                        .weight(.8f)
                        .alpha(.8f),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.AttachMoney,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                        .weight(.2f),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "The cheapest and the best quality food, You ever seen. At your hands.",
                    modifier = Modifier
                        .weight(.8f)
                        .alpha(.8f),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                )
            }

            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hi_emoji),
                    contentDescription = "",
                    modifier = Modifier.size(
                        dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)
                    )
                )
                Text(
                    text = buildAnnotatedString {
                        append("Start your conversation, By saying ")
                        withStyle(
                            SpanStyle(
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
                            append(
                                "Hi to our Assistant."
                            )
                        }
                    },
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        ),
                    textAlign = TextAlign.Center,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._8ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
                )
            }

        }
    }

    AnimatedVisibility(showLoadingDialog) {
        InstfyProgressDialog(
            title = "Remembering our old conversations ✨ ",
            description = "please wait! This may take while depends on your internet connection."
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        val (divider, messages, inputFiled) = createRefs()
        HorizontalDivider(
            modifier = Modifier
                .shadow(elevation = 3.dp)
                .constrainAs(divider) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                    bottom.linkTo(messages.top)
                },
            thickness = dimensionResource(id = com.intuit.sdp.R.dimen._1sdp),
            color = Color.DarkGray
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .constrainAs(messages) {
                    centerHorizontallyTo(parent)
                    top.linkTo(divider.bottom)
                    bottom.linkTo(inputFiled.top)
                    height = Dimension.fillToConstraints
                },
            reverseLayout = true
        ) {
            if (previousConversationUiState.previousConversation != null) {
                showLoadingDialog = false
            }
            itemsIndexed(assistantConversation.reversed()) { index: Int, item: AssistantConversationModel ->
                index.takeIf { it == 0 }?.let {
                    when {
                        assistantResponseUiState.isLoading -> {
                            Row(
                                modifier = Modifier
                                    .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                    .fillMaxWidth(.8f)
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.profile_place_holder),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .align(Alignment.Top)
                                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                                        .size(
                                            dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(bottom = 10.dp, start = 10.dp)
                                        .wrapContentHeight()
                                        .clip(MaterialTheme.shapes.large)
                                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
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
                            }
                        }


                        assistantResponseUiState.error != null -> {
                            AssistantConversationMessageError(
                                assistantConversationModel = AssistantConversationModel(
                                    message = assistantResponseUiState.error,
                                    isFromUser = false
                                )
                            )
                        }

                        else -> {

                        }
                    }
                }

                AssistantConversationMessage(
                    assistantConversationModel = item,
                    onItemClick = {
                        selectedItems.add(it!!)
                    },
                    onMenuTap = {
                        context.makeToast("Tap on the Button to confirm your order.")
                    },
                    onMenuOrderConfirmation = {
                        context.makeToast("Order confirmed. ")
                        onAssistantEvent(AssistantEvent.AddOrdersToFirebase(it))
                    }
                )
            }
            if (previousConversationUiState.emptyConversation == true) {
                showLoadingDialog = false
                stickyHeader {
                    emptyConversationContent()
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp),
                    bottom = if (selectedItems.isEmpty()) dimensionResource(id = com.intuit.sdp.R.dimen._15sdp) else 0.dp
                )
                .fillMaxWidth()
                .wrapContentHeight()
                .background(if (selectedItems.isEmpty()) Color.Transparent else MaterialTheme.colorScheme.surfaceContainerHigh)
                .animateContentSize(tween(700))
                .constrainAs(inputFiled) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = selectedItems.isNotEmpty(),
                enter = fadeIn() + expandVertically(clip = false),
                exit = fadeOut() + shrinkVertically(clip = false),
            ) {
                //Shows the selected items.
                ConstraintLayout {
                    val (title, selectedItemsRow, totalAmount, remarkColumn, actionButton) = createRefs()
                    var showCustomizationInputField by remember {
                        mutableStateOf(false)
                    }
                    var customizationText by remember {
                        mutableStateOf("")
                    }
                    Text(
                        text = "Your selections",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        modifier = Modifier
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                            .constrainAs(title) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                    )

                    val totalBill = selectedItems.sumOf { it.amount ?: 0.0 }
                    Text(
                        text = "$ $totalBill",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        modifier = Modifier
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                            .constrainAs(totalAmount) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                start.linkTo(title.end)
                            }
                    )

                    //Selected Items
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                            )
                            .constrainAs(selectedItemsRow) {
                                top.linkTo(title.bottom, margin = 15.dp)
                                start.linkTo(parent.start)
                            },
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)))
                        }
                        itemsIndexed(selectedItems) { index, item ->
                            ConstraintLayout(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .wrapContentSize()
                                    .padding(
                                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp),
                                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                                    )
                            ) {
                                val (itemNumber, itemName, itemAmount, cancelButton) = createRefs()
                                Text(
                                    text = item.itemNumber.toString().plus(" . "),
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                    modifier = Modifier.constrainAs(itemNumber) {
                                        start.linkTo(parent.start)
                                        top.linkTo(parent.top)
                                    }
                                )
                                Text(
                                    text = item.itemName ?: "",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                    modifier = Modifier
                                        .requiredWidthIn(max = dimensionResource(id = com.intuit.sdp.R.dimen._150sdp))
                                        .constrainAs(itemName) {
                                            start.linkTo(itemNumber.end)
                                            top.linkTo(parent.top)
                                        }
                                )
                                IconButton(
                                    onClick = {
                                        selectedItems.removeAt(index)
                                    },
                                    modifier = Modifier
                                        .constrainAs(cancelButton) {
                                            top.linkTo(parent.top)
                                            start.linkTo(itemName.end)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Cancel,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier
                                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._22sdp))
                                    )
                                }

                                Text(
                                    text = " $${item.amount.toString()}",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                    modifier = Modifier.constrainAs(itemAmount) {
                                        start.linkTo(itemName.start)
                                        end.linkTo(cancelButton.start)
                                        top.linkTo(itemName.bottom, margin = 5.dp)
                                    }
                                )
                            }
                        }
                    }


                    Column(
                        modifier = Modifier
                            .constrainAs(remarkColumn) {
                                top.linkTo(selectedItemsRow.bottom)
                                centerHorizontallyTo(parent)
                            }
                    ) {
                        AnimatedVisibility(visible = showCustomizationInputField) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._22sdp))
                                    .padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                    .fillMaxWidth()
                                    .heightIn(
                                        min = dimensionResource(id = com.intuit.sdp.R.dimen._40sdp),
                                        max = dimensionResource(id = com.intuit.sdp.R.dimen._260sdp)
                                    )
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    .animateContentSize(
                                        animationSpec = tween(
                                            500
                                        )
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    10.dp,
                                    alignment = Alignment.End
                                )
                            ) {
                                AnimatedVisibility(
                                    visible = customizationText.isNotEmpty()
                                ) {
                                    IconButton(
                                        onClick = { customizationText = "" },
                                        modifier = Modifier
                                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                        ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.cancel),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .weight(.2f)
                                                .size(dimensionResource(id = com.intuit.sdp.R.dimen._18sdp)),
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                                TextField(
                                    modifier = Modifier
                                        .weight(.6f)
                                        .padding(
                                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                                        )
                                        .wrapContentHeight()
                                        .verticalScroll(state = rememberScrollState()),
                                    value = customizationText,
                                    textStyle = TextStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                                        lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
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
                                        customizationText = it
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Type your customization.",
                                            fontFamily = poppinsFontFamily,
                                            fontWeight = FontWeight.Light,
                                            modifier = Modifier
                                                .alpha(.8f),
                                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
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
                        }
                        Button(
                            onClick = {
                                showCustomizationInputField = !showCustomizationInputField
                                if (!showCustomizationInputField) {
                                    val itemNames =
                                        selectedItems.joinToString(",") { it.itemName ?: "" }
                                    val query =
                                        "i want to add $itemNames ${customizationText.ifEmpty { "" }}"
                                    //Execute Query
                                    onAssistantEvent(
                                        AssistantEvent.AddMessageToConversation(
                                            AssistantConversationModel(
                                                message = query,
                                                isFromUser = true
                                            )
                                        )
                                    )
                                    onAssistantEvent(
                                        AssistantEvent.GetResponseFromAssistant(
                                            query
                                        )
                                    )
                                    selectedItems.clear()
                                }
                            },
                            modifier = Modifier
                                .padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                .fillMaxWidth()
                                .height(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
                                .padding(
                                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._40sdp),
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = "Add Customization",
                                fontFamily = poppinsFontFamily,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
                            )
                        }
                    }

                    if (customizationText.isEmpty()) {
                        Button(
                            onClick = {
                                val itemNames =
                                    selectedItems.joinToString(",") { it.itemName ?: "" }
                                val query =
                                    "i want to add $itemNames ${customizationText.ifEmpty { "" }}"
                                //Execute Query
                                onAssistantEvent(
                                    AssistantEvent.AddMessageToConversation(
                                        AssistantConversationModel(
                                            message = query,
                                            isFromUser = true
                                        )
                                    )
                                )
                                onAssistantEvent(
                                    AssistantEvent.GetResponseFromAssistant(
                                        query
                                    )
                                )
                                selectedItems.clear()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                .height(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
                                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                                .constrainAs(actionButton) {
                                    top.linkTo(remarkColumn.bottom)
                                    bottom.linkTo(parent.bottom, margin = 15.dp)
                                }
                        ) {
                            Text(
                                text = "Add to order",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(visible = selectedItems.isEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp)
                        )
                        .fillMaxWidth()
                        .heightIn(
                            min = dimensionResource(id = com.intuit.sdp.R.dimen._45sdp)
                        )
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        .animateContentSize(
                            animationSpec = tween(
                                500
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.End)
                ) {
                    AnimatedVisibility(
                        visible = userResponse.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.cancel),
                            contentDescription = "",
                            modifier = Modifier
                                .weight(.2f)
                                .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._12sdp))
                                .size(dimensionResource(id = com.intuit.sdp.R.dimen._14sdp))
                                .clickable {
                                    userResponse = ""
                                },
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    TextField(
                        modifier = Modifier
                            .weight(.6f)
                            .padding(vertical = 5.dp)
                            .verticalScroll(state = rememberScrollState()),
                        value = userResponse,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
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
                                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
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
                    IconButton(
                        onClick = {
                            if (userResponse.isNotEmpty()) {
                                //Execute Query
                                onAssistantEvent(
                                    AssistantEvent.AddMessageToConversation(
                                        AssistantConversationModel(
                                            message = userResponse,
                                            isFromUser = true
                                        )
                                    )
                                )
                                onAssistantEvent(
                                    AssistantEvent.GetResponseFromAssistant(
                                        userResponse
                                    )
                                )
                                userResponse = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(.2f)
                            .clip(CircleShape)
                            .size(
                                dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)
                            ),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.send),
                            contentDescription = "",
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .size(
                                    dimensionResource(id = com.intuit.sdp.R.dimen._18sdp)
                                ),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = assistantResponseUiState.assistantResponse) {
        if (assistantResponseUiState.assistantResponse != null) {
            onAssistantEvent(
                AssistantEvent.AddMessageToConversation(
                    AssistantConversationModel(
                        assistantResponseUiState.assistantResponse,
                        isFromUser = false
                    )
                )
            )
            Timber
                .tag(GENERIC_SCREEN)
                .d("Message Added From Assistant : ${assistantResponseUiState.assistantResponse}")
        }
    }
    LaunchedEffect(key1 = previousConversationUiState) {
        if (previousConversationUiState.isLoading) {
            showLoadingDialog = true
        }
    }
}


@CompactThemedPreviewProvider
@Composable
fun CompactRecentConversationScreenPreview() {
    InsightifyTheme {
        GenericAssistantScreen(
            navController = rememberNavController(),
            assistantResponseUiState = AssistantResponseUiState(
                isLoading = false,
                assistantResponse = "INFO9900"
            ),
            assistantConversation = listOf(),
            windowWidthSizeClass = WindowWidthSizeClass.Compact,
            previousConversationUiState = PreviousConversationUiState(
                isLoading = false,
                previousConversation = null,
                emptyConversation = true
            )
        ) {

        }
    }
}

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

private const val GENERIC_SCREEN = "Generic Screen"
private const val GENERIC_SCREEN_CROSS_FADE = "Generic Screen CrossFade"
private const val BACK_ARROW = "Back Arrow"

