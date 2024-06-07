package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.R
import io.github.drp08.studypal.presentation.components.CalendarViewSwitcher
import io.github.drp08.studypal.presentation.viewmodels.MonthlyCalendarViewModel
import io.ktor.util.date.WeekDay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import network.chaintech.utils.now

object MonthlyCalendarScreen : Screen {

    @Composable
    override fun Content() {
        var currentView by remember { mutableStateOf(CalendarView.MONTHLY) }

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
                CalendarView.DAILY -> DailyCalendarScreen.DailyView()
                CalendarView.WEEKLY -> WeeklyCalendarScreen.WeeklyView()
                CalendarView.MONTHLY -> MonthlyView()
            }
        }
    }

    @Composable
    fun MonthlyView() {
        val viewModel = viewModel<MonthlyCalendarViewModel>()
        val currentDate by viewModel.currentDate.collectAsState()
        val currentMonth = remember { mutableStateOf(currentDate) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            CalendarHeader(
                currentMonth = currentMonth.value,
                onPreviousMonth = {
                    currentMonth.value = currentMonth.value.minus(1, DateTimeUnit.MONTH)
                },
                onNextMonth = {
                    currentMonth.value = currentMonth.value.plus(1, DateTimeUnit.MONTH)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CalendarGrid(currentMonth.value)
        }
    }

    @Composable
    fun CalendarHeader(
        currentMonth: LocalDate,
        onPreviousMonth: () -> Unit,
        onNextMonth: () -> Unit
    ) {
        val formattedMonthName = currentMonth.month.name.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Previous Month"
                )
            }
            Text(
                text = "$formattedMonthName ${currentMonth.year}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onNextMonth) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Next Month"
                )
            }
        }
    }

    @Composable
    fun CalendarGrid(currentMonth: LocalDate) {
        val daysInMonth = currentMonth.daysInMonth()
        val firstDayOfMonth = currentMonth.withDayOfMonth(1)
        val currentDay = LocalDate.now()

        val calendarGrid = mutableListOf<MutableList<LocalDate?>>()
        var week = mutableListOf<LocalDate?>()
        var dayOfWeekCounter = firstDayOfMonth.dayOfWeek.ordinal

        // Add empty slots for days before the first day of the month
        repeat(dayOfWeekCounter) {
            week.add(null)
        }

        for (i in 1..daysInMonth) {
            val day = currentMonth.withDayOfMonth(i)
            week.add(day)
            if (week.size == 7) {
                calendarGrid.add(week)
                week = mutableListOf()
            }
        }

        // Add empty slots after the last day of the month to fill out the last row
        while (week.size < 7) {
            week.add(null)
        }

        // Add remaining days if any
        if (week.isNotEmpty()) {
            calendarGrid.add(week)
        }

        Column {
            // Display days of the week
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeekDay.values().forEach { day ->
                    Text(
                        text = day.name.take(3), // Take the first 3 characters to abbreviate
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Display dates aligned with the correct weekday
            for (week in calendarGrid) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    week.forEachIndexed { index, day ->
                        if (day != null) {
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp)
                                    .background(
                                        if (day == currentDay && day.month == currentMonth.month)
                                            Color.Gray.copy(alpha = 0.5f)
                                        else
                                            Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.dayOfMonth.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            // Add spacer for empty slots
                            Spacer(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CalendarDay(day: Int?) {
        Box(
            modifier = Modifier
//                .weight(1f)
                .aspectRatio(1f)
                .padding(4.dp)
                .background(Color.Gray.copy(alpha = 0.3f))
                .clickable(enabled = day != null) {

                },
            contentAlignment = Alignment.Center
        ) {
            if (day != null) {
                Text(
                    text = day.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    private fun generateCalendarGrid(daysInMonth: Int, dayOffset: Int): List<List<Int?>> {
        val totalCells = daysInMonth + dayOffset
        val totalRows = (totalCells + 6) / 7
        val calendarGrid = MutableList(totalRows * 7) { null as Int? }

        for (day in 1..daysInMonth) {
            calendarGrid[dayOffset + day - 1] = day
        }

        return calendarGrid.chunked(7)
    }

    private fun LocalDate.daysInMonth(): Int {
        val year = this.year
        val month = this.month
        return when (month) {
            Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY, Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            Month.FEBRUARY -> if (year.isLeapYear()) 29 else 28
            else -> TODO()
        }
    }

    private fun Int.isLeapYear(): Boolean {
        return (this % 4 == 0 && this % 100 != 0) || (this % 400 == 0)
    }

    private fun LocalDate.withDayOfMonth(dayOfMonth: Int): LocalDate {
        return LocalDate(this.year, this.month, dayOfMonth)
    }
}
