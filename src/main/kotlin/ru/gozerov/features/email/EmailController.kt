package ru.gozerov.features.email

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import ru.gozerov.database.users.Users
import ru.gozerov.database.verification.VerificationTokenDTO
import ru.gozerov.database.verification.VerificationTokens
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class EmailController(
    private val call: ApplicationCall
) {

    suspend fun sendVerificationCode(emailAddress: String) {
        if (checkIsEmailInSystem(emailAddress)) {
            val code = Random.nextInt(1000..9999)
            val email = SimpleEmail()
            email.hostName = "smtp.googlemail.com"
            email.setSmtpPort(465)
            email.setAuthenticator(DefaultAuthenticator("ebaydroidbackend@gmail.com", "gmciigcubwqslayq"))
            email.isSSLOnConnect = true
            email.setFrom("ebaydroidbackend@gmail.com")
            email.subject = "Your code - $code"
            email.setMsg("Your code: $code. You can use that code to reset your password in Ebay Android.")
            email.addTo(emailAddress)
            email.send()
            val token = UUID.randomUUID().toString()
            VerificationTokens.insert(VerificationTokenDTO(token, code, emailAddress))
            println(token)
            call.respond(HttpStatusCode.OK, ResetEmailResponse(token))
        } else
            call.respond(HttpStatusCode.NotFound, "User not found")
    }

    suspend fun startRegistration(emailAddress: String) {
        if (!Users.checkUserInSystemByEmail(emailAddress)) {
            val code = Random.nextInt(1000..9999)
            val email = SimpleEmail()
            email.hostName = "smtp.googlemail.com"
            email.setSmtpPort(465)
            email.setAuthenticator(DefaultAuthenticator("ebaydroidbackend@gmail.com", "gmciigcubwqslayq"))
            email.isSSLOnConnect = true
            email.setFrom("ebaydroidbackend@gmail.com")
            email.subject = "Your code - $code"
            email.setMsg("Your code: $code. You can use that code to reset your password in Ebay Android.")
            email.addTo(emailAddress)
            email.send()
            val token = UUID.randomUUID().toString()
            VerificationTokens.insert(VerificationTokenDTO(token, code, emailAddress))
            println(token)
            call.respond(HttpStatusCode.OK, ResetEmailResponse(token))
        } else
            call.respond(HttpStatusCode.BadRequest, "Account already exist")
    }

}

fun checkIsEmailInSystem(email: String) : Boolean = Users.checkUserInSystemByEmail(email)
