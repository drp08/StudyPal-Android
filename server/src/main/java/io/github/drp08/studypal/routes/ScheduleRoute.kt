package io.github.drp08.studypal.routes

import io.github.drp08.studypal.domain.models.PostBody
import io.github.drp08.studypal.scheduler.RandomiseScheduler
import io.github.drp08.studypal.scheduler.Scheduler
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.scheduleRouting() {
    post<Schedule> {
        val scheduler: Scheduler = RandomiseScheduler()
        val body = call.receive<PostBody>()
        val newSessions = scheduler.schedule(body.subjects, body.topics, body.sessions, body.user)
        call.respond(newSessions)
    }
}
