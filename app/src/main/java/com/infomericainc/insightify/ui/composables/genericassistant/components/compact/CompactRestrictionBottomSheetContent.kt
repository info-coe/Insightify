package com.infomericainc.insightify.ui.composables.genericassistant.components.compact

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun CompactRestrictionBottomSheetContent(
    navController: NavController
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.conversation_limit),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._200sdp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "You reached your conversation limit with our assistant",
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                )
                .padding(
                    top = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp),
                )
                .fillMaxWidth()
        )
        Text(
            text = "Please contacts us to access the full version of our assistant and to unlock more features.",
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                )
                .padding(
                    top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                )
                .fillMaxWidth()
        )

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp),
                    vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                )
                .height(
                    dimensionResource(id = com.intuit.sdp.R.dimen._35sdp)
                )
        ) {
            Text(
                text = "Exit",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
            )
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
            CompactRestrictionBottomSheetContent(rememberNavController())
        }
    }
}