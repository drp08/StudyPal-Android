package io.github.drp08.studypal.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val name: String,
    val startWorkingHours: Int, // Second of the day?
    val endWorkingHours: Int, // Second of the day?
    val maxStudyHours: Int
)