package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val name: String,
    val confidenceLevel: Int,
    val totalSessions: Int,
    val completedSessions: Int,
    val scheduledSessions: Int,
)