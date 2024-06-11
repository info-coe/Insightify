package com.infomerica.insightify.ui.composables.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.infomerica.insightify.R

@Composable
fun TestScreen() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._50sdp))
                    .background(Color.Black)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._90sdp))
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._120sdp))
                    .background(Color.Blue)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._160sdp))
                    .background(Color.DarkGray)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.intuit.sdp.R.dimen._160sdp))
                    .background(Color.DarkGray)
            )
        }
    }
}

@Preview(device = Devices.PIXEL)
@Preview(device = Devices.PIXEL_7)
@Composable
fun TestScreenPreview() {
    TestScreen()
}