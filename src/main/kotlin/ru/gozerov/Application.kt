package ru.gozerov

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.gozerov.database.sales.configureSalesRouting
import ru.gozerov.features.confirm_password.configurePasswordsRouting
import ru.gozerov.features.email.configureEmailRouting
import ru.gozerov.features.goods.configureGoodsRouting
import ru.gozerov.features.login.configureLoginRouting
import ru.gozerov.features.register.configureRegisterRouting
import ru.gozerov.features.reviews.configureReviewRouting
import ru.gozerov.features.verification.configureVerificationRouting
import ru.gozerov.plugins.configureSerialization

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabase()
    configureLoginRouting()
    configureEmailRouting()
    configureVerificationRouting()
    configurePasswordsRouting()
    configureRegisterRouting()
    configureGoodsRouting()
    configureReviewRouting()
    configureSalesRouting()
}

fun Application.configureDatabase() {
    DatabaseFactory.init(this.environment.config)
}

object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("storage.driverClassName").getString()
        val url = config.property("storage.jdbcURL").getString()
        val user = config.property("storage.user").getString()
        val password = config.property("storage.password").getString()
        val flyway = Flyway.configure().dataSource(url, user, password).load()
        flyway.migrate()

        Database.connect(
            url = url,
            user = user,
            driver = driverClassName,
            password = password
        )
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

}
