package com.infomericainc.insightify.ui.composables.profile.varients

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserProfileDto
import com.infomericainc.insightify.ui.composables.profile.UserProfileUiState
import com.infomericainc.insightify.ui.navigation.profile.ProfileScreens
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider

@Composable
private fun CompactProfileScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    userProfileUiState: UserProfileUiState
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(
                            dimensionResource(id = com.intuit.sdp.R.dimen._100sdp)
                        )
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = userProfileUiState.userProfile?.profileUrl,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = userProfileUiState.userProfile?.username ?: "",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._22ssp).value.sp,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._30ssp).value.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
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
                    text = userProfileUiState.userProfile?.email ?: "",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                        .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth()
                    .heightIn(max = dimensionResource(id = com.intuit.sdp.R.dimen._150sdp))
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .weight(.9f)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .clickable {
                            navController.navigate(ProfileScreens.SettingsScreen.route)
                        }
                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Settings",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                        modifier = Modifier
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Customize your application",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .weight(.9f)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .clickable {
                            navController.navigate(ProfileScreens.ProfileCustomizationScreen.route)
                        }
                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Edit",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                    )
                    Text(
                        text = "Customize your profile",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp

                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .heightIn(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Business,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(.2f)
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                    )
                    Column(
                        modifier = Modifier
                            .weight(.8f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "About us",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                        )
                        Text(
                            text = "Like to know more about us.",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(.2f)
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                    )
                }
                HorizontalDivider(
                    thickness = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp),
                    color = MaterialTheme.colorScheme.surface
                )
                Row(
                    modifier = Modifier
                        .clickable {
                            navController
                                .navigate(ProfileScreens.AboutScreenSpec.route)
                        }
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(.2f)
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._22sdp))
                    )
                    Column(
                        modifier = Modifier
                            .weight(.8f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "About",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                        )
                        Text(
                            text = "Some information about the application.",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(.2f)
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .heightIn(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.StarBorder,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(.2f)
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                    )
                    Column(
                        modifier = Modifier
                            .weight(.8f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Rate us on playstore",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                        )
                        Text(
                            text = "Help us by rating this application on play store.",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(.2f)
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                    )
                }
                HorizontalDivider(
                    thickness = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp),
                    color = MaterialTheme.colorScheme.surface
                )
                Row(
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .weight(.2f)
                            .size(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Log out",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                        )
                        Text(
                            text = "Time to say goodByes.",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
                        )
                    }
                }
            }
        }

    }
}

@CompactThemedPreviewProvider
@Composable
private fun CompactProfileScreenContentPreview() {
    InsightifyTheme {
        Surface {
            CompactProfileScreenContent(
                paddingValues = PaddingValues(),
                navController = rememberNavController(),
                UserProfileUiState(
                    userProfile = UserProfileDto(
                        username = "Lisa",
                        email = "Lisa@gmail.com"
                    )
                )
            )
        }
    }
}