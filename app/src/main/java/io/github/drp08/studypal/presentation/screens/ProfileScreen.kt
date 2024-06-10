package io.github.drp08.studypal.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.MainActivity
import io.github.drp08.studypal.presentation.viewmodels.ProfileViewModel

object ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<ProfileViewModel>()

        Text(text = "Profile Screen")
    }
}