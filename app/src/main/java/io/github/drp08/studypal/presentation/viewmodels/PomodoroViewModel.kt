package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.drp08.studypal.domain.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class PomodoroViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    fun updateXP(){
        viewModelScope.launch{
            userRepository.updateXp()
        }
    }
}