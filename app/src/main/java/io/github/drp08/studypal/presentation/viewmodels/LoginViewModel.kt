package io.github.drp08.studypal.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.presentation.navigation.HomeNavigator
import io.github.drp08.studypal.presentation.screens.HomeScreen
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    companion object {
        private const val TAG = "LoginViewModel"
    }

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun onEmailChange(newName: String) {
        email = newName
    }

    fun onPasswordChange(newName: String) {
        password = newName
    }

    fun onLogin(navigator: Navigator) {
        viewModelScope.launch {
            try {
                userRepository.loginUser(email, password)
                val user = userRepository.getUser()
                navigator.replace(HomeNavigator(startScreen = HomeScreen, user = user))
            } catch (e: Exception) {
                Log.e(TAG, "onRegister: ${e.message}", e)
            }
        }
    }
}