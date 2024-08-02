package com.infomericainc.insightify.ui.composables.settings.variants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infomericainc.insightify.extension.makeToast
import com.infomericainc.insightify.ui.components.compact.CompactSettingsItem
import com.infomericainc.insightify.ui.navigation.profile.ProfileScreens
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.intuit.sdp.R

@Composable
fun CompactSettingsScreen(
    paddingValues: PaddingValues,
    navController: NavController
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen._20sdp)),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen._10sdp)
        )
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen._5sdp)))
        CompactSettingsItem(
            "ChatBot Configs",
            "Tweak your assistant configuration.",
            canShowNextArrow = true
        ) {
            context.makeToast("Will avail soon.")
        }
        CompactSettingsItem(
            "Manage Threads",
            "Customize your conversation threads.",
            canShowNextArrow = true
        ) {
            navController.navigate(ProfileScreens.ThreadsScreen.route)
        }
    }
}

@CompactThemedPreviewProvider
@Composable
private fun CompactSettingsScreenPreview() {
    InsightifyTheme {
        Surface {
            CompactSettingsScreen(paddingValues = PaddingValues(), navController = rememberNavController())
        }
    }
}