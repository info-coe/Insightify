package com.infomericainc.insightify.ui.composables.profileCustomization.variants

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FilePresent
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.infomericainc.insightify.R
import com.infomericainc.insightify.extension.makeToast
import com.infomericainc.insightify.ui.composables.profileCustomization.ProfileUIState
import com.infomericainc.insightify.ui.composables.profileCustomization.components.CompactProfileCustomizationItem
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.Constants

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompactProfileCustomizationContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavController,
    userProfileUIState: ProfileUIState
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Profile Customization",
                    fontFamily = poppinsFontFamily,
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._60sdp))
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                ) {
                    AsyncImage(
                        model = userProfileUIState.userProfileDto?.profileUrl,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = userProfileUIState.userProfileDto?.username ?: "",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.error
                                )
                            )
                        )
                    )
                    Text(
                        text = userProfileUIState.userProfileDto?.email ?: "",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.PhoneAndroid,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Your device is registered on table number ${userProfileUIState.userConfigurationDto?.registeredTableNumber}",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        ),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        item {
            Text(
                text = "Account",
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                        bottom = dimensionResource(id = com.intuit.ssp.R.dimen._15ssp)
                    ),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
            )
        }
        item {
            CompactProfileCustomizationItem(
                itemName = "Profile Information",
                itemDescription = "Customize username, profile.",
                icon = ImageVector.vectorResource(id = R.drawable.user),
                showNextIcons = true,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    ),
                onItemClick = {
                    context.makeToast("In development, Will avail soon.")
                }
            )
        }
        item {
            CompactProfileCustomizationItem(
                itemName = "Account deletion",
                itemDescription = "Delete your insightify account.",
                showNextIcons = true,
                icon = Icons.Rounded.Delete,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                    ),
                onItemClick = {
                    uriHandler
                        .openUri(Constants.CONTACT_US_URL)
                }
            )
        }
        item {
            Text(
                text = "Help & Support",
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                        bottom = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp)
                    ),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
            )
        }
        item {
            CompactProfileCustomizationItem(
                itemName = "Privacy Policy",
                itemDescription = "About how we use your data.",
                showNextIcons = true,
                icon = Icons.Rounded.Lock,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    ),
                onItemClick = {
                    uriHandler
                        .openUri(Constants.PRIVACY_URL)
                }
            )
        }
        item {
            CompactProfileCustomizationItem(
                itemName = "Terms & Conditions",
                itemDescription = "Some information about the terms and conditions.",
                showNextIcons = true,
                icon = Icons.Rounded.FilePresent,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                    ),
                onItemClick = {
                    uriHandler
                        .openUri(Constants.TERMS_URL)
                }
            )
        }

        item {
            Column(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Insighitfy",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .alpha(.6f),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
                )
                Text(
                    text = Constants.VERSION,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .alpha(.6f),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
                )
            }
        }
    }
}

@CompactThemedPreviewProvider
@Composable
private fun CompactProfileCustomizationContentPreview() {
    InsightifyTheme {
        Surface {
            CompactProfileCustomizationContent(
                paddingValues = PaddingValues(),
                navController = rememberNavController(),
                userProfileUIState = ProfileUIState()
            )
        }
    }
}
