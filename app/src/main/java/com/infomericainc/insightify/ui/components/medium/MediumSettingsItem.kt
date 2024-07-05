package com.infomericainc.insightify.ui.components.medium

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.MediumThemedPreviewProvider

@Composable
fun MediumSettingsItem(
    itemName: String,
    itemDescription: String,
    canShowNextArrow: Boolean,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.8f)
        ) {
            Text(
                text = itemName,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp
            )
            Text(
                text = itemDescription,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                    .alpha(.7f)
            )
        }
        if (canShowNextArrow) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .weight(.2f)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForwardIos,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._14sdp))
                )
            }
        }
    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumSettingsItemPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
        )
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)))
        MediumSettingsItem(
            "ChatBot Configs",
            "Tweak your assistant configuration.",
            canShowNextArrow = true
        ) {

        }
        MediumSettingsItem(
            "Manage Threads",
            "Customize your conversation threads.",
            canShowNextArrow = true
        ) {

        }
    }
}