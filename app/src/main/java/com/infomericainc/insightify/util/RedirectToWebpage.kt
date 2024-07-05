package com.infomericainc.insightify.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.redirectToWebsite(uri: String) {
    Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(uri)
        this@redirectToWebsite.startActivity(this)
    }
}