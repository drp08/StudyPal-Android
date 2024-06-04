package io.github.drp08.studypal.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import io.github.drp08.studypal.presentation.navigation.BottomNavBar
import io.github.drp08.studypal.presentation.navigation.BottomNavItem
import io.github.drp08.studypal.presentation.screens.BlankScreen
import io.github.drp08.studypal.presentation.theme.StudyPalAndroidTheme

@Composable
fun App() {
    StudyPalAndroidTheme(darkTheme = false) {
        Navigator(screen = BlankScreen) { navigator ->
            if (navigator.lastItem is BlankScreen) {
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
