package ru.gozerov.database.verification

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.gozerov.features.verification.VerificationCodeResponse

object VerificationTokens : Table(name = "verification_tokens") {

    private val token = VerificationTokens.varchar("token", 75)
    private val code = VerificationTokens.integer("code")
    private val email = VerificationTokens.varchar("email", 50)

    fun insert(verificationTokenDTO: VerificationTokenDTO) : String = transaction {
        VerificationTokens.insert {
            it[token] = verificationTokenDTO.token
            it[code] = verificationTokenDTO.code
            it[email] = verificationTokenDTO.email
        }
        return@transaction verificationTokenDTO.token
    }

    fun checkIsCodeValidByToken(token: String, code: Int): VerificationCodeResponse? = transaction {
        val verificationToken = VerificationTokens.select {
            (VerificationTokens.token.eq(token) and VerificationTokens.code.eq(code))
        }.singleOrNull()
        return@transaction verificationToken?.run {
            VerificationCodeResponse(
                token = verificationToken[this@VerificationTokens.token]
            )
        }
    }

    fun checkIsTokenValid(token: String) : Boolean = transaction {
        return@transaction VerificationTokens.select { VerificationTokens.token.eq(token) }.singleOrNull() != null
    }

    fun getEmailByToken(token: String): String = transaction {
        return@transaction VerificationTokens.select { VerificationTokens.token.eq(token) }.singleOrNull()?.get(this@VerificationTokens.email) ?: ""
    }

    fun deleteVerificationTokenByToken(token: String) = transaction {
        VerificationTokens.deleteWhere { VerificationTokens.token.eq(token) }
    }

}