package com.infomerica.insightify.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.infomerica.insightify.R
import com.infomerica.insightify.ui.components.InstfyLottie
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider

@Composable
fun InstfyErrorDialog(
    title: String = "",
    description: String = "",
    onPositiveFeedBack: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .wrapContentHeight()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InstfyLottie(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(120.dp)
                    .height(60.dp),
                resource = R.raw.error,
                reverseOnRepeat = false,
                contentScale = ContentScale.Crop
            )

            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 20.dp)
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
                    .padding(top = 10.dp, bottom = 20.dp)
                    .fillMaxWidth(.9f)
                    .alpha(.7f),
                fontSize = 16.sp,
                lineHeight = 25.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .padding(bottom = 15.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .height(55.dp)
                ) {
                    Text(
                        text = "Exit and Try again.",
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}

@CompactThemedPreviewProvider
@Composable
private fun InstfyProgressDialogPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            InstfyErrorDialog(
                title = "We unable to load your data.",
                description = "We are facing some trouble, While loading your data. We Recommend you to go back and try again.",
            ) {

            }
        }
    }
}