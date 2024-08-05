package com.infomericainc.insightify.ui.composables.genericassistant.variants

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.infomericainc.insightify.R
import com.infomericainc.insightify.extension.makeToast
import com.infomericainc.insightify.manager.PreferencesManager
import com.infomericainc.insightify.ui.components.dialog.InstfyProgressDialog
import com.infomericainc.insightify.ui.composables.genericassistant.AssistantConversationModel
import com.infomericainc.insightify.ui.composables.genericassistant.AssistantEvent
import com.infomericainc.insightify.ui.composables.genericassistant.AssistantResponseUiState
import com.infomericainc.insightify.ui.composables.genericassistant.ConversationRestrictionUIState
import com.infomericainc.insightify.ui.composables.genericassistant.PreviousConversationUiState
import com.infomericainc.insightify.ui.composables.genericassistant.components.compact.AssistantConversationMessageError
import com.infomericainc.insightify.ui.composables.genericassistant.components.compact.CompactAssistantConversationMessage
import com.infomericainc.insightify.ui.composables.genericassistant.components.compact.CompactEmptyConversationPlaceHolder
import com.infomericainc.insightify.ui.composables.genericassistant.components.compact.CompactRestrictionBottomSheetContent
import com.infomericainc.insightify.ui.composables.genericassistant.components.compact.CompactTTSBottomSheetContent
import com.infomericainc.insightify.ui.composables.genericassistant.components.compact.CompactTypingProgressPlaceHolder
import com.infomericainc.insightify.ui.composables.genericassistant.menu.Item
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CompactGenericAssistantScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    assistantResponseUiState: AssistantResponseUiState,
    previousConversationUiState: PreviousConversationUiState,
    assistantConversation: List<AssistantConversationModel>,
    conversationRestrictionUIState: ConversationRestrictionUIState,
    onAssistantEvent: (AssistantEvent) -> Unit,
) {
    var userResponse by rememberSaveable {
        mutableStateOf("")
    }

    var showLoadingDialog by remember {
        mutableStateOf(false)
    }

    var showTTSBottomSheet by remember {
        mutableStateOf(false)
    }


    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val restrictionBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    var isConversationLimitReached by remember {
        mutableStateOf(false)
    }

    val permissionState =
        rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val preferencesManager by remember {
        mutableStateOf(PreferencesManager(context))
    }

    LaunchedEffect(key1 = Unit) {
        //Adding little bit of delay for Performance
        delay(500L)
        onAssistantEvent(AssistantEvent.GetPreviousConversationFromAssistant)
    }
    LaunchedEffect(key1 = assistantConversation) {
        Timber
            .tag(COMPACT_GENERIC_SCREEN)
            .i("Changed conversation - $assistantConversation")
    }

    LaunchedEffect(conversationRestrictionUIState) {
        isConversationLimitReached = conversationRestrictionUIState.isLimitReached
    }

    LaunchedEffect(Unit) {
        if (preferencesManager.getBoolean(
                Constants.IS_CONVERSATION_LIMIT_REACHED,
                defaultValue = false
            )
        ) {
            isConversationLimitReached = true
        }
    }

    DisposableEffect(key1 = Unit) {
        Timber
            .tag(COMPACT_GENERIC_SCREEN)
            .i("Generic screen created")
        onDispose {
            Timber
                .tag(COMPACT_GENERIC_SCREEN)
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


    AnimatedVisibility(showLoadingDialog) {
        InstfyProgressDialog(
            title = "Remembering our old conversations ✨ ",
            description = "please wait! This may take while depends on your internet connection."
        )
    }

    AnimatedVisibility(isConversationLimitReached) {
        ModalBottomSheet(
            onDismissRequest = { /*TODO*/ },
            sheetState = restrictionBottomSheetState,
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            dragHandle = {},
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false,
                isFocusable = true,
                securePolicy = SecureFlagPolicy.SecureOff
            ),
            modifier = Modifier
                .padding(paddingValues)
                .padding(
                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                )
                .fillMaxHeight(.9f),
        ) {
            CompactRestrictionBottomSheetContent(
                navController = navController
            )
        }
    }

    if (showTTSBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTTSBottomSheet = false },
            sheetState = bottomSheetState,
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight(.9f),
        ) {
            CompactTTSBottomSheetContent(
                onSpeechCompleted = { TTSResult ->
                    if (TTSResult?.isNotEmpty() == true) {
                        coroutineScope.launch {
                            userResponse = TTSResult
                            delay(3000L)
                            showTTSBottomSheet = false
                            bottomSheetState.hide()
                        }
                    }
                },
                onClose = {
                    coroutineScope.launch {
                        showTTSBottomSheet = false
                    }
                }
            )
        }
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
                .blur(if (isConversationLimitReached) 10.dp else 0.dp)
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
                            CompactTypingProgressPlaceHolder(
                                modifier = Modifier
                                    .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                                    .fillMaxWidth(.8f)
                                    .wrapContentHeight()
                                    .animateItem(),
                                lottieComposition = composition,
                                progress = progress
                            )
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

                CompactAssistantConversationMessage(
                    modifier = Modifier
                        .animateItem(
                            placementSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow,
                            ),
                            fadeOutSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow,
                            )
                        ),
                    assistantConversationModel = item,
                    onItemClick = {
                        selectedItems.add(it!!)
                    },
                    onItemLongClick = {

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
                    CompactEmptyConversationPlaceHolder()
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp),
                    bottom = if (selectedItems.isEmpty()) dimensionResource(id = com.intuit.sdp.R.dimen._10sdp) else 0.dp
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
                                        imageVector = Icons.Rounded.Close,
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(
                                    start = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp)
                                )
                                .fillMaxWidth(.8f)
                                .wrapContentHeight()
                                .clip(MaterialTheme.shapes.extraLarge)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
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
                                visible = userResponse.isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .weight(.2f)
                                        .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
                                        .clickable {
                                            userResponse = ""
                                        },
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            TextField(
                                modifier = Modifier
                                    .weight(.8f)
                                    .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                                    .wrapContentHeight()
                                    .verticalScroll(state = rememberScrollState()),
                                value = userResponse,
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._13ssp).value.sp,
                                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp,
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
                                        text = "Message",
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
                                    .weight(.2f)
                                    .clip(CircleShape)
                                    .size(
                                        dimensionResource(id = com.intuit.sdp.R.dimen._40sdp)
                                    ),
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Send,
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    if (permissionState.hasPermission) {
                                        showTTSBottomSheet = !showTTSBottomSheet
                                    } else {
                                        permissionState.launchPermissionRequest()
                                        when {
                                            permissionState.hasPermission -> {
                                                showTTSBottomSheet = !showTTSBottomSheet
                                            }

                                            permissionState.shouldShowRationale -> {}
                                            !permissionState.hasPermission && !permissionState.shouldShowRationale -> {
                                                context.makeToast("Accept permission to use this feature.")
                                            }
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Mic,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                                        .graphicsLayer(alpha = 0.99f),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    Text(
                        text = "This is the Alpha build of our assistant. Contact us to access the full version.",
                        modifier = Modifier
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._40sdp)
                            )
                            .alpha(.6f),
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._9ssp).value.sp,
                        lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        textAlign = TextAlign.Center
                    )
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
                .tag(COMPACT_GENERIC_SCREEN)
                .d("Message Added From Assistant : ${assistantResponseUiState.assistantResponse}")
        }
    }
    LaunchedEffect(key1 = previousConversationUiState) {
        if (previousConversationUiState.isLoading) {
            showLoadingDialog = true
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
@CompactThemedPreviewProvider
fun CompactGenericScreenContentPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            CompactGenericAssistantScreenContent(
                paddingValues = PaddingValues(),
                navController = rememberNavController(),
                assistantResponseUiState = AssistantResponseUiState(
                    isLoading = true
                ),
                previousConversationUiState = PreviousConversationUiState(
                    isLoading = false,
                    emptyConversation = true
                ),
                conversationRestrictionUIState = ConversationRestrictionUIState(
                    isLimitReached = false
                ),
                assistantConversation = listOf()
            ) {

            }
        }

    }
}


private const val COMPACT_GENERIC_SCREEN = "CompactGenericScreen"