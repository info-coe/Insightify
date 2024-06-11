package com.infomerica.insightify.manager

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DateGenerationManager @Inject constructor() {

    private val currentDate = Date()
    private val formatter = SimpleDateFormat("dd - MMM", Locale.getDefault())

    fun getCurrentDate(): String {
        return formatter.format(currentDate)
    }
}