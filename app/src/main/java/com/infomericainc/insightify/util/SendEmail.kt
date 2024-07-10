package com.infomericainc.insightify.util

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties

fun sendEmail(to: String, subject: String, messageBody: String, from: String, password: String) {
    val properties = Properties().apply {
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
    }

    val session = Session.getInstance(properties, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(from, password)
        }
    })

    try {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(from))
            addRecipient(Message.RecipientType.TO, InternetAddress(to))
            setSubject(subject)
            description = "Insightify welcome email"
            setText(messageBody)
        }
        Transport.send(message)
        println("Email sent successfully")
    } catch (e: MessagingException) {
        e.printStackTrace()
    }
}
