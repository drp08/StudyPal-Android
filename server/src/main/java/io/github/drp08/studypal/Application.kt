package io.github.drp08.studypal

import io.github.drp08.studypal.routes.databaseApiRoutes
import io.github.drp08.studypal.routes.scheduleRouting
import io.github.drp08.studypal.utils.HttpConstants
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(
        Netty,
        port = HttpConstants.PORT,
        host = HttpConstants.HOSTNAME,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureResources()
    configureRouting()
    configureDatabase()
}

fun Application.configureDatabase() {
    Database.connect(
        url = "jdbc:sqlite::resource:data.db",
        driver = "org.sqlite.JDBC"
    )
}

private fun Application.configureResources() {
    install(Resources)
}

fun Application.configureRouting() {
    routing {
        scheduleRouting()
        databaseApiRoutes()

        this.get("/") {
            call.respond("Hello World!")
        }
    }
}


fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
