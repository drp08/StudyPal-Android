package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.presentation.viewmodels.DailyCalendarView
import io.github.drp08.studypal.presentation.viewmodels.Event
import kotlinx.datetime.*


object DailyCalendarScreen : Screen {
    private fun readResolve(): Any = DailyCalendarScreen

    @Composable
    override fun Content() {
        val viewModel = viewModel<DailyCalendarView>()
        val currentDate by viewModel.currentDate.collectAsState(initial = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
        val events by viewModel.events.collectAsState(initial = emptyList())

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
        ) {
            Text(
                    text = "${currentDate.dayOfWeek}, ${currentDate.month} ${currentDate.dayOfMonth}, ${currentDate.year}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                val timeSlots = generateTimeSlots(LocalTime(8, 0), LocalTime(18, 0))

                LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.Start,
                ) {
                    items(timeSlots) { slot ->
                        TimeSlotRow(slot, events)
                    }
                }
            }
        }
    }
}

@Composable
fun TimeSlotRow(time: LocalTime, events: List<Event>) {
    val eventsInSlot = events.filter { it.startTime <= time.plusMinutes(60) && it.endTime >= time }

    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f)),
            verticalAlignment = Alignment.Top
    ) {
        val formattedTime = formattedTime(time)
        Text(
                text = formattedTime,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                        .padding(8.dp)
                        .width(60.dp)
        )

        Column(
                modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
        ) {
            eventsInSlot.forEach { event ->
                EventBlock(event, time)
            }
        }
    }
}

@Composable
fun EventBlock(event: Event, time: LocalTime) {
    val eventStartMinutes = if (event.startTime >= time) {
        (event.startTime.hour * 60 + event.startTime.minute) - (time.hour * 60 + time.minute)
    } else {
        0
    }
    val eventEndMinutes = if (event.endTime < time.plusMinutes(60)) {
        (event.endTime.hour * 60 + event.endTime.minute) - (time.hour * 60 + time.minute)
    } else {
        60
    }
    val eventHeight = ((eventEndMinutes - eventStartMinutes) * 60 / 60).dp

    Box(
            modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = eventStartMinutes.dp)
                    .height(eventHeight)
                    .background(Color.Blue.copy(alpha = 0.7f))
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center
    ) {
        Text(
                text = event.title,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
        )
    }
}


private fun generateTimeSlots(startTime: LocalTime, endTime: LocalTime): List<LocalTime> {
    val timeSlots = mutableListOf<LocalTime>()
    var current = startTime
    while (current <= endTime) {
        timeSlots.add(current)
        current = current.plusMinutes(60) // Add 1 hour
    }
    return timeSlots
}

private fun LocalTime.plusMinutes(minutes: Int): LocalTime {
    val totalMinutes = hour * 60 + minute + minutes
    val newHour = totalMinutes / 60
    val newMinute = totalMinutes % 60
    return LocalTime(newHour, newMinute)
}

private operator fun Int.compareTo(otherTime: LocalTime): Int {
    val thisTime = LocalTime(this, 0)
    return thisTime.compareTo(otherTime)
}

private fun formattedTime(time: LocalTime): String {
    val hourString = if (time.hour < 10) "0${time.hour}" else time.hour.toString()
    val minuteString = if (time.minute < 10) "0${time.minute}" else time.minute.toString()
    return "$hourString:$minuteString"
}

