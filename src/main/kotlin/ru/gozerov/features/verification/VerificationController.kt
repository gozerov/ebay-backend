package ru.gozerov.features.verification

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.gozerov.database.verification.VerificationTokens

class VerificationController(
    private val call: ApplicationCall
) {

    suspend fun checkVerificationCode(verificationCodeRequest: VerificationCodeRequest) {
        val response = VerificationTokens.checkIsCodeValidByToken(verificationCodeRequest.token, verificationCodeRequest.code)
        response?.let {
            call.respond(response)
        } ?: call.respond(HttpStatusCode.BadRequest, "Code isn`t valid")
    }

}