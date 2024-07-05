package com.infomericainc.insightify.ui.composables.genericassistant.components.medium

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.intuit.sdp.R

@Composable
fun MediumTypingProgressPlaceHolder(
    modifier: Modifier = Modifier,
    lottieComposition: LottieComposition?,
    progress : Float
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = com.infomericainc.insightify.R.drawable.profile_place_holder),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Top)
                .padding(top = dimensionResource(id = R.dimen._3sdp))
                .padding(start = dimensionResource(id = R.dimen._10sdp))
                .size(
                    dimensionResource(id = R.dimen._30sdp)
                )
        )
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .padding(bottom = dimensionResource(id = R.dimen._10sdp), start = dimensionResource(id = R.dimen._10sdp))
                .wrapContentHeight()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = lottieComposition,
                progress = {
                    progress
                },
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen._5sdp), end = dimensionResource(id = R.dimen._5sdp))
                    .padding(vertical = dimensionResource(id = R.dimen._5sdp))
                    .width(dimensionResource(id = R.dimen._40sdp))
                    .height(dimensionResource(id = R.dimen._20sdp))
                    .scale(1.4f)
            )
        }
    }
}