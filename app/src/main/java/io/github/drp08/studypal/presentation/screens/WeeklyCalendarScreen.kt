package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.R
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import io.github.drp08.studypal.presentation.viewmodels.WeeklyViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object WeeklyCalendarScreen : Screen {
    private fun readResolve(): Any = WeeklyCalendarScreen

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var currentView by remember { mutableStateOf(CalendarView.WEEKLY) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CalendarViewSwitcher(
                currentView = currentView,
                onViewChange = { newView -> currentView = newView }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (currentView) {
                CalendarView.DAILY -> DailyCalendarScreen.Content()
                CalendarView.WEEKLY -> WeeklyView()
                CalendarView.MONTHLY -> MonthlyCalendarScreen.Content()
            }
        }
    }

    @Composable
    fun WeeklyView() {
        val viewModel = WeeklyViewModel()
        val currentDate by viewModel.currentDate.collectAsState()
        val currentWeek = remember { mutableStateOf(currentDate) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            WeekHeader(
                currentWeek = currentWeek.value,
                onPreviousWeek = { currentWeek.value = currentWeek.value.minus(7, DateTimeUnit.DAY) },
                onNextWeek = { currentWeek.value = currentWeek.value.plus(7, DateTimeUnit.DAY) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            WeekGrid(currentWeek.value, viewModel)
        }
    }


    @Composable
    fun WeekHeader(currentWeek: LocalDate, onPreviousWeek: () -> Unit, onNextWeek: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPreviousWeek) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Previous Week"
                )
            }
            Text(
                text = "${currentWeek.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentWeek.year}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onNextWeek) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Next Week"
                )
            }
        }
    }

    @Composable
    fun WeekGrid(currentWeek: LocalDate, viewModel: WeeklyViewModel) {
        val startOfWeek = currentWeek.atStartOfWeek()
        val daysOfWeek = (0..6).map { startOfWeek.plus(it, DateTimeUnit.DAY) }
        val currentDay = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                daysOfWeek.forEach { date ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = date.dayOfWeek.name.take(3),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                daysOfWeek.forEach { date ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val activities = viewModel.getActivitiesForDay(date.dayOfWeek, currentWeek)
                        activities.forEach { activity ->
                            Box(
                                modifier = Modifier
                                    .background(Color.LightGray)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = activity.title,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    Text(
                                        text = "${activity.startTime} - ${activity.endTime}",
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    fun LocalDate.plusDays(days: Int): LocalDate {
        return this.plus(days, DateTimeUnit.DAY)
    }
    private fun LocalDate.atStartOfWeek(): LocalDate {
        val dayOfWeek = this.dayOfWeek.ordinal
        return this.minus(dayOfWeek, DateTimeUnit.DAY)
    }
}