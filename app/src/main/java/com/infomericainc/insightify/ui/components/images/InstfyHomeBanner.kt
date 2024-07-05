package com.infomericainc.insightify.ui.components.images

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun InstfyHomeBanner(
    modifier: Modifier = Modifier,
    @DrawableRes imageId : Int
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 25.dp)
            .padding(top = 25.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .then(modifier)
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "HomeBanner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(0.dp),
            alpha = .9f
        )
    }
}

@CompactThemedPreviewProvider
@Composable
private fun InstfyHomeBannerPreview() {
    InsightifyTheme {
        InstfyHomeBanner(imageId = R.drawable.home_background)
    }
}