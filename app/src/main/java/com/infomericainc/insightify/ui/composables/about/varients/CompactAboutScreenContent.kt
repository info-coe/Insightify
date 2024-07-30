package com.infomericainc.insightify.ui.composables.about.varients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.infomericainc.insightify.ui.components.compact.CompactSettingsItem
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.util.Constants

@Composable
fun CompactAboutScreenContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
        )
    ) {
        item {
            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))
        }
        item {
            CompactSettingsItem(
                itemName = "Privacy Policy",
                itemDescription = "Explore more about how we collect and manage your data.",
                canShowNextArrow = true,
                onItemClick = {
                    uriHandler
                        .openUri(Constants.PRIVACY_URL)
                }
            )
        }
        item {
            CompactSettingsItem(
                itemName = "Terms & Conditions",
                itemDescription = "The terms and conditions need to follow.",
                canShowNextArrow = true,
                onItemClick = {
                    uriHandler
                        .openUri(Constants.TERMS_URL)
                }
            )
        }
        item {
            CompactSettingsItem(
                itemName = "Open Source",
                itemDescription = "Know More about the things we used in the application.",
                canShowNextArrow = true,
                onItemClick = {
                    uriHandler
                        .openUri(Constants.TERMS_URL)
                }
            )
        }
    }
}


@Preview
@Composable
private fun CompactAboutScreenContentPreview() {
    InsightifyTheme {
        Surface {
            CompactAboutScreenContent(
                paddingValues = PaddingValues()
            )
        }
    }
}