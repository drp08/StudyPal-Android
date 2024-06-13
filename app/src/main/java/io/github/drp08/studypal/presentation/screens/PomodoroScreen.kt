package io.github.drp08.studypal.presentation.screens

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.utils.formatTime
import kotlinx.coroutines.delay

data class PomodoroScreen(
    private val startTime: Long,
    private var endTime: Long
) : Screen {
    @Composable
    override fun Content() {
        val current = System.currentTimeMillis()
        val totalTimeLeft = endTime - startTime
        var timeLeft by remember { mutableLongStateOf(endTime - current) }
        var showDialog by remember { mutableStateOf(false) }
        var selectedMinutes by remember { mutableStateOf(5L) }

        LaunchedEffect(key1 = timeLeft) {
            if (timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1000L
            }
        }

        if (showDialog) {
            TimeExtendDialog(
                selectedMinutes = selectedMinutes,
                onMinutesSelected = { selectedMinutes = it },
                onCancel = { showDialog = false },
                onExtend = {
                    endTime += selectedMinutes * 60 * 1000
                    timeLeft += selectedMinutes * 60 * 1000
                    showDialog = false
                }
            )
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

                    val endTimeStr = formatTime(endTime, "HH:mm")
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
                            text = formatTime(timeLeft, "HH:mm:ss"),
                            fontSize = 32.sp
                        )
                    }
                    Text(
                        text = "Until Break",
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { showDialog = true }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(text = "Extend Session")
                    }
                }
            }
        }
    }
    @Composable
    fun TimeExtendDialog(
        selectedMinutes: Long,
        onMinutesSelected: (Long) -> Unit,
        onCancel: () -> Unit,
        onExtend: () -> Unit
    ) {
        val options = listOf(5L, 10L, 15L)
        var expanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = onCancel,
            title = { Text(text = "Extend Session") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Select additional time:")
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .selectable(
                                selected = expanded,
                                onClick = { expanded = !expanded }
                            )
                    ) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { minutes ->
                                DropdownMenuItem({ Text(text = "$minutes minutes") }, onClick = {
                                    onMinutesSelected(minutes)
                                    expanded = false
                                })
                            }
                        }
                        Text(text = "$selectedMinutes minutes")
                    }
                }
            },
            confirmButton = {
                Button(onClick = onExtend) {
                    Text(text = "Extend")
                }
            },
            dismissButton = {
                Button(onClick = onCancel) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}