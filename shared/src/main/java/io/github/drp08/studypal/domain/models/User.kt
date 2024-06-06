package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val name: String,
    val startWorkingHours: Long, // Epoch millis
    val endWorkingHours: Long, // Epoch millis
    val maxStudyingHours: Int
)