package io.github.drp08.studypal

import io.github.drp08.studypal.routes.scheduleRouting
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

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "192.168.1.196",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureResources()
    configureRouting()
}

private fun Application.configureResources() {
    install(Resources)
}

fun Application.configureRouting() {
    routing {
        scheduleRouting()

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
