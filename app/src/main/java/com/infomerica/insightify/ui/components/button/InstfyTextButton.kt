package com.infomerica.insightify.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider

@Composable
fun InstfyTextButton(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 25.dp)
            .padding(top = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable {
                onClick()
            }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .alpha(.9f),
            fontFamily = poppinsFontFamily,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@CompactThemedPreviewProvider
@Composable
private fun InstfyTextButtonPreview() {
    InsightifyTheme {
        InstfyTextButton(
            text = buildAnnotatedString {
                append("Our")
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append(" NextGen AI ")
                }
                append("is in your hands,\n")
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                ) {
                    append("Tap here to try. ")
                }
            }
        ) {

        }
    }
}