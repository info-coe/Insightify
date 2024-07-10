package com.infomericainc.insightify.ui.components.placeholders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.ui.theme.poppinsFontFamily

@Composable
fun UnSupportedResolutionPlaceHolder() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Error,
            contentDescription = "",
            modifier = Modifier.size(
                dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
            ),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = "Insightify Do not Support Landscape Resolutions. Please Rotate your device to Landscape, To Continue using our application.",
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp),
                    vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                ),
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._19ssp).value.sp,
            textAlign = TextAlign.Center
        )
    }
}

