package com.infomericainc.insightify.ui.composables.generic_assistant.components.medium

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.infomericainc.insightify.R
import com.infomericainc.insightify.ui.composables.generic_assistant.AssistantConversationModel
import com.infomericainc.insightify.ui.composables.generic_assistant.DemoConversation
import com.infomericainc.insightify.ui.composables.generic_assistant.components.compact.CompactOrderItem
import com.infomericainc.insightify.ui.composables.generic_assistant.menu.Item
import com.infomericainc.insightify.ui.composables.generic_assistant.menu.Menu
import com.infomericainc.insightify.ui.composables.generic_assistant.order.Orders
import com.infomericainc.insightify.ui.theme.InsightifyTheme
import com.infomericainc.insightify.ui.theme.poppinsFontFamily
import com.infomericainc.insightify.util.GenericAssistantConstants
import com.infomericainc.insightify.util.MediumThemedPreviewProvider
import timber.log.Timber

@Composable
fun MediumAssistantConversationMessage(
    modifier: Modifier = Modifier,
    assistantConversationModel: AssistantConversationModel,
    onItemClick: (Item?) -> Unit,
    onMenuTap: () -> Unit,
    onMenuOrderConfirmation: (Orders?) -> Unit
) {
    if (assistantConversationModel.isFromUser) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .padding(end = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth(.8f)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = assistantConversationModel.message.toString(),
                    modifier = Modifier
                        //padding inside the text
                        .padding(
                            start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                            bottom = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                        )
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                        //padding outside the text
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        ),
                    fontSize =  GenericAssistantConstants.Medium.defaultTextSize(),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight =GenericAssistantConstants.Medium.defaultLineHeight()
                )
            }
        }
    } else {
        val isJSONResponse = assistantConversationModel.message?.contains("{") == true
        if (isJSONResponse) {
            val isBill = try {
                //Replaces the Extra characters in String
                val formattedString = assistantConversationModel.message?.replace("```", "")
                val finalString = formattedString?.replace("json", "")
                //Filter the Json Response By using { and }
                val startIndex = finalString?.indexOf("{")
                val endIndex = finalString?.lastIndexOf("}")?.plus(1)
                //Extract the json code inside Json
                val jsonString = finalString?.substring(startIndex ?: 0, endIndex ?: 0)
                //Convert the json code to object.
                val jsonParser = JsonParser.parseString(jsonString).asJsonObject
                //If the Json Code Contains OrderData.
                if (
                    jsonParser.has("OrderData")
                ) {
                    Gson().fromJson(jsonString, Orders::class.java)
                } else {
                    null
                }
            } catch (e: Exception) {
                Timber
                    .tag("AssistantConversation")
                    .e("Exception caught ${e.printStackTrace()}")
                null
            }
            val isMenu = try {
                val formattedString = assistantConversationModel.message?.replace("```", "")
                val finalString = formattedString?.replace("json", "")
                val startIndex = finalString?.indexOf("{")
                val endIndex = finalString?.lastIndexOf("}")?.plus(1)
                val jsonString = finalString?.substring(startIndex ?: 0, endIndex ?: 0)
                val jsonParser = JsonParser.parseString(jsonString).asJsonObject
                Timber
                    .tag("jsonParserResult")
                    .d(jsonParser.toString())
                if (
                    jsonParser.has("menuData")
                ) {
                    Gson().fromJson(jsonString, Menu::class.java)
                } else {
                    null
                }
            } catch (e: Exception) {
                Timber
                    .tag("AssistantConversation")
                    .d("Exception caught ${e.printStackTrace()}")
                null
            }
            when {
                isBill != null -> AssistantOrderMessage(
                    modifier,
                    isBill,
                    onMenuTap = onMenuTap,
                    onMenuOrderConfirmation = {
                        onMenuOrderConfirmation(it)
                    }
                )

                isMenu != null -> AssistantMenuMessage(modifier,isMenu) {
                    onItemClick(it)
                }

            }
        } else {
            val isOrderConfirmation = assistantConversationModel.message?.contains("ORDER_CONFORM")
            if (isOrderConfirmation == true) {
                Row(
                    modifier = Modifier
                        .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        .fillMaxWidth(.8f)
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_place_holder),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.Top)
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                            .size(
                                GenericAssistantConstants.Medium.defaultProfileSize()
                            )
                    )
                    Text(
                        text = "Visit your cart to Confirm your order.",
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                bottom = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                            )
                            .clip(MaterialTheme.shapes.extraLarge)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            ),
                        fontSize = GenericAssistantConstants.Medium.defaultTextSize(),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = GenericAssistantConstants.Medium.defaultLineHeight()
                    )
                }
            } else {
                //General Assistant Text
                Row(
                    modifier = Modifier
                        .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        .fillMaxWidth(.8f)
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_place_holder),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.Top)
                            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                            .size(
                                GenericAssistantConstants.Medium.defaultProfileSize()
                            )
                    )
                    Text(
                        text = assistantConversationModel.message.toString(),
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                bottom = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                            )
                            .clip(MaterialTheme.shapes.extraLarge)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                            .padding(
                                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            ),
                        fontSize = GenericAssistantConstants.Medium.defaultTextSize(),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = GenericAssistantConstants.Medium.defaultLineHeight()
                    )
                }
            }
        }
    }
}

@Composable
fun AssistantConversationMessageError(
    assistantConversationModel: AssistantConversationModel
) {
    if (assistantConversationModel.isFromUser) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .padding(end = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    .fillMaxWidth(.8f)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = assistantConversationModel.message.toString(),
                    modifier = Modifier
                        //padding inside the text
                        .padding(
                            start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                            bottom = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                        )
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                        //padding outside the text
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                            vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        ),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._17ssp).value.sp
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                .fillMaxWidth(.8f)
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_place_holder),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                    .size(
                        dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)
                    )
            )
            Text(
                text = assistantConversationModel.message.toString(),
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                    )
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.errorContainer
                    )
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    ),
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._17ssp).value.sp
            )
        }
    }
}

@Composable
fun AssistantOrderMessage(
    modifier: Modifier = Modifier,
    orderedItems: Orders,
    onMenuTap: () -> Unit,
    onMenuOrderConfirmation: (Orders?) -> Unit
) {
    val orderMessageTitle = buildAnnotatedString {
        append(
            "Thank you for choosing"
        )
        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(" IndoChinese Restaurant")
        }
        append("!\n")
        append("Before we dive into the delectable feast, could you please")
        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(" double-check")
        }
        append(" your order? We want to make sure your taste buds are in for a delightful experience! \uD83D\uDE0A\uD83C\uDF5C")
    }
    Row(
        modifier = Modifier
            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
            .fillMaxWidth(.9f)
            .wrapContentHeight()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_place_holder),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Top)
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                .size(
                    dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                )
        )
        Column {
            Column(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                    )
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
            ) {
                Text(
                    text = orderMessageTitle,
                    fontSize = GenericAssistantConstants.Medium.defaultTextSize(),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = GenericAssistantConstants.Medium.defaultLineHeight()
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)))
                Text(
                    text = "#DoubleCheckForDeliciousness",
                    fontSize = GenericAssistantConstants.Medium.defaultTextSize(),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = GenericAssistantConstants.Medium.defaultLineHeight(),
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                    )
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .clickable {
                        onMenuTap()
                    }
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Order Overview",
                        modifier = Modifier
                            .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = GenericAssistantConstants.Medium.billHeadingTextSize(),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (orderedItems.OrderData.Drinks != null) {
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    ) {
                        Text(
                            text = "Drinks",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = GenericAssistantConstants.Medium.billItemTextHeadingSize(),
                            modifier = Modifier.padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        )
                        orderedItems.OrderData.Drinks.forEachIndexed { index, drink ->
                            MediumOrderItem(
                                itemNumber = index + 1,
                                name = drink.name,
                                price = drink.price
                            )
                        }
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))
                    }
                }

                if (orderedItems.OrderData.Soups != null) {
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    ) {
                        Text(
                            text = "Soups",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize =  GenericAssistantConstants.Medium.billItemTextHeadingSize(),
                            modifier = Modifier.padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        )
                        orderedItems.OrderData.Soups.forEachIndexed { index, soup ->
                            MediumOrderItem(
                                itemNumber = index + 1,
                                name = soup.name,
                                price = soup.price
                            )
                        }
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))

                    }
                }

                if (orderedItems.OrderData.Appetizers != null) {
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    ) {
                        Text(
                            text = "Appetizers",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize =  GenericAssistantConstants.Medium.billItemTextHeadingSize(),
                            modifier = Modifier.padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        )
                        orderedItems.OrderData.Appetizers.forEachIndexed { index, appetizer ->
                            MediumOrderItem(
                                itemNumber = index + 1,
                                name = appetizer.name,
                                price = appetizer.price
                            )
                        }
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))

                    }
                }

                if (orderedItems.OrderData.Main != null) {
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    ) {
                        Text(
                            text = "Main Course",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize =  GenericAssistantConstants.Medium.billItemTextHeadingSize(),
                            modifier = Modifier.padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        )
                        orderedItems.OrderData.Main.forEachIndexed { index, main ->
                            MediumOrderItem(
                                itemNumber = index + 1,
                                name = main.name,
                                price = main.price
                            )
                        }
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))

                    }
                }

                if (orderedItems.OrderData.Desert != null) {
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                    ) {
                        Text(
                            text = "Desert",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize =  GenericAssistantConstants.Medium.billItemTextHeadingSize(),
                            modifier = Modifier.padding(bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                        )
                        orderedItems.OrderData.Desert.forEachIndexed { index, desert ->
                            MediumOrderItem(
                                itemNumber = index + 1,
                                name = desert.name,
                                price = desert.price
                            )
                        }
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))

                    }
                }

                if (orderedItems.OrderData.Total != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    ) {
                        Text(
                            text = "Order Summary",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._17ssp).value.sp,
                            modifier = Modifier.padding(
                                top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                            )
                        )
                        CompactOrderItem(
                            itemNumber = 0,
                            name = "Sub Total",
                            price = orderedItems.OrderData.Total.Amount
                        )
                        CompactOrderItem(
                            itemNumber = 0,
                            name = "Taxes",
                            price = orderedItems.OrderData.Total.GST
                        )
                        CompactOrderItem(
                            itemNumber = 0,
                            name = "Total cost",
                            price = orderedItems.OrderData.Total.GrandTotal
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))
                    }
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                    )
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .alpha(.8f)
            ) {
                Text(
                    text = "If you're satisfied with your order overview, kindly let us know by responding to the button, and we'll proceed to the next step in processing your order.",
                    modifier = Modifier.padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                    fontFamily = poppinsFontFamily,
                    fontSize = GenericAssistantConstants.Medium.billItemTextSize(),
                    lineHeight = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(
                    onClick = { onMenuOrderConfirmation(orderedItems) },
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        )
                        .padding(
                            horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                        )
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
                ) {
                    Text(
                        text = "Add to order",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp,
                        lineHeight = GenericAssistantConstants.Medium.defaultLineHeight()
                    )
                }
            }

        }

    }
}

@Composable
fun AssistantMenuMessage(
    modifier: Modifier = Modifier,
    menu: Menu,
    onItemClick: (Item?) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
            .fillMaxWidth(.8f)
            .wrapContentHeight()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_place_holder),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Top)
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp))
                .size(
                    GenericAssistantConstants.Medium.defaultProfileSize()
                )
        )
        Column {
            Column(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                    )
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
            ) {
                Text(
                    text = menu.menuData?.menuCategory ?: "unable to display your menu",
                    fontSize = GenericAssistantConstants.Medium.defaultTextSize(),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = GenericAssistantConstants.Medium.defaultLineHeight()
                )
            }
            Column(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                    )
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .padding(
                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                    )
            ) {
                Text(
                    text = "\uD83C\uDF1F Tap on the item to unveil a treasure trove of customization options and add it to your order. Let's sprinkle some magic onto your dish together! ✨",
                    fontSize = GenericAssistantConstants.Medium.defaultTextSize(),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = GenericAssistantConstants.Medium.defaultLineHeight(),
                    modifier = Modifier.alpha(.8f)
                )
            }
            menu.menuData?.items?.forEachIndexed { index, menuItem ->
                menuItem?.let { item ->
                    item.itemName?.let { itemName ->
                        if (itemName.isNotEmpty()) {
                            val cardColor = if (item.itemOfTheDay != null) {
                                if (item.itemOfTheDay) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            }
                            val contentColor = if (item.itemOfTheDay != null) {
                                if (item.itemOfTheDay) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                            val isMostOrdered = item.itemOfTheDay ?: false
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                        bottom = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                                    )
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .background(
                                        cardColor
                                    )
                                    .clickable {
                                        onItemClick(menu.menuData.items[index])
                                    }
                                    .padding(
                                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
                                    )
                            ) {
                                Row {
                                    Text(
                                        text = (item.itemNumber.toString().plus(". ")),
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = contentColor,
                                        fontSize = GenericAssistantConstants.Medium.defaultTextSize(),
                                        lineHeight = GenericAssistantConstants.Medium.defaultLineHeight()
                                    )
                                    Text(
                                        text = itemName,
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = contentColor,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        fontSize = GenericAssistantConstants.Medium.defaultTextSize(),
                                        lineHeight = GenericAssistantConstants.Medium.defaultLineHeight()
                                    )
                                }
                                Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)))
                                if (item.itemDescription != null) {
                                    if (item.itemDescription.isNotEmpty()) {
                                        Text(
                                            text = item.itemDescription,
                                            fontFamily = poppinsFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            color = contentColor,
                                            fontSize = GenericAssistantConstants.Medium.defaultOrderItemSize(),
                                            lineHeight = GenericAssistantConstants.Medium.defaultOrderItemHeight(),
                                            modifier = Modifier
                                                .alpha(.8f)
                                                .padding(end = 20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)))
                                if (item.amount != null) {
                                    if (item.amount != 0.0) {
                                        Row(
                                            modifier = Modifier,
                                            horizontalArrangement = Arrangement.spacedBy(
                                                dimensionResource(
                                                    id = com.intuit.sdp.R.dimen._3sdp
                                                )
                                            )
                                        ) {
                                            Text(
                                                text = "$ ${item.amount}",
                                                fontFamily = poppinsFontFamily,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (contentColor == MaterialTheme.colorScheme.onSurface) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.error,
                                                fontSize = GenericAssistantConstants.Medium.defaultLineHeight()
                                            )
                                        }
                                    }
                                }
                                if ((item.spiceLevel != null) or isMostOrdered) {
                                    val spiceLevel = when (item.spiceLevel) {
                                        1 -> "Mild"
                                        2 -> "Medium"
                                        3 -> "Spicy"
                                        else -> ""
                                    }
                                    val spicyText = if (item.spiceLevel == 3) "" else "spice"
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)))
                                        if (spiceLevel.isNotEmpty()) {
                                            Text(
                                                text = spiceLevel.plus(" $spicyText"),
                                                modifier = Modifier
                                                    .clip(MaterialTheme.shapes.extraLarge)
                                                    .background(MaterialTheme.colorScheme.error)
                                                    .padding(
                                                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                                                    ),
                                                fontFamily = poppinsFontFamily,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onError,
                                                fontSize = GenericAssistantConstants.Medium.defaultOrderItemSize()
                                            )
                                        }
                                        if (isMostOrdered) {
                                            Text(
                                                text = "Item of the Day",
                                                modifier = Modifier
                                                    .padding(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                                                    .clip(MaterialTheme.shapes.extraLarge)
                                                    .background(MaterialTheme.colorScheme.primary)
                                                    .padding(
                                                        horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp),
                                                        vertical = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
                                                    ),
                                                fontFamily = poppinsFontFamily,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontSize = GenericAssistantConstants.Medium.defaultOrderItemSize()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumAssistantConversationMessagePreview() {
    InsightifyTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            items(DemoConversation.AssistantDemoConversation) {
                MediumAssistantConversationMessage(
                    assistantConversationModel = it,
                    onMenuTap = {

                    },
                    onMenuOrderConfirmation = {

                    },
                    onItemClick = {

                    }
                )
            }

        }
    }
}


