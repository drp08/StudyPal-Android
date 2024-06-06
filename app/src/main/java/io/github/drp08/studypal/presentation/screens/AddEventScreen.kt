package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import network.chaintech.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.utils.DateTimePickerView
import network.chaintech.utils.TimeFormat
import network.chaintech.utils.WheelPickerDefaults
import network.chaintech.utils.dateTimeToString

object AddEventScreen : Screen {
    @Composable
    override fun Content() {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(320.dp, 150.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(3.dp)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Column {
                    EventNameTextField()
                    EventDateTimeDialogueBox()
                }
            }
        }
    }

    @Composable
    fun EventNameTextField() {
        var eventName by rememberSaveable { mutableStateOf("") }

        Box (
            modifier = Modifier
                .padding(start = 1.dp, end = 1.dp)
                .height(80.dp)
                .background(Color.Transparent)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text(text = "Event Name", color = Color.DarkGray) },
            )
        }
    }

    @Composable
    fun EventDateTimeDialogueBox() {
        var showDateTimePicker by remember { mutableStateOf(false) }
        var eventDateTime by rememberSaveable { mutableStateOf("Event Date and Time") }

        if (showDateTimePicker) {
            WheelDateTimePickerView(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                showDatePicker = showDateTimePicker,
                title = "Add Event Date & Time",
                doneLabel = "Okay",
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
                yearsRange = IntRange(2024, 2300),
                height = 180.dp,
                rowCount = 5,
                dateTextStyle = TextStyle(
                    fontWeight = FontWeight(400)
                ),
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
                dateTextColor = Color.DarkGray,
                hideHeader = false,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
                onDoneClick = {
                    eventDateTime = ("Event Date Time: ")
                        .plus(dateTimeToString(it, "hh:mm a dd-MM-yyyy"))
                    showDateTimePicker = false },
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    borderColor = Color.DarkGray
                ),
                onDismiss = { showDateTimePicker = false }
            )
        }

        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 10.dp)
                .background(Color.Transparent)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            TextButton(
                onClick = { showDateTimePicker = true },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding()
            ) {
                Text(text = eventDateTime, color = Color.DarkGray)
            }
        }
    }

}