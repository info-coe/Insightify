package com.infomericainc.insightify.ui.composables.guide.components.compact

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.Constants

@Composable
fun CompactContactUsTabContent(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.infomerica_office),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._200sdp)),
                contentScale = ContentScale.Crop
            )
        }
        item {
            Text(
                text = "Infomerica is always there for your bussiness needs.",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    ),
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._24ssp).value.sp,
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
        }

        item {
            Text(
                text = "Thank you for your interest in Infomerica services. Please provide the following information about your business needs to help us serve you better. This information will enable us to route your request to the appropriate person. You should receive a response within 8 hours.",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .alpha(.8f),
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp,
                textAlign = TextAlign.Justify
            )
        }
        item {
            Button(
                onClick = { uriHandler.openUri(Constants.CONTACT_US_URL) },
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
            ) {
                Text(
                    text = "Contact us",
                    fontFamily = poppinsFontFamily
                )
                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                )
            }
        }
    }
}

@CompactThemedPreviewProvider
@Composable
private fun CompactContactUsTabContentPreview() {
    InsightifyTheme {
        Surface {
            CompactContactUsTabContent()
        }
    }
}