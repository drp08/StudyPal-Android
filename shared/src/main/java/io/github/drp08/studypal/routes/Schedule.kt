package io.github.drp08.studypal.routes

import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic
import io.github.drp08.studypal.domain.models.User
import io.ktor.resources.Resource

@Resource("/schedule")
data class Schedule(
    val subjects: List<Subject>,
    val topics: List<Topic>,
    val sessions: List<Session>,
    val user: User
)