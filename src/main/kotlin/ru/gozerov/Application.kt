package ru.gozerov

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import ru.gozerov.database.sales.configureSalesRouting
import ru.gozerov.features.confirm_password.configurePasswordsRouting
import ru.gozerov.features.email.configureEmailRouting
import ru.gozerov.features.goods.configureGoodsRouting
import ru.gozerov.features.login.configureLoginRouting
import ru.gozerov.features.register.configureRegisterRouting
import ru.gozerov.features.reviews.configureReviewRouting
import ru.gozerov.features.verification.configureVerificationRouting
import ru.gozerov.plugins.configureSerialization

fun main() {
    val url = "jdbc:postgresql://host.docker.internal:5433/ebay"
    val user = "postgres"
    val password = "StrongPssw1"
    val driverClassName = "org.postgresql.Driver"
    Database.connect(
        url = url,
        user = user,
        driver = driverClassName,
        password = password
    )
    embeddedServer(Netty, port = 8090, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureLoginRouting()
    configureEmailRouting()
    configureVerificationRouting()
    configurePasswordsRouting()
    configureRegisterRouting()
    configureGoodsRouting()
    configureReviewRouting()
    configureSalesRouting()
}
