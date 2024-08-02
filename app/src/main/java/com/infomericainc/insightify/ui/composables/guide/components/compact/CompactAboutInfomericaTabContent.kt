package com.infomericainc.insightify.ui.composables.guide.components.compact

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoMode
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material.icons.rounded.Money
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.CompactThemedPreviewProvider
import com.infomericainc.insightify.util.Constants

@Composable
fun CompactAboutInfomericaTabContent(
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.company_logo),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._180sdp))
            )
        }
        item {
            Text(
                text = "Business growth through\n" +
                        "Innovative approaches ",
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._24ssp).value.sp,
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
        }

        item {
            Text(
                text = "Our Bussiness Partners",
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .fillMaxWidth()
                    .alpha(.6f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
            )
        }

        item {
            LazyRow(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth()
                    .heightIn(
                        max = dimensionResource(id = com.intuit.sdp.R.dimen._80sdp)
                    )
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
            ) {
                item {
                    Image(
                        painter = painterResource(id = R.drawable.microsoft_gold_partner),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                            )
                            .fillMaxHeight()
                    )
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.cisco),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                            )
                            .fillMaxHeight()
                    )
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.appian),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                            )
                            .fillMaxHeight()
                    )
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.ui_path),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                            )
                            .fillMaxHeight()
                    )
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.oracle),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                                end = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                            )
                            .fillMaxHeight(),
                        colorFilter = ColorFilter.lighting(Color.White, Color.Red)
                    )
                }
            }
        }

        item {
            Text(
                text = "Who we are",
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,

                )
        }

        item {
            Text(
                text = "Established in 1998, Infomerica, Inc. is a renowned Global Systems Integrator focusing on ERP/ BPM/Cloud/Mobile/SOA/API/Digital transformation Technologies. We are a Minority Certified Business, with our headquarters situated in Cary, NC – USA. For over twenty-five years, Infomerica has been instrumental in aiding a myriad of large and mid-sized businesses in transitioning their IT to a Service-Oriented Architecture, involving IT Transformation Initiatives and the deployment of SAP/Oracle ERP Systems.\n",
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth()
                    .alpha(.8f),
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
            )
        }

        item {
            Text(
                text = "Why choose us?",
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
            )
        }

        item {
            Text(
                text = "We strive for your business growth through innovative approaches.",
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                    )
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
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
        }

        item {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                    )
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(.45f)
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Rounded.Leaderboard, contentDescription = "")
                    Text(
                        text = "Thought Leadership",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            ),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)))
                Column(
                    modifier = Modifier
                        .weight(.45f)
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Rounded.Work, contentDescription = "")
                    Text(
                        text = "Excellence",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            ),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }

        item {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(.45f)
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Rounded.AutoMode, contentDescription = "")
                    Text(
                        text = "Innovation",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            ),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)))
                Column(
                    modifier = Modifier
                        .weight(.45f)
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Rounded.Person, contentDescription = "")
                    Text(
                        text = "Customer Ecentric",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            ),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }

        item {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .padding(
                        top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Rounded.Money, contentDescription = "")
                Text(
                    text = "Cost Effective",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        ),
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            Button(
                onClick = { uriHandler.openUri(Constants.INFOMERICA_URL) },
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                    )
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
            ) {
                Text(
                    text = "See more about us",
                    fontFamily = poppinsFontFamily
                )
                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                )
            }
        }

    }
}

@CompactThemedPreviewProvider
@Composable
private fun CompactAboutInfomericaTabContentPreview() {
    InsightifyTheme {
        Surface {
            CompactAboutInfomericaTabContent()
        }
    }
}