package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val sessionId: Int,
    val topic: String,
    val startTime: Long, // Second of the day
    val endTime: Long, // Second of the day
    val totalSessions: Int
)