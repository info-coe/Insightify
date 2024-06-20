package com.infomerica.insightify.ui.components.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.MediumThemedPreviewProvider
import com.intuit.sdp.R

@DrawableRes
private val icons = mutableListOf(
    com.infomerica.insightify.R.drawable.ic_google,
    com.infomerica.insightify.R.drawable.apple,
    com.infomerica.insightify.R.drawable.github,
)
private val labels = mutableListOf(
    "Sign in with Google",
    "Sign in with Apple",
    "Sign in with Github",
    "Sign in with Facebook"
)

@Composable
fun CompactSignInOptions(
    onGoogleSignIn: () -> Unit,
    onAppleSignIn: () -> Unit,
    onGithubSignIn: () -> Unit
) {
    repeat(3) {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                .padding(top = if (it != 0) dimensionResource(id = com.intuit.sdp.R.dimen._15sdp) else 0.dp)
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._48sdp))
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    when (it) {
                        0 -> onGoogleSignIn()
                        1 -> onAppleSignIn()
                        2 -> onGithubSignIn()
                        else -> {

                        }
                    }
                }
        ) {
            val holder = createRef()
            Row(
                modifier = Modifier.constrainAs(holder) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = icons[it]
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen._20sdp)),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = labels[it],
                    modifier = Modifier
                        .wrapContentSize(),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = dimensionResource(id = R.dimen._14sdp).value.sp,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun MediumSignInOptions(
    onGoogleSignIn: () -> Unit,
    onAppleSignIn: () -> Unit,
    onGithubSignIn: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen._10sdp), alignment = Alignment.CenterHorizontally),
    ) {
        IconButton(
            onClick = { onGoogleSignIn() },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(dimensionResource(id = R.dimen._5sdp))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(com.infomerica.insightify.R.drawable.ic_google),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen._18sdp)),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = { onGithubSignIn() },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(dimensionResource(id = R.dimen._5sdp))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(com.infomerica.insightify.R.drawable.github),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen._18sdp)),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = { onAppleSignIn() },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(dimensionResource(id = R.dimen._5sdp))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(com.infomerica.insightify.R.drawable.apple),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen._18sdp)),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@MediumThemedPreviewProvider
@Composable
private fun MediumSignInOptionsPreview() {
    MediumSignInOptions(onGoogleSignIn = { /*TODO*/ }, onAppleSignIn = { /*TODO*/ }) {

    }
}