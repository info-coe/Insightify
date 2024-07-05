package com.infomericainc.insightify.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun formatDate(date: Date): String {
    return SimpleDateFormat("dd/MM - HH.MM 'hrs'", Locale.getDefault()).format(date)
}