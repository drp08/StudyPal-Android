package io.github.drp08.studypal.routes

import io.github.drp08.studypal.domain.models.PostBody
import io.github.drp08.studypal.scheduler.RandomiseScheduler
import io.github.drp08.studypal.scheduler.Scheduler
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.scheduleRouting() {
    post("/schedule") {
        val scheduler: Scheduler = RandomiseScheduler()
        val str = call.receive<String>()
        println(str)
        val body = Json.decodeFromString<PostBody>(str)
        println(body)
        val newSessions = scheduler.schedule(body.subjects.toList(), body.topics.toList(), body.sessions.toList(), body.user)
        call.respond(Json.encodeToString(newSessions))
    }
}
