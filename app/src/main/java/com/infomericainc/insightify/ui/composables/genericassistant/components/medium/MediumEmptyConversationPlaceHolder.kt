package com.infomericainc.insightify.ui.composables.genericassistant.components.medium

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.MediumThemedPreviewProvider

@Composable
fun MediumEmptyConversationPlaceHolder() {
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
                painter = painterResource(id = com.infomericainc.insightify.R.drawable.hi_emoji),
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
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
            )
        }

    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumEmptyConversationPlaceHolderPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MediumEmptyConversationPlaceHolder()
    }
}