package com.infomericainc.insightify.manager

import java.security.SecureRandom
import javax.inject.Inject

class RandomIdManager @Inject constructor(){
    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+"
        val secureRandom = SecureRandom()
        val randomStringBuilder = StringBuilder()

        repeat(length) {
            val randomIndex = secureRandom.nextInt(charset.length)
            randomStringBuilder.append(charset[randomIndex])
        }

        return randomStringBuilder.toString()
    }
}