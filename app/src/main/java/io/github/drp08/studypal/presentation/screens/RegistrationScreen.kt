package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.drp08.studypal.presentation.viewmodels.RegistrationViewModel
import io.github.drp08.studypal.utils.formatTime
import network.chaintech.ui.timepicker.WheelTimePickerView
import network.chaintech.utils.DateTimePickerView
import network.chaintech.utils.TimeFormat
import network.chaintech.utils.WheelPickerDefaults

object RegistrationScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<RegistrationViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        if (viewModel.isUserRegistered())
            navigator.replace(HomeScreen)

        val user = viewModel.user

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "StudyPal",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            TextField(
                value = user.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Your name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors()
            )

            Text(
                text = "I would like revision to be scheduled between",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var showStartTimeDialog by remember { mutableStateOf(false) }
                WheelTimePickerView(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .fillMaxWidth(),
                    showTimePicker = showStartTimeDialog,
                    title = "From",
                    doneLabel = "Done",
                    titleStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    doneLabelStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = Color.Black
                    ),
                    timeFormat = TimeFormat.AM_PM,
                    height = 180.dp,
                    rowCount = 5,
                    dragHandle = {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .width(100.dp)
                                .clip(CircleShape),
                            thickness = 4.dp,
                            color = Color.DarkGray
                        )
                    },
                    hideHeader = false,
                    containerColor = Color.White,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
                    onDoneClick = {
                        viewModel.onWorkingHoursStartChange(it.toMillisecondOfDay().toLong())
                        showStartTimeDialog = false
                    },
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        borderColor = Color.DarkGray
                    ),
                    onDismiss = { showStartTimeDialog = false }
                )

                TextButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(),
                    onClick = { showStartTimeDialog = true }
                ) {
                    val timeFormat = formatTime(user.startWorkingHours, "HH:mm a")
                    Text(text = "Start: $timeFormat")
                }

                Text(
                    text = "and",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                var showEndTimeDialog by remember { mutableStateOf(false) }
                WheelTimePickerView(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .fillMaxWidth(),
                    showTimePicker = showEndTimeDialog,
                    title = "From",
                    doneLabel = "Done",
                    titleStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    doneLabelStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = Color.Black
                    ),
                    timeFormat = TimeFormat.AM_PM,
                    height = 180.dp,
                    rowCount = 5,
                    dragHandle = {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .width(100.dp)
                                .clip(CircleShape),
                            thickness = 4.dp,
                            color = Color.DarkGray
                        )
                    },
                    hideHeader = false,
                    containerColor = Color.White,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
                    onDoneClick = {
                        viewModel.onWorkingHoursEndChange(it.toMillisecondOfDay().toLong())
                        showEndTimeDialog = false
                    },
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        borderColor = Color.DarkGray
                    ),
                    onDismiss = { showEndTimeDialog = false }
                )

                TextButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(),
                    onClick = { showEndTimeDialog = true }
                ) {
                    val timeFormat = formatTime(user.endWorkingHours, "HH:mm a")
                    Text(text = "End: $timeFormat")
                }
            }

            TextField(
                value = user.maxStudyingHours.toString(),
                onValueChange = {
                    viewModel.onHoursPerDayChange(it.toInt())
                },
                label = { Text("I want to work ... Hours per day") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handle Google Calendar import */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Import Calendar", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onRegister(navigator) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Register", fontSize = 18.sp)
            }
        }
    }
}

