package io.github.drp08.studypal.presentation.controllers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ViewController {
    var name by mutableStateOf("")
    var workingHoursStart by mutableStateOf("")
    var workingHoursEnd by mutableStateOf("")
    var hoursPerDay by mutableStateOf("")

    fun onNameChange(newValue: String) {
        name = newValue
    }

    fun onWorkingHoursStartChange(newValue: String) {
        workingHoursStart = newValue
    }

    fun onWorkingHoursEndChange(newValue: String) {
        workingHoursEnd = newValue
    }

    fun onHoursPerDayChange(newValue: String) {
        hoursPerDay = newValue
    }
}