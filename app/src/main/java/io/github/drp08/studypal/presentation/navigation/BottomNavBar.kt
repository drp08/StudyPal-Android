package io.github.drp08.studypal.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
fun BottomNavBar(modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.currentOrThrow

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Blue)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavItem.values().forEach {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { navigator.push(it.screen) }
            ) {
                Image(imageVector = it.icon, contentDescription = null, modifier = Modifier)
                Text(text = it.label, color = Color.White)
            }
        }
    }
}