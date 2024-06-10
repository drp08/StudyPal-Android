package io.github.drp08.studypal.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.MainActivity

object ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val sharedPrefs = MainActivity.prefs
        sharedPrefs.getString("name","Default")?.let { Text(text = "Welcome $it") }

//        Text(text = "Profile Screen")
    }
}