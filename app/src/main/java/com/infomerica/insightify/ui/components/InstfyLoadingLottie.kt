package com.infomerica.insightify.ui.components

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun InstfyLottie(
    modifier: Modifier = Modifier,
    @RawRes resource : Int,
    foreverIteration : Boolean = true,
    isPlaying : Boolean = true,
    speed : Float = 1f,
    reverseOnRepeat : Boolean = true,
    contentScale: ContentScale = ContentScale.None
) {
    val loadingComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(resource))
    val loadingProgress by animateLottieCompositionAsState(
        loadingComposition,
        isPlaying = isPlaying,
        iterations = if(foreverIteration) LottieConstants.IterateForever else 1,
        reverseOnRepeat = reverseOnRepeat,
        speed = speed,
    )

    LottieAnimation(
        composition = loadingComposition,
        progress = { loadingProgress },
        modifier = modifier,
        renderMode = RenderMode.AUTOMATIC,
        contentScale = contentScale
    )
}