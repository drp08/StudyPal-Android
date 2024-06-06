package io.github.drp08.studypal.scheduler

import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic
import io.github.drp08.studypal.domain.models.User

interface Scheduler {
    fun schedule(
        subjects: List<Subject>,
        topics: List<Topic>,
        fixedSessions: List<Session>,
        user: User
    ): List<Session>
}