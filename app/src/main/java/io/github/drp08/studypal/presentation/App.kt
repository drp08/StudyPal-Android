package io.github.drp08.studypal.presentation

import android.provider.Settings.Global.getString
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import io.github.drp08.studypal.MainActivity
import io.github.drp08.studypal.R
import io.github.drp08.studypal.presentation.navigation.BottomNavBar
import io.github.drp08.studypal.presentation.navigation.BottomNavItem
import io.github.drp08.studypal.presentation.screens.HomeScreen
import io.github.drp08.studypal.presentation.screens.RegistrationScreen
import io.github.drp08.studypal.presentation.theme.StudyPalAndroidTheme

@Composable
fun App() {
    var startScreen: Screen = HomeScreen
    if (MainActivity.firstTimeOpening){
        startScreen = RegistrationScreen
    }
    StudyPalAndroidTheme(darkTheme = false) {
        Navigator(screen = startScreen) { navigator ->
            if (navigator.lastItem is HomeScreen) {
                Navigator(BottomNavItem.Home.screen) { _ ->
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = { BottomNavBar() }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            CurrentScreen()
                        }
                    }
                }
            } else {
                CurrentScreen()
            }
        }
    }
}
