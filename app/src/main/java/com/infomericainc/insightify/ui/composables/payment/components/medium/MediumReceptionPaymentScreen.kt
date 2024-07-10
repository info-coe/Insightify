package com.infomericainc.insightify.ui.composables.payment.components.medium

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.MediumThemedPreviewProvider

@Composable
fun MediumReceptionPaymentScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.reception_payment),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                .size(dimensionResource(id = com.intuit.sdp.R.dimen._130sdp))
        )
        Text(
            text = "Please pay your bill at the reception.",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._40sdp)),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
            textAlign = TextAlign.Center,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
        )
        Text(
            text = "We are unable to process your payment right now, Please pay at the reception, Thank you.",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                .alpha(.6f),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
            textAlign = TextAlign.Center,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
        )

        FilledTonalButton(
            onClick = { onExitClick() },
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._35sdp))
        ) {
            Text(
                text = "Exit to HomeScreen",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
            )
        }
    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumReceptionPaymentScreenPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            MediumReceptionPaymentScreen(
                paddingValues = PaddingValues(),
            ) {

            }
        }
    }
}


private const val COMPACT_PAYMENT_SCREEN = "COMPACT_PAYMENT_SCREEN"