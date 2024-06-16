package io.github.drp08.studypal.domain.scheduler

import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import io.github.drp08.studypal.domain.models.Session
import io.github.drp08.studypal.domain.models.Subject
import io.github.drp08.studypal.domain.models.Topic
import io.github.drp08.studypal.domain.models.User

interface Scheduler {
    fun schedule(
        subjects: List<SubjectEntity>,
        topics: List<TopicEntity>,
        fixedSessions: List<SessionEntity>,
        user: User
    ): List<SessionEntity>

    companion object {
        const val HOUR_IN_MILLIS = 60 * 60 * 1000L
    }
}