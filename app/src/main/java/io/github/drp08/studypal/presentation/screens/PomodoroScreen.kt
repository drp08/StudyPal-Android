package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class PomodoroScreen(
    private val startTime: Long,
    private val endTime: Long
) : Screen {
    @Composable
    override fun Content() {
        val current = System.currentTimeMillis()
        val totalTimeLeft = endTime - startTime
        var timeLeft by remember { mutableLongStateOf(endTime - current) }

        LaunchedEffect(key1 = timeLeft) {
            if (timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1000L
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {}) {
                            Text(text = "Pomodoro short break")
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Button(onClick = {}) {
                            Text(text = "Pomodoro long break")
                        }
                    }

                    val endTimeStr = formatTime(endTime)
                    Text(
                        text = "Session finishes at $endTimeStr",
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .aspectRatio(1f),
                            progress = { timeLeft.toFloat() / totalTimeLeft },
                            strokeWidth = 12.dp,
                            strokeCap = StrokeCap.Round
                        )

                        Text(
                            text = formatTime(timeLeft),
                            fontSize = 32.sp
                        )
                    }
                    Text(
                        text = "Until Break",
                        modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    private fun formatTime(timeLeft: Long): String {
        val instant = Instant.ofEpochMilli(timeLeft)
        val localTime = instant.atZone(ZoneId.systemDefault()).toLocalTime()

        val formatter = DateTimeFormatter.ofPattern("mm:ss")
        return localTime.format(formatter)
    }
}