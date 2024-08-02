package com.infomericainc.insightify.ui.composables.guide.variants

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.infomericainc.insightify.ui.composables.guide.components.compact.CompactAboutInfomericaTabContent
import com.infomericainc.insightify.ui.composables.guide.components.compact.CompactContactUsTabContent
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactGuideScreen(modifier: Modifier = Modifier) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val titles = mutableListOf("About Infomerica", "Contact Us")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    )
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                ) {
                    Text(
                        text = title,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                        modifier = Modifier
                            .padding(
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        AnimatedContent(
            targetState = selectedTabIndex,
            transitionSpec = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                ) togetherWith
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )
            },
            label = "SecondaryTabAnimation"
        ) { index ->
            when (index) {
                0 -> {
                    CompactAboutInfomericaTabContent()
                }

                1 -> {
                    CompactContactUsTabContent()
                }
            }
        }
    }
}


@CompactThemedPreviewProvider
@Composable
private fun CompactGuideScreenPreview() {
    InsightifyTheme {
        Surface {
            CompactGuideScreen()
        }
    }
}