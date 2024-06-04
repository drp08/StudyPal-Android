package io.github.drp08.studypal.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object CalendarScreen : Screen {
    @Composable
    override fun Content() {
        Text(text = "Calendar Screen")
    }
}