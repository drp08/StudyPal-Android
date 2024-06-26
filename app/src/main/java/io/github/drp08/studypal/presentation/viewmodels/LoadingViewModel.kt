package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.domain.models.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun getUser(): Result<User> {
        return runBlocking {
            userRepository.verifyAndGetUser()
        }
    }
}
