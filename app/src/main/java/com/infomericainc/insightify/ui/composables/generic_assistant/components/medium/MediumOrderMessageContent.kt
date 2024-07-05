package com.infomericainc.insightify.ui.composables.generic_assistant.components.medium

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.GenericAssistantConstants
import com.infomericainc.insightify.util.MediumThemedPreviewProvider
import com.intuit.sdp.R


@Composable
fun MediumOrderItem(itemNumber: Int?, name: String?, price: Double?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen._10sdp))
            .padding(bottom = dimensionResource(id = R.dimen._5sdp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(itemNumber != 0) {
            Text(
                text = itemNumber.toString().plus(". "),
                fontSize = GenericAssistantConstants.Medium.billItemTextSize(),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                modifier = Modifier
                    .padding(
                        end = dimensionResource(id = R.dimen._5sdp)
                    )
                    .align(Alignment.Top),
                maxLines = 3,
                textAlign = TextAlign.Start
            )
        }
        Text(
            text = name ?: "",
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
            modifier = Modifier
                .weight(.7f)
                .padding(end = dimensionResource(id = R.dimen._10sdp))
                .alpha(.7f),
            maxLines = 3,
            textAlign = TextAlign.Start
        )
        Text(
            text = "$ ".plus(price.toString()),
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .weight(.3f)
                .alpha(.8f)
        )
    }
}


@Composable
@MediumThemedPreviewProvider
private fun MediumOrderMessagePreview() {
    Surface {
        Column {
            MediumOrderItem(
                itemNumber = 1,
                name = "Masala Tea",
                price = 12.22
            )
        }
    }
}