package com.infomericainc.insightify.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.DataUsage
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.MediumThemedPreviewProvider

@Composable
fun InstfyPermissionDialog(
    modifier: Modifier = Modifier,
    permissionIcon : ImageVector = Icons.Rounded.CloudUpload,
    permissionTitle: String = "",
    permissionDescription: AnnotatedString = buildAnnotatedString { },
    positiveText: String = "",
    negativeText: String = "",
    onPositiveFeedBack: () -> Unit,
    onNegativeFeedBack: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .wrapContentHeight()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = permissionIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                )
                Text(
                    text = permissionTitle,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(.9f),
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }

            Text(
                text = permissionDescription,
                modifier = Modifier
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth(.9f)
                    .alpha(.7f),
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal
            )
            Row(
                modifier = Modifier
                    .padding(top = 25.dp, bottom = 20.dp)
                    .fillMaxWidth(.9f)
                    .height(60.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onNegativeFeedBack,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = negativeText, fontFamily = poppinsFontFamily
                    )
                }
                Button(
                    onClick = onPositiveFeedBack,
                    modifier = Modifier.height(55.dp)
                ) {
                    Text(
                        text = positiveText,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}

@CompactThemedPreviewProvider
@MediumThemedPreviewProvider
@Composable
private fun InstfyPermissionDialogPreview() {
    InsightifyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            InstfyPermissionDialog(
                permissionTitle = "Storage permission Required",
                permissionDescription = buildAnnotatedString {
                    append(
                        "We need to access your "
                    )
                    withStyle(SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold
                    )) {
                        append("primary account information")
                    }
                    append(" to provide personalized features. This includes your ")
                    withStyle(SpanStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold
                    )) {
                        append("username, email address, profile information ")
                    }
                    append("Your data will be securely stored in")
                    withStyle(SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold
                    )) {
                        append(" Firestore")
                    }
                    append(" Do you consent to this.")
                },
                positiveText = "Accept",
                negativeText = "Decline",
                onPositiveFeedBack = {

                },
                onNegativeFeedBack = {

                }
            )
        }
    }
}