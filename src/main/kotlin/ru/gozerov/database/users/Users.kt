package ru.gozerov.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.gozerov.database.verification.VerificationTokens

object Users: Table("users") {
    private val id = Users.integer("id")
    private val password = Users.varchar("password", 25)
    private val username = Users.varchar("username", 25)
    private val email = Users.varchar("email", 50)
    private val avatar = Users.varchar("avatar", 100)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[password] = userDTO.password
                it[username] = userDTO.username
                it[email] = userDTO.email ?: ""
            }
        }
    }

    fun getUserById(id: Int): UserDTO? = transaction{
        val user = Users.select { Users.id.eq(this@Users.id) }.singleOrNull()
        return@transaction if (user == null) null else
            UserDTO(
                id = user[Users.id],
                password = user[password],
                username = user[username],
                email = user[email]
            )
    }
    fun getUserByEmail(email: String): UserDTO? = transaction{
        val user = Users.select { Users.email.eq(email) }.singleOrNull()
        return@transaction if (user == null) null else
            UserDTO(
                id = user[this@Users.id],
                password = user[password],
                username = user[username],
                email = user[this@Users.email]
            )
    }

    fun updateUserPassword(token: String, newPassword: String) = transaction {
        val userEmail = VerificationTokens.getEmailByToken(token)
        Users.update(where = {
            Users.email.eq(userEmail)
        }, body = {
            it[password] = newPassword
        })
    }

    fun checkUserInSystemByEmail(email: String) : Boolean = transaction {
        val user = Users.select { Users.email.eq(email) }.singleOrNull()
        return@transaction user != null
    }
}