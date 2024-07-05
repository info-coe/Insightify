package com.infomericainc.insightify.ui.components.text

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun InstfyRegularText(
    modifier: Modifier = Modifier,
    text : String,
    fontSize: Int,
) {
    Text(
        text = text,
        modifier = Modifier
            .wrapContentHeight()
            .then(modifier),
        fontFamily = poppinsFontFamily,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Normal
    )
}

@CompactThemedPreviewProvider
@Composable
private fun InstfyGreetingTextPreview() {
    InsightifyTheme {
        InstfyRegularText(text = "Hey there 👋,", fontSize = 26)
    }
}