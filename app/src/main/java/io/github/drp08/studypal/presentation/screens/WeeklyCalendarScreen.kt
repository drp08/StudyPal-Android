package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.presentation.viewmodels.WeeklyViewModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.util.date.WeekDay
import kotlinx.datetime.DayOfWeek

object WeeklyCalendarScreen : Screen {
    private fun readResolve(): Any = WeeklyCalendarScreen

    @Composable
    override fun Content() {
        val viewModel = WeeklyViewModel()
        val currentDate = viewModel.currentDate.value
        val month = currentDate.month
        val daysOfWeek = DayOfWeek.values()

        Column(modifier = Modifier.fillMaxSize()) {
            // Display month
            Text(
                text = month.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Display dates
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (day in 3..9) {
                    Text(
                        text = day.toString(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(48.dp)
                            .padding(vertical = 8.dp)
                    )
                }
            }

            // Display days of the week
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                daysOfWeek.forEach { dayOfWeek ->
                    Text(
                        text = dayOfWeek.toString().take(3),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            // Display activities for each day of the week
            for (dayOfWeek in daysOfWeek) {
                val activities = viewModel.getActivitiesForDay(dayOfWeek, currentDate)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 3..9) {
                        val activityText = activities.getOrNull(i - 3)?.title ?: ""
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(100.dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = activityText,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}