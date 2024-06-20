package com.infomerica.insightify.ui.components.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.intuit.sdp.R

@Composable
fun CompactLoginDivider() {
    Row(
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen._15sdp))
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.width(
                dimensionResource(id = R.dimen._65sdp)
            )
        )
        Text(
            text = "or",
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen._14sdp)
            ),
            textAlign = TextAlign.Center,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        )
        HorizontalDivider(
            modifier = Modifier.width(
                dimensionResource(id = R.dimen._65sdp)
            )
        )
    }
}

@Composable
fun MediumLoginDivider() {
    Row(
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen._15sdp))
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.width(
                dimensionResource(id = R.dimen._65sdp)
            )
        )
        Text(
            text = "or",
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen._18sdp)
            ),
            textAlign = TextAlign.Center,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
        )
        HorizontalDivider(
            modifier = Modifier.width(
                dimensionResource(id = R.dimen._65sdp)
            )
        )
    }
}