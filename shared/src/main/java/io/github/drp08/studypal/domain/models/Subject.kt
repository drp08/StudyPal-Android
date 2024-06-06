package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val name: String,
    val confidenceLevel: Float, // from 0f to 1f
    val totalSessions: Int,
    val completedSessions: Int,
    val scheduledSessions: Int,
    val hoursPerWeek: Int,
    val examEpoch: Long?
)