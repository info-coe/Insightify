package com.infomerica.insightify.ui.composables.chatbot.generic_assistant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.intuit.sdp.R

@Composable
fun TypingProgressPlaceHolder(
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
            painter = painterResource(id = com.infomerica.insightify.R.drawable.profile_place_holder),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Top)
                .padding(top = dimensionResource(id = R.dimen._3sdp))
                .size(
                    dimensionResource(id = R.dimen._30sdp)
                )
        )
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .padding(bottom = 10.dp, start = 10.dp)
                .wrapContentHeight()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = lottieComposition,
                progress = {
                    progress
                },
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .padding(vertical = 10.dp)
                    .width(50.dp)
                    .height(30.dp)
                    .scale(1.6f)
            )
        }
    }
}