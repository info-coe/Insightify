package com.infomericainc.insightify.ui.composables.threads.variants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import com.infomericainc.insightify.ui.composables.threads.ThreadItemDto
import com.infomericainc.insightify.ui.composables.threads.ThreadsEvent
import com.infomericainc.insightify.ui.composables.threads.ThreadsUIState
import com.infomericainc.insightify.ui.composables.threads.components.MediumThreadItem
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.MediumThemedPreviewProvider

@Composable
fun MediumThreadsScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    threadsUIState: ThreadsUIState,
    onThreadsEvent: (ThreadsEvent) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
       // onThreadsEvent(ThreadsEvent.FetchThreadsFromFirebase)
    }
    LaunchedEffect(key1 = threadsUIState) {
        when {
            threadsUIState.isFetching -> {

            }

            threadsUIState.fetched != null -> {

            }

            threadsUIState.error != null -> {

            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)))
        }
        item {
            Box(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(
                        dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
            ) {
                Text(
                    text = "Threading functionality is still in development, We will fix this on next updates.",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))
        }
        itemsIndexed(
            listOf(
                ThreadItemDto(
                    threadName = "Thread ",
                    lastSeen = Timestamp.now(),
                    currentlyActive = true
                ),
                ThreadItemDto(
                    threadName = "Thread ",
                    lastSeen = Timestamp.now(),
                    currentlyActive = false
                ),
                ThreadItemDto(
                    threadName = "Thread ",
                    lastSeen = Timestamp.now(),
                    currentlyActive = false
                ),
                ThreadItemDto(
                    threadName = "Thread ",
                    lastSeen = Timestamp.now(),
                    currentlyActive = false
                )
            )
        ) { key, item ->
            MediumThreadItem(
                threadName = item.threadName.toString().plus(key),
                lastSeen = item.lastSeen,
                currentlyActive = item.currentlyActive,
                onItemClick = { }
            )
        }
    }
}


@MediumThemedPreviewProvider
@Composable
private fun MediumThreadsScreenPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MediumThreadsScreenContent(
                paddingValues = PaddingValues(),
                navController = rememberNavController(),
                threadsUIState = ThreadsUIState(),
                onThreadsEvent = { }
            )
        }
    }
}