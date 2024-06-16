package io.github.drp08.studypal.domain.models.v2

data class Friend(
    val friend1: String,
    val friend2: String
) {
    constructor(): this("", "")
}