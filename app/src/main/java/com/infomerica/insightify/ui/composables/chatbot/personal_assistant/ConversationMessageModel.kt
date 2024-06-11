package com.infomerica.insightify.ui.composables.chatbot.personal_assistant

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily

/**
 * This is the message Model.
 * This composable is responsible for displaying messages on the
 * conversation screen.
 */
@Composable
fun ConversationMessageModel(
    conversationModel: ConversationModel,
    onCopyText: (String) -> Unit = {},
    onPdfDownload: () -> Unit = {},
    onAnswersGenerate: () -> Unit = {}
) {

    //This condition triggers when the text message is form User side.
    conversationModel.messageOrigin.takeIf { it == MessageOrigin.USER }?.let {
        Row(
            modifier = Modifier
                .padding(top = 15.dp)
                .padding(start = 60.dp, end = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = conversationModel.message,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(
                        end = 15.dp,
                        bottom = 15.dp,
                        top = 15.dp,
                        start = 15.dp
                    ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    } ?: run {
        //This condition triggers when the text message is form Chat bot side.
        //Generated answer is of 2 types
        // - Default - Text type for questions or messages.
        // - DropDown - DropDown type used for answers.
        conversationModel.messageType.takeIf { it == MessageType.GENERATED }?.let {
            val formattedMessage: String = if (
                conversationModel.message.contains(regex = Regex("\\?"))
            ) {
                conversationModel.message.replace("?", "? \n")
            } else {
                conversationModel.message
            }

            when (conversationModel.responseType) {
                ResponseType.DEFAULT -> {
                    var isLongPressEnabled by remember {
                        mutableStateOf(false)
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 60.dp, bottom = 0.dp, top = 15.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Column {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        ParagraphStyle(
                                            textMotion = TextMotion.Animated
                                        )
                                    ) {
                                        append(formattedMessage)
                                    }
                                },
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                modifier = Modifier
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                isLongPressEnabled = !isLongPressEnabled
                                            }
                                        )
                                    }
                                    .wrapContentSize()
                                    .clip(MaterialTheme.shapes.large)
                                    .background(
                                        animateColorAsState(
                                            targetValue = if (isLongPressEnabled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryContainer,
                                            animationSpec = tween(500),
                                            label = ""
                                        ).value
                                    )
                                    .padding(15.dp),
                                color = if (isLongPressEnabled) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            AnimatedVisibility(visible = isLongPressEnabled) {
                                Row(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(top = 10.dp, bottom = 10.dp)
                                        .wrapContentHeight()
                                        .clip(MaterialTheme.shapes.large)
                                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.CopyAll,
                                        contentDescription = "copy",
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier
                                            .padding(start = 15.dp, end = 15.dp)
                                            .padding(vertical = 10.dp)
                                            .clip(CircleShape)
                                            .size(25.dp)
                                            .clickable {
                                                isLongPressEnabled = false
                                                onCopyText(conversationModel.message)
                                            }
                                    )
                                    val pattern = Regex("([5-9]|1[0-9]|20)")
                                    if (pattern.containsMatchIn(conversationModel.message)) {
                                        Icon(
                                            imageVector = Icons.Rounded.PictureAsPdf,
                                            contentDescription = "pdf",
                                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                            modifier = Modifier
                                                .padding(end = 15.dp)
                                                .padding(vertical = 10.dp)
                                                .clip(CircleShape)
                                                .size(25.dp)
                                                .clickable {
                                                    isLongPressEnabled = false
                                                    onPdfDownload()
                                                }
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Rounded.QuestionAnswer,
                                        contentDescription = "generate answers",
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier
                                            .padding(end = 15.dp)
                                            .padding(vertical = 10.dp)
                                            .clip(CircleShape)
                                            .size(25.dp)
                                            .clickable {
                                                isLongPressEnabled = false
                                                onAnswersGenerate()
                                            }
                                    )
                                }
                            }
                        }

                    }
                }

                ResponseType.DROPDOWN -> {
                    var isDropDownEnabled by remember {
                        mutableStateOf(false)
                    }

                    Column(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .padding(start = 10.dp, end = 60.dp)
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .animateContentSize(
                                spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                            .clickable {
                                isDropDownEnabled = !isDropDownEnabled
                            },
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(15.dp)
                        ) {
                            Text(
                                text = conversationModel.dropDownItem.question,
                                modifier = Modifier
                                    .fillMaxWidth(.9f)
                                    .padding(end = 5.dp),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Icon(
                                imageVector = if (isDropDownEnabled) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                                contentDescription = "",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        AnimatedVisibility(visible = isDropDownEnabled, label = "DropDown") {
                            Column {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier
                                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                                )
                                Text(
                                    text = conversationModel.dropDownItem.answer,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp)
                                        .padding(top = 10.dp),
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                if(conversationModel.dropDownItem.codeBlock.isNotEmpty()) {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .heightIn(max = 200.dp)
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                        .clip(MaterialTheme.shapes.extraLarge)
                                        .background(MaterialTheme.colorScheme.secondary)
                                        .verticalScroll(rememberScrollState())
                                        .horizontalScroll(rememberScrollState()),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = conversationModel.dropDownItem.codeBlock.ifEmpty { "sample" },
                                            fontFamily = poppinsFontFamily,
                                            modifier = Modifier.padding(15.dp),
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                }
                                conversationModel.dropDownItem.referenceUrl.takeIf { it.isNotEmpty() }?.let {
                                    Text(
                                        text = buildAnnotatedString {
                                            append("To know more about ")
                                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                                append(text = conversationModel.dropDownItem.referenceText)
                                            }
                                            append(" refer to this link.")
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp)
                                            .padding(top = 10.dp),
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                ClickableText(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            SpanStyle(
                                                fontFamily = poppinsFontFamily,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            append(
                                                conversationModel.dropDownItem.referenceUrl
                                            )
                                        }
                                    },
                                    onClick = {
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp)
                                        .padding(bottom = 15.dp, top = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        } ?: run {
            //This condition triggers when the Generated message is form Chat Bot.
            Row(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .padding(start = 10.dp, end = 60.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val formattedMessage: String = if (
                    conversationModel.message.contains(regex = Regex("\\?"))
                ) {
                    conversationModel.message.replace("?", "? \n")
                } else {
                    conversationModel.message
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            ParagraphStyle(
                                textMotion = TextMotion.Animated
                            )
                        ) {
                            append(formattedMessage)
                        }
                    },
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(end = 15.dp, bottom = 15.dp, start = 15.dp, top = 15.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * Composable for displaying difficulty options in text.
 */
@Composable
fun ConversationOptionModel(
    conversationModel: ConversationModel,
    onEasy: () -> Unit,
    onMedium: () -> Unit,
    onHard: () -> Unit,
    enable: Boolean
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 60.dp, bottom = 0.dp, top = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column {
            Text(
                text = conversationModel.message,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(end = 15.dp, bottom = 15.dp, start = 15.dp, top = 15.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            AnimatedVisibility(visible = enable) {
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable(enabled = enable, role = Role.Button) {

                        },
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable {
                                onEasy()
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)

                    ) {
                        Text(
                            text = "Easy",
                            fontFamily = poppinsFontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .wrapContentSize()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .clickable {
                                onMedium()
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Medium",
                            fontFamily = poppinsFontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.error)
                            .clickable {
                                onHard()
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Hard",
                            fontFamily = poppinsFontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable for displaying questions slider in text.
 */
@Composable
fun ConversationNumberModel(
    chatModel: ConversationModel,
    onGenerate: (Int) -> Unit,
    enable: Boolean
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 60.dp, bottom = 0.dp, top = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.End
    ) {
        var sliderPosition by rememberSaveable { mutableFloatStateOf(0f) }
        Column {
            Text(
                text = chatModel.message,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(end = 15.dp, bottom = 15.dp, start = 15.dp, top = 15.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = buildAnnotatedString {
                    append("I want to generate ")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(sliderPosition.toInt().toString())
                    }
                    append(" questions.")
                },
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .wrapContentSize()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(end = 15.dp, bottom = 15.dp, start = 15.dp, top = 15.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            AnimatedVisibility(visible = enable) {
                Column {
                    Slider(
                        value = sliderPosition,
                        onValueChange = { updateValue ->
                            sliderPosition = updateValue
                        },
                        valueRange = 0f..8f,
                        steps = 8,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(top = 10.dp)
                    )
                    AnimatedVisibility(visible = sliderPosition.toInt() != 0) {
                        Button(
                            onClick = {
                                onGenerate(sliderPosition.toInt())
                            },
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .height(55.dp)
                        ) {
                            Text(
                                text = "Generate",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
@Preview(
    uiMode = UI_MODE_NIGHT_YES,
    device = PIXEL_7
)
private fun ConversationMessageModelPreview() {
    InsightifyTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            ConversationMessageModel(
                conversationModel = ConversationModel(
                    "Hey there! What would like to search today!",
                    conversationType = ConversationType.TEXT,
                    messageOrigin = MessageOrigin.ASSISTANT,
                    responseType = ResponseType.DROPDOWN
                )
            )
            ConversationMessageModel(
                conversationModel = ConversationModel(
                    "Hey there! What would like to search today!",
                    conversationType = ConversationType.TEXT,
                    messageOrigin = MessageOrigin.ASSISTANT,
                    responseType = ResponseType.DROPDOWN,
                    messageType = MessageType.GENERATED,
                    dropDownItem = DropDownItem(
                        question = "What is kotlin",
                        answer = "Some Answer about kotlin and so on That basically refers to idk and something.",
                        referenceText = "Java static keyword",
                        referenceUrl = "https://www.javatpoint.com/static-keyword-in-java"
                    )
                )
            )
            ConversationMessageModel(
                conversationModel = ConversationModel(
                    "Kotlin",
                    conversationType = ConversationType.TEXT,
                    messageOrigin = MessageOrigin.USER,
                    responseType = ResponseType.DROPDOWN
                )
            )

            ConversationOptionModel(
                conversationModel = ConversationModel(
                    "Sure! select your difficulty level - Easy, Medium, Hard. ",
                    conversationType = ConversationType.TEXT,
                    messageOrigin = MessageOrigin.ASSISTANT
                ),
                onHard = {},
                onMedium = {},
                onEasy = {},
                enable = true
            )

            ConversationMessageModel(
                conversationModel = ConversationModel(
                    "Difficulty level set to Medium",
                    conversationType = ConversationType.TEXT,
                    messageOrigin = MessageOrigin.USER,
                    responseType = ResponseType.DROPDOWN
                )
            )
            ConversationNumberModel(
                chatModel = ConversationModel(
                    "How many number of question you would like to generate.",
                    conversationType = ConversationType.TEXT,
                    messageOrigin = MessageOrigin.ASSISTANT,
                    messageType = MessageType.GENERATED,
                    responseType = ResponseType.DROPDOWN
                ),
                enable = true,
                onGenerate = {

                }
            )
            ConversationMessageModel(
                conversationModel = ConversationModel(
                    "How many number of question you would like to generate.",
                    conversationType = ConversationType.TEXT,
                    messageOrigin = MessageOrigin.ASSISTANT,
                    messageType = MessageType.GENERATED,
                    responseType = ResponseType.DEFAULT
                )
            )
        }
    }
}