package io.github.drp08.studypal.data

import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.routes.Schedule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get

class SchedulingRepositoryImpl(
    private val client: HttpClient
) : SchedulingRepository {
    override suspend fun getScheduleForSubject(subject: String): Session {
        val response = client.get(Schedule(name = subject))
        return response.body<Session>()
    }
}