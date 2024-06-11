package com.infomerica.insightify.ui.components.text

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider

/**
 * This Large text is used for
 * Displaying Bigger Text.
 * This is the Biggest Text in the application.
 * Weight - Medium
 */
@Composable
fun InstfyBoldText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight : Float
) {
    Text(
        text = text,
        modifier = Modifier
            .wrapContentSize()
            .then(modifier),
        fontFamily = poppinsFontFamily,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        lineHeight = lineHeight.sp
    )
}

@CompactThemedPreviewProvider
@Composable
private fun InstfyTitleTextPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            InstfyMediumText(text = "Recent Sessions", fontSize = 50, lineHeight = 55)
        }
    }
}