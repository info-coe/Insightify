package com.infomericainc.insightify.ui.components.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.intuit.ssp.R

@Composable
fun LoginHeader() {
    Text(
        text = buildAnnotatedString {
            append("Let's")
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                append(" sign")
            }
            append("\nyou in.")
        },
        color = MaterialTheme.colorScheme.onSurface,
        fontFamily = poppinsFontFamily,
        fontSize = dimensionResource(id = R.dimen._28ssp).value.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp))
            .padding(
                bottom = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp),
            )
            .fillMaxWidth(),
        lineHeight = dimensionResource(id = R.dimen._35ssp).value.sp,
        letterSpacing = 1.sp
    )
}