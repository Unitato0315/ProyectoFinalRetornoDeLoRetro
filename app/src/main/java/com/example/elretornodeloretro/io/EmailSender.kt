package com.example.elretornodeloretro.io

import android.util.Log
import com.example.elretornodeloretro.model.Constantes.EMAIL
import com.example.elretornodeloretro.model.Constantes.PASSWORD_APP
import com.example.elretornodeloretro.model.Constantes.SMTP_HOST
import com.example.elretornodeloretro.model.Constantes.SMTP_PORT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class EmailSender {
    companion object {
        suspend fun sendEmail(to: String, subject: String, bodyText: String) {
            Log.e("JVVM", "CONTROL")
            withContext(Dispatchers.IO) {
                val props = System.getProperties()
                props["mail.smtp.host"] = SMTP_HOST
                props["mail.smtp.port"] = SMTP_PORT
                props["mail.smtp.auth"] = "true"
                props["mail.smtp.starttls.enable"] = "true"

                val session = Session.getInstance(props, object : javax.mail.Authenticator() {
                    override fun getPasswordAuthentication() = javax.mail.PasswordAuthentication(EMAIL, PASSWORD_APP)
                })

                try {
                    val message = MimeMessage(session).apply {
                        setFrom(InternetAddress(EMAIL))
                        setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                        this.subject = subject
                        setText(bodyText)
                    }

                    Transport.send(message)
                    Log.e("JVVM", "CONTROL")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("JVVM", "CONTROL ERROR")
                }
            }
        }
    }
}