package com.infomericainc.insightify.util

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview


@Preview(
    name = "CompactLight",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_7
)
@Preview(
    name = "CompactDark",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_7
)
annotation class CompactThemedPreviewProvider

@Preview(
    name = "MediumLight",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_TABLET,
    widthDp = 800,
    heightDp = 1200
)
@Preview(
    name = "MediumDark",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_TABLET,
    widthDp = 800,
    heightDp = 1200
)
annotation class MediumThemedPreviewProvider
