package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val name: String,
    val startWorkingHours: Long, // Epoch millis from the start of the working day
    val endWorkingHours: Long, // Epoch millis from the start of the working day
    var maxStudyingHours: Int
)