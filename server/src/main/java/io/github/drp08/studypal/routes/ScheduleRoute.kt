package io.github.drp08.studypal.routes

import io.github.drp08.studypal.database.Database
import io.github.drp08.studypal.domain.models.Session
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.scheduleRouting() {
    get<Schedule> {
        val oneHour = 60 * 60

        val session = Session(
            sessionId = 1,
            topic = it.name,
            startTime = 10000,
            endTime = 16000,
            totalSessions = 6
        )

        Database.startTime += oneHour
        call.respond(session)
    }
}
