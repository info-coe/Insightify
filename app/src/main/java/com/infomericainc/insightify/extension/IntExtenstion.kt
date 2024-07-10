package com.infomericainc.insightify.extension

fun Double.toCurrency(): Long {
    return (this * 100).toLong()
}