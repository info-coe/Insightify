package com.infomerica.insightify.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.infomerica.insightify.R
import com.infomerica.insightify.ui.theme.InsightifyTheme
import com.infomerica.insightify.util.CompactThemedPreviewProvider


/**
 * This large text is used for displaying
 * application title.
 */
@Composable
fun InstfyCompanyLogo(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int,
) {
    Image(
        painter = painterResource(id = imageId),
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth(.8f)
            .height(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
            .then(modifier)
    )
}

@CompactThemedPreviewProvider
@Composable
private fun InstfyLargeTextPreview() {
    InsightifyTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            InstfyCompanyLogo(imageId = R.drawable.company_logo)
        }
    }
}