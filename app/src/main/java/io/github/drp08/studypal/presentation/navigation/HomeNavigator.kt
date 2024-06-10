package io.github.drp08.studypal.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import io.github.drp08.studypal.db.session.UserSession.Companion.ActiveUser
import io.github.drp08.studypal.domain.models.User

data class HomeNavigator(
    private val startScreen: Screen,
    private val user: User
) : Screen {

    @Composable
    override fun Content() {
        Navigator(screen = startScreen) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = { BottomNavBar() }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    CompositionLocalProvider(ActiveUser provides user) {
                        CurrentScreen()
                    }
                }
            }
        }
    }
}