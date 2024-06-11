package com.infomerica.insightify.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider

@Composable
fun InstfyAlertDialog(
    title: String = "",
    description: String = "",
    positiveText : String = "",
    negativeText : String = "",
    onPositiveFeedBack : () -> Unit,
    onNegativeFeedBack : () -> Unit
) {
    Dialog(
        onDismissRequest = {  },
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
            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(.9f),
                fontSize = 22.sp,
                lineHeight = 30.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium
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
                fontWeight = FontWeight.Normal
            )
            Row(
                modifier = Modifier
                    .padding(top = 25.dp, bottom = 20.dp)
                    .fillMaxWidth(.9f)
                    .height(60.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onNegativeFeedBack,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = negativeText, fontFamily = poppinsFontFamily
                    )
                }
                Button(
                    onClick = onPositiveFeedBack,
                    modifier = Modifier.height(55.dp)
                ) {
                    Text(
                        text = positiveText,
                        fontFamily = poppinsFontFamily
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
            InstfyAlertDialog(
                title = "Would you like to save this session?",
                description = "By saving the session you can save this conversation.",
                positiveText = "Yes, save",
                negativeText = "No, Don't save",
                onPositiveFeedBack = {

                },
                onNegativeFeedBack = {

                }
            )
        }
    }
}