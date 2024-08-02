package com.infomericainc.insightify.ui.composables.aboutRestaurant.variants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun CompactAboutRestaurantScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(
                        dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
            ) {
                Text(
                    text = "This screen is still in development, We will fix this on next updates.",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@CompactThemedPreviewProvider
@Composable
private fun CompactAboutRestaurantScreenPreview() {
    InsightifyTheme {
        Surface {
            CompactAboutRestaurantScreen(paddingValues = PaddingValues())
        }
    }
}