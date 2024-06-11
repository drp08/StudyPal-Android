package io.github.drp08.studypal.routes

import io.github.drp08.studypal.database.addFriend
import io.github.drp08.studypal.database.addUser
import io.github.drp08.studypal.database.deleteFriend
import io.github.drp08.studypal.database.getFriends
import io.github.drp08.studypal.database.getUser
import io.github.drp08.studypal.database.updateXp
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.databaseApiRoutes() {
    route("/database") {
        userDbRoutes()
        friendsDbRoutes()
    }
}

fun Route.userDbRoutes() {
    route("/users") {
        get("/{name}") {
            val name = call.parameters["name"]!!
            getUser(name)?.let {
                call.respond(it)
            } ?: call.respond(HttpStatusCode.NotFound, "$name user not found.")
        }

        post {
            val name = call.receive<String>()
            val success = addUser(name)

            call.respond(
                if (success) HttpStatusCode.OK else HttpStatusCode.InternalServerError
            )
        }

        put {
            val body = call.receive<String>()
            val (name, xp) = body.split(" ").run { this[0] to this[1].toInt() }
            updateXp(name, xp)
        }
    }
}

fun Route.friendsDbRoutes() {
    route("/friends") {
        get("/{name}") {
            val name = call.parameters["name"]!!
            call.respond(getFriends(name))
        }

        post {
            val body = call.receive<String>()
            val (name, friend) = body.split(" ").run { this[0] to this[1] }

            getUser(name) ?:
                return@post call.respond(HttpStatusCode.NotFound, "$name user not found.")
            getUser(friend) ?:
                return@post call.respond(HttpStatusCode.NotFound, "$friend user not found.")

            val success = addFriend(name, friend)

            call.respond(
                if (success) HttpStatusCode.OK else HttpStatusCode.InternalServerError
            )
        }

        delete {
            val body = call.receive<String>()
            val (name, friend) = body.split(" ").run { this[0] to this[1] }

            getUser(name) ?:
                return@delete call.respond(HttpStatusCode.NotFound, "$name user not found.")
            getUser(friend) ?:
                return@delete call.respond(HttpStatusCode.NotFound, "$friend user not found.")

            val success = deleteFriend(name, friend)

            call.respond(
                if (success) HttpStatusCode.OK else HttpStatusCode.InternalServerError
            )
        }
    }
}