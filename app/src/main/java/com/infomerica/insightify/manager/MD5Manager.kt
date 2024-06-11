package com.infomerica.insightify.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import javax.inject.Inject

class MD5Manager @Inject constructor() {

    private val md = MessageDigest.getInstance("MD5")

    suspend fun computeMD5Hash(data: List<String>): String {
        return withContext(Dispatchers.IO) {
            val combinedData = data.joinToString(",")
            val hashBytes = md.digest(combinedData.toByteArray())
            return@withContext hashBytes.joinToString("") { "%02x".format(it) }
        }
    }
}