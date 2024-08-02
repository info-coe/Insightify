package com.infomericainc.insightify.ui.composables.profileCustomization.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
fun CompactProfileCustomizationItem(
    modifier: Modifier = Modifier,
    itemName: String = "Default Item",
    itemDescription: String = "Some Description about the default Item.",
    icon: ImageVector = Icons.Outlined.Settings,
    showNextIcons: Boolean = true,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick()
            }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier
                .weight(.2f)
                .size(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
            tint = MaterialTheme.colorScheme.primary
        )
        Column(
            modifier = Modifier
                .weight(.6f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = itemName,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
            )
            Text(
                text = itemDescription,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                modifier = Modifier
                    .alpha(.6f)
            )
        }
        if (showNextIcons) {
            IconButton(
                onClick = { onItemClick() },
                modifier = Modifier
                    .weight(.2f)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForwardIos,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@CompactThemedPreviewProvider
@Composable
private fun CompactProfileCustomizationItemPreview() {
    InsightifyTheme {
        Surface {
            CompactProfileCustomizationItem {

            }
        }
    }
}