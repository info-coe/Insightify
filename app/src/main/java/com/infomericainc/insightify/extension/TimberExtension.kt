package com.infomericainc.insightify.extension

import timber.log.Timber

fun logInfo(tag: String, message: String) {
    Timber
        .tag(tag)
        .i(message)
}

fun logError(tag: String, message: String?) {
    Timber
        .tag(tag)
        .e(message)
}