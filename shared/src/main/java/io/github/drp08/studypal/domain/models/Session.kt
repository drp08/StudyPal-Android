package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val sessionId: Int,
    val subject: Subject,
    val topic: Topic,
    val startTime: Int, // Second of the day
    val endTime: Int, // Second of the day
    val totalSessions: Int
)