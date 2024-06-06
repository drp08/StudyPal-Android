package io.github.drp08.studypal.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.drp08.studypal.presentation.screens.CalendarView

@Composable
fun CalendarViewSwitcher(currentView: CalendarView,
                         onViewChange: (CalendarView) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { onViewChange(CalendarView.DAILY) },
            enabled = currentView != CalendarView.DAILY
        ) {
            Text("Daily")
        }
        Button(
            onClick = { onViewChange(CalendarView.WEEKLY) },
            enabled = currentView != CalendarView.WEEKLY
        ) {
            Text("Weekly")
        }
        Button(
            onClick = { onViewChange(CalendarView.MONTHLY) },
            enabled = currentView != CalendarView.MONTHLY
        ) {
            Text("Monthly")
        }
    }

}