package io.github.drp08.studypal.routes

import io.ktor.resources.Resource

@Resource("/schedule")
data class Schedule(val name: String)