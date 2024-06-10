package io.github.drp08.studypal.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
fun BottomNavBar(modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.currentOrThrow

    NavigationBar(
        modifier = modifier,
        containerColor = Color.Blue,
        contentColor = Color.White
    ) {
        BottomNavItem.values().forEach {
            NavigationBarItem(
                selected = navigator.lastItem == it.screen,
                onClick = { navigator.push(it.screen) },
                icon = { Icon(imageVector = it.icon, contentDescription = null) },
                label = { Text(text = it.label) },
                colors = NavigationBarItemColors(
                    selectedIndicatorColor = Color.White,
                    selectedIconColor = Color.Black,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    disabledIconColor = Color.Gray,
                    disabledTextColor = Color.Gray
                )
            )
        }
    }
}