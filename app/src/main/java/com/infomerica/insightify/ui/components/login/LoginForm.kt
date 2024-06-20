package com.infomerica.insightify.ui.components.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infomerica.insightify.ui.theme.poppinsFontFamily
import com.infomerica.insightify.util.CompactThemedPreviewProvider
import com.infomerica.insightify.util.MediumThemedPreviewProvider
import com.intuit.sdp.R.dimen
import com.intuit.ssp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CompactLoginForm(
    onLogin: () -> Unit
) {
    var loginError by remember {
        mutableStateOf("")
    }

    var emailTextFiledError by remember {
        mutableStateOf(false)
    }

    var emailTextFiledErrorMessage by remember {
        mutableStateOf("")
    }

    var passwordTextFiledError by remember {
        mutableStateOf(false)
    }

    var passwordTextFiledErrorMessage by remember {
        mutableStateOf("")
    }

    var showPassword by remember {
        mutableStateOf(false)
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp))
            .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Sign In with Email.",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = dimensionResource(id = R.dimen._16ssp).value.sp,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(.9f)
        )
        AnimatedVisibility(visible = emailTextFiledError) {
            Text(
                text = emailTextFiledErrorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontFamily = poppinsFontFamily
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(
                    text = "Email",
                    fontFamily = poppinsFontFamily,
                    fontSize = dimensionResource(id = R.dimen._13ssp).value.sp
                )
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp))
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._58sdp)),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = emailTextFiledError,
            singleLine = true,
            trailingIcon = {
                AnimatedVisibility(visible = email.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = dimensionResource(id = dimen._20sdp))
                            .size(dimensionResource(id = dimen._20sdp))
                            .clickable {
                                email = ""
                            },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            textStyle = TextStyle(
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = R.dimen._14ssp).value.sp
            )
        )

        AnimatedVisibility(visible = passwordTextFiledError) {
            Text(
                text = passwordTextFiledErrorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontFamily = poppinsFontFamily
            )
        }
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(
                    text = "Password",
                    fontFamily = poppinsFontFamily,
                    fontSize = dimensionResource(id = R.dimen._13ssp).value.sp
                )
            },
            trailingIcon = {
                AnimatedVisibility(visible = password.isNotEmpty()) {
                    Icon(
                        imageVector = if (showPassword) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = dimensionResource(id = dimen._20sdp))
                            .size(dimensionResource(id = dimen._20sdp))
                            .clickable {
                                showPassword = !showPassword
                            },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp))
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._58sdp)),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            textStyle = TextStyle(
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = R.dimen._14ssp).value.sp
            ),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordTextFiledError,
            singleLine = true
        )
        AnimatedVisibility(visible = loginError.isNotEmpty(), label = "") {
            Text(
                text = loginError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        LaunchedEffect(key1 = loginError) {
            delay(4000L)
            loginError = ""
        }
        //TODO : Fix the focus
        Button(
            onClick = {
                focusRequester.requestFocus()
                keyboardController?.hide()
                val (isEmailError, emailErrorMessage) = validateEmail(email)
                val (isPasswordError, passwordErrorMessage) = validatePassword(password)
                if (isEmailError) {
                    emailTextFiledErrorMessage = emailErrorMessage
                    emailTextFiledError = true
                    coroutineScope.launch {
                        delay(4000L)
                        emailTextFiledError = false
                        emailTextFiledErrorMessage = ""
                    }
                }
                if (isPasswordError) {
                    passwordTextFiledErrorMessage = passwordErrorMessage
                    passwordTextFiledError = true
                    coroutineScope.launch {
                        delay(4000L)
                        passwordTextFiledError = false
                        passwordTextFiledErrorMessage = ""
                    }
                }
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    if (validateUser(email, password)) {
                        onLogin()
                    } else {
                        loginError = "email or the password was incorrect."
                    }
                }
            },
            modifier = Modifier
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp))
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._36sdp))
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._48sdp))
                .focusRequester(focusRequester)
        ) {
            Text(
                text = "Login",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(id = R.dimen._15ssp).value.sp
            )
        }
    }
}

@Composable
fun MediumLoginForm(
    onLogin: () -> Unit
) {
    var loginError by remember {
        mutableStateOf("")
    }

    var emailTextFiledError by remember {
        mutableStateOf(false)
    }

    var emailTextFiledErrorMessage by remember {
        mutableStateOf("")
    }

    var passwordTextFiledError by remember {
        mutableStateOf(false)
    }

    var passwordTextFiledErrorMessage by remember {
        mutableStateOf("")
    }

    var showPassword by remember {
        mutableStateOf(false)
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
            .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Sign In with Email.",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = dimensionResource(id = R.dimen._10ssp).value.sp,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(.9f)
        )
        AnimatedVisibility(visible = emailTextFiledError) {
            Text(
                text = emailTextFiledErrorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontFamily = poppinsFontFamily
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(
                    text = "Email",
                    fontFamily = poppinsFontFamily,
                    fontSize = dimensionResource(id = R.dimen._8ssp).value.sp
                )
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(top = dimensionResource(id = dimen._8sdp))
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp)),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = emailTextFiledError,
            singleLine = true,
            trailingIcon = {
                AnimatedVisibility(visible = email.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = dimensionResource(id = dimen._20sdp))
                            .size(dimensionResource(id = dimen._20sdp))
                            .clickable {
                                email = ""
                            },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            textStyle = TextStyle(
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = R.dimen._12ssp).value.sp
            )
        )

        AnimatedVisibility(visible = passwordTextFiledError) {
            Text(
                text = passwordTextFiledErrorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontFamily = poppinsFontFamily
            )
        }
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(
                    text = "Password",
                    fontFamily = poppinsFontFamily,
                    fontSize = dimensionResource(id = R.dimen._8ssp).value.sp
                )
            },
            trailingIcon = {
                AnimatedVisibility(visible = password.isNotEmpty()) {
                    Icon(
                        imageVector = if (showPassword) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = dimensionResource(id = dimen._15sdp))
                            .size(dimensionResource(id = dimen._17sdp))
                            .clickable {
                                showPassword = !showPassword
                            },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp))
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp)),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            textStyle = TextStyle(
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = R.dimen._12ssp).value.sp
            ),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordTextFiledError,
            singleLine = true
        )
        AnimatedVisibility(visible = loginError.isNotEmpty(), label = "") {
            Text(
                text = loginError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        LaunchedEffect(key1 = loginError) {
            delay(4000L)
            loginError = ""
        }
        //TODO : Fix the focus
        Button(
            onClick = {
                focusRequester.requestFocus()
                keyboardController?.hide()
                val (isEmailError, emailErrorMessage) = validateEmail(email)
                val (isPasswordError, passwordErrorMessage) = validatePassword(password)
                if (isEmailError) {
                    emailTextFiledErrorMessage = emailErrorMessage
                    emailTextFiledError = true
                    coroutineScope.launch {
                        delay(4000L)
                        emailTextFiledError = false
                        emailTextFiledErrorMessage = ""
                    }
                }
                if (isPasswordError) {
                    passwordTextFiledErrorMessage = passwordErrorMessage
                    passwordTextFiledError = true
                    coroutineScope.launch {
                        delay(4000L)
                        passwordTextFiledError = false
                        passwordTextFiledErrorMessage = ""
                    }
                }
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    if (validateUser(email, password)) {
                        onLogin()
                    } else {
                        loginError = "email or the password was incorrect."
                    }
                }
            },
            modifier = Modifier
                .padding(top = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._36sdp))
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._35sdp))
                .focusRequester(focusRequester)
        ) {
            Text(
                text = "Login",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(id = R.dimen._10ssp).value.sp
            )
        }
    }
}


@CompactThemedPreviewProvider
@Composable
fun CompactLoginFormPreview() {
    Surface {
        CompactLoginForm {

        }
    }
}

@MediumThemedPreviewProvider
@Composable
fun MediumLoginFormPreview() {
    Surface {
        MediumLoginForm {

        }
    }
}


private fun validateUser(email: String, password: String): Boolean {
    val emails = mutableListOf(
        "sai@infomericainc.com",
        "krupal@infomericainc.com",
        "bhargav@infomericainc.com",
        "srikanth@infomericainc.com",
        "karthik@infomericainc.com",
    )
    return emails.contains(email) && password == "Info@123"
}

private fun validateEmail(email: String): Pair<Boolean, String> {
    return when {
        email.isEmpty() -> Pair(true, "email is empty.")
        email.contains("@infomericainc.com").not() -> Pair(
            true,
            "Unknown email address."
        )

        else -> Pair(false, "")
    }
}

private fun validatePassword(password: String): Pair<Boolean, String> {
    return when {
        password.isEmpty() -> Pair(true, "password is empty.")
        else -> Pair(false, "")
    }
}
