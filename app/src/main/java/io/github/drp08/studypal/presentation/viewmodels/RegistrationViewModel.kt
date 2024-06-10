package io.github.drp08.studypal.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.session.Session
import io.github.drp08.studypal.domain.models.User
import io.github.drp08.studypal.presentation.screens.HomeScreen
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val session: Session
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
            session.setUser(User(
                name,
                workingHoursStart,
                workingHoursEnd,
                hoursPerDay
            ))

            navigator.push(HomeScreen)
        }
    }
}