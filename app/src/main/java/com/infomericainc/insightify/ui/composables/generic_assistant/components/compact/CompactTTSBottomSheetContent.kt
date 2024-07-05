package com.infomericainc.insightify.ui.composables.generic_assistant.components.compact

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.composables.generic_assistant.components.medium.normalizeRMS
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun CompactTTSBottomSheetContent(
    onSpeechCompleted: (String?) -> Unit,
    onClose: () -> Unit
) {
    val speechRecognitionsIntent = Intent(
        RecognizerIntent.ACTION_RECOGNIZE_SPEECH
    ).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
    }
    val context = LocalContext.current
    var speechRecognizer: SpeechRecognizer?
    var speechResult by remember {
        mutableStateOf<String?>("")
    }
    var unRecognizedTry by remember {
        mutableIntStateOf(0)
    }
    var rmsDbSpeed by remember {
        mutableFloatStateOf(0.2f)
    }

    var showSuggestions by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.speech))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        reverseOnRepeat = false,
    )
    DisposableEffect(key1 = Unit) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer?.startListening(speechRecognitionsIntent)
        speechRecognizer?.setRecognitionListener(
            object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    showSuggestions = true
                    speechResult = "How may I help with your order?"
                }

                override fun onBeginningOfSpeech() {
                    speechResult = "Listening"
                    showSuggestions = false
                }

                override fun onRmsChanged(rmsdB: Float) {
                    rmsDbSpeed = normalizeRMS(rmsdB, maxRmsDb = 10f, minRange = 0.2f, maxRange = 1.0f)
                }

                override fun onBufferReceived(buffer: ByteArray?) {

                }

                override fun onEndOfSpeech() {
                    speechResult = "Processing"
                    showSuggestions = false
                }

                override fun onError(error: Int) {
                    coroutineScope.launch {
                        unRecognizedTry++
                        if (unRecognizedTry <= 3) {
                            speechResult =
                                "I am facing some problem while processing your voice. Let me try again."
                            delay(5000L)
                            speechResult = "How may I help with your order?"
                            speechRecognizer?.startListening(speechRecognitionsIntent)
                        } else {
                            speechResult = "Voice Not Detected. Please Try Again."
                            showSuggestions = false
                        }
                    }
                }

                override fun onResults(results: Bundle?) {
                    speechResult = results?.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                    )?.get(0)
                    onSpeechCompleted(speechResult)
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    speechResult + partialResults?.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                    )?.get(0)
                }

                override fun onEvent(eventType: Int, params: Bundle?) {

                }

            }
        )
        onDispose {
            speechRecognizer?.destroy()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    onClose()
                },
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._22sdp))
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        LottieAnimation(
            composition = composition, progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._180sdp)),
            contentScale = ContentScale.Crop
        )
        AnimatedContent(
            targetState = speechResult,
            label = "",
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) {
            Text(
                text = it ?: "Setting up",
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFontFamily,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._24ssp).value.sp,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .alpha(.5f),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        AnimatedVisibility(visible = showSuggestions) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = dimensionResource(id = com.intuit.sdp.R.dimen._140sdp))
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
            ) {
                item {
                    Spacer(modifier = Modifier.width(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)))
                }
                item {
                    Box(
                        modifier = Modifier
                            .width(dimensionResource(id = com.intuit.sdp.R.dimen._150sdp))
                            .wrapContentHeight()
                            .border(
                                width = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp),
                                shape = MaterialTheme.shapes.large,
                                brush = Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.tertiary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "How about asking! could you recommend a good biryani recipe?",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .width(dimensionResource(id = com.intuit.sdp.R.dimen._150sdp))
                            .wrapContentHeight()
                            .border(
                                width = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp),
                                shape = MaterialTheme.shapes.large,
                                brush = Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.tertiary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Or try asking, Best vegetarian food recommendations in the menu.",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .width(dimensionResource(id = com.intuit.sdp.R.dimen._150sdp))
                            .wrapContentHeight()
                            .border(
                                width = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp),
                                shape = MaterialTheme.shapes.large,
                                brush = Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.tertiary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Try! what's on the menu for today?",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


@CompactThemedPreviewProvider
@Composable
private fun CompactTTSBottomSheetContentPreview() {
    InsightifyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            CompactTTSBottomSheetContent(onSpeechCompleted = {}, onClose = {})
        }
    }
}