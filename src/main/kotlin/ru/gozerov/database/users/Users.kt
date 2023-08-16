package ru.gozerov.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users: Table("users") {
    private val login = Users.varchar("login", 50)
    private val password = Users.varchar("password", 25)
    private val username = Users.varchar("username", 25)
    private val email = Users.varchar("email", 50)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[username] = userDTO.username
                it[email] = userDTO.email ?: ""
            }
        }
    }

    fun getUser(login: String): UserDTO? = transaction{
        val user = Users.select { Users.login.eq(login) }.singleOrNull()
        return@transaction if (user == null) null else
            UserDTO(
                login = user[Users.login],
                password = user[password],
                username = user[username],
                email = user[email]
            )
    }
}