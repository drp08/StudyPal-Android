package io.github.drp08.studypal.domain

import io.github.drp08.studypal.domain.models.Session

interface SchedulingRepository {
    suspend fun getScheduleForSubject(subject: String): Session
}