package ru.gozerov

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Database.Companion
import ru.gozerov.features.goods.configureGoodsRouting
import ru.gozerov.features.login.configureLoginRouting
import ru.gozerov.features.register.configureRegisterRouting
import ru.gozerov.plugins.configureRouting
import ru.gozerov.plugins.configureSerialization

fun main() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5433/ebay",
        user = "postgres",
        driver = "org.postgresql.Driver",
        password = "StrongPssw1"
    )

    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureLoginRouting()
    configureRegisterRouting()
    configureGoodsRouting()
}
