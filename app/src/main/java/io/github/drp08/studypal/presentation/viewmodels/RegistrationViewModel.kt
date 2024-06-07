package io.github.drp08.studypal.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import io.github.drp08.studypal.db.daos.UserDao
import io.github.drp08.studypal.domain.entities.UserEntity
import io.github.drp08.studypal.presentation.screens.HomeScreen
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val userDao: UserDao
) : ViewModel() {
    var name by mutableStateOf("")
    var workingHoursStart by mutableStateOf(7 * 60 * 60 * 1000L)
    var workingHoursEnd by mutableStateOf(16 * 60 * 60 * 1000L)
    var hoursPerDay by mutableStateOf(6)

    fun onNameChange(newValue: String) {
        name = newValue
    }

    fun onWorkingHoursStartChange(newValue: Long) {
        workingHoursStart = newValue - 1 * 60 * 60 * 1000
    }

    fun onWorkingHoursEndChange(newValue: Long) {
        workingHoursEnd = newValue - 1 * 60 * 60 * 1000
    }

    fun onHoursPerDayChange(newValue: Int) {
        hoursPerDay = newValue
    }

    fun onRegister(navigator: Navigator) {
        if (name.isBlank())
            return

        viewModelScope.launch {
            userDao.upsertUser(UserEntity(
                name = this@RegistrationViewModel.name,
                startWorkingHours = workingHoursStart,
                endWorkingHours = workingHoursEnd,
                maxStudyingHours = hoursPerDay
            ))
            navigator.push(HomeScreen)
        }
    }
}