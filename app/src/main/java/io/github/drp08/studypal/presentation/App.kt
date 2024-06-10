package io.github.drp08.studypal.presentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import io.github.drp08.studypal.presentation.screens.LoadingScreen
import io.github.drp08.studypal.presentation.theme.StudyPalAndroidTheme

@Composable
fun App() {
    StudyPalAndroidTheme(darkTheme = false) {
        Navigator(LoadingScreen)
    }
}
