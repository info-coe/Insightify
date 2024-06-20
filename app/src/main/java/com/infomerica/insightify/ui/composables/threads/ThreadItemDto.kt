package com.infomerica.insightify.ui.composables.threads

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class ThreadItemDto(
    @Exclude
    val threadName:String? = null,
    val lastSeen: Timestamp = Timestamp.now(),
    val currentlyActive : Boolean = false
)
