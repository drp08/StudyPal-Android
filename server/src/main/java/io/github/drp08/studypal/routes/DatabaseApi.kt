package io.github.drp08.studypal.routes

import io.github.drp08.studypal.data.addUser
import io.github.drp08.studypal.data.getUser
import io.github.drp08.studypal.data.updateXp
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.databaseApiRoutes() {
    route("/users") {
        get("/{name}") {
            val name = call.parameters["name"]!!
            call.respond(getUser(name))
        }

        post {
            val name = call.receive<String>()
            val success = addUser(name)

            call.respond(if (success) HttpStatusCode.OK else HttpStatusCode.InternalServerError)
        }

        put {
            val body = call.receive<String>()
            val (name, xp) = body.split(" ").run { this[0] to this[1].toInt() }
            updateXp(name, xp)
        }
    }
}
