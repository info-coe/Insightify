package com.infomericainc.insightify.ui.composables.threads.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.formatDate
import com.intuit.ssp.R

@Composable
fun CompactThreadItem(
    threadName: String,
    lastSeen: Timestamp,
    currentlyActive: Boolean,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(if (currentlyActive) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent)
            .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
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
                text = threadName,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = R.dimen._16ssp).value.sp,
                lineHeight = dimensionResource(id = R.dimen._22ssp).value.sp
            )
            Text(
                text = "Last used ${formatDate(lastSeen.toDate())}",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = R.dimen._12ssp).value.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                    .alpha(.7f)
            )

            if (currentlyActive) {
                Text(
                    text = "Currently Active",
                    modifier = Modifier
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp))
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)
                        ),
                    color = MaterialTheme.colorScheme.error,
                    fontFamily = poppinsFontFamily
                )
            }

        }
    }
}

@CompactThemedPreviewProvider
@Composable
private fun CompactThreadItemPreview() {
    InsightifyTheme {
        Surface {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .fillMaxSize()
            ) {
                item {
                    CompactThreadItem(
                        threadName = "Thread 1",
                        lastSeen = Timestamp.now() ,
                        currentlyActive = true
                    ) {

                    }
                }
                item {
                    CompactThreadItem(
                        threadName = "Thread $1",
                        lastSeen = Timestamp.now(),
                        currentlyActive = false
                    ) {

                    }
                }
                item {
                    CompactThreadItem(
                        threadName = "Thread $1",
                        lastSeen = Timestamp.now(),
                        currentlyActive = false
                    ) {

                    }
                }
            }
        }
    }
}