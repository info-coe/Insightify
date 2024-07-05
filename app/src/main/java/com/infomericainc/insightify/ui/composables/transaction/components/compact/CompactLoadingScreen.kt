package com.infomericainc.insightify.ui.composables.transaction.components.compact

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.dark_Pending
import com.infomericainc.insightify.ui.theme.dark_PendingContainer
import com.infomericainc.insightify.ui.theme.light_Pending
import com.infomericainc.insightify.ui.theme.light_PendingContainer
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun CompactLoadingScreen(modifier: Modifier = Modifier) {

    val isInDarkTheme = isSystemInDarkTheme()

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(dimensionResource(id = com.intuit.sdp.R.dimen._170sdp)),
            strokeWidth = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
            trackColor = if (isInDarkTheme) dark_PendingContainer else light_PendingContainer,
            strokeCap = StrokeCap.Round,
            color = if (isInDarkTheme) dark_Pending else light_PendingContainer
        )
        Text(
            text = "Processing your payment\nplease wait",
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)
                ),
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
            textAlign = TextAlign.Center,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp
        )
    }
}


@CompactThemedPreviewProvider
@Composable
private fun CompactLoadingScreenPreview() {
    InsightifyTheme {
        Surface {
            CompactLoadingScreen()
        }
    }
}