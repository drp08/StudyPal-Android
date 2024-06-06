package io.github.drp08.studypal.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
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