package com.infomericainc.insightify.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun InstfyWarningDialog(
    title: AnnotatedString = buildAnnotatedString { },
    description: AnnotatedString = buildAnnotatedString { },
    positiveText: String = "",
    negativeText: String = "",
    onPositiveFeedBack: () -> Unit,
    onNegativeFeedBack: () -> Unit,
    icon: ImageVector = Icons.Rounded.ArrowDropUp,
    iconProperties: Pair<Color, Color> = Pair(Color.Transparent, Color.Transparent)
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .wrapContentHeight()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "WarningDialogIcon",
                tint = iconProperties.first,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(CircleShape)
                    .background(iconProperties.second)
                    .padding(10.dp)
                    .size(25.dp)
            )
            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(.9f),
                fontSize = 22.sp,
                lineHeight = 30.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Text(
                text = description,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(.9f)
                    .alpha(.7f),
                fontSize = 16.sp,
                lineHeight = 25.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onPositiveFeedBack,
                modifier = Modifier
                    .padding(
                        top = 20.dp,
                        bottom = if(negativeText.isEmpty()) 20.dp else 0.dp
                    )
                    .height(55.dp)
            ) {
                Text(
                    text = positiveText,
                    fontFamily = poppinsFontFamily
                )
            }
            negativeText.takeIf { it.isNotEmpty() }?.let {
                TextButton(
                    onClick = onNegativeFeedBack,
                    modifier = Modifier
                        .padding(bottom = 15.dp, top = 5.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = negativeText, fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}

@CompactThemedPreviewProvider
@Composable
private fun InstfyAlertDialogPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            InstfyWarningDialog(
                onPositiveFeedBack = { /*TODO*/ },
                onNegativeFeedBack = { /*TODO*/ },
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
                        append(" 3 ")
                    }
                    append("left. Buy")
                    withStyle(
                        SpanStyle(color = MaterialTheme.colorScheme.primary)
                    ) {
                        append(" premium ")
                    }
                    append("to remove the limit.")
                },
                positiveText = "I will buy later",
                negativeText = "never mind"
            )
        }
    }
}