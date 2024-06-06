package io.github.drp08.studypal.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import network.chaintech.ui.datepicker.WheelDatePickerView
import network.chaintech.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.utils.DateTimePickerView
import network.chaintech.utils.TimeFormat
import network.chaintech.utils.WheelPickerDefaults
import network.chaintech.utils.dateTimeToString

object AddEventScreen : Screen {
    @Composable
    override fun Content() {
        var checked by remember { mutableStateOf(false) }
        var numberDatesAdded by remember { mutableStateOf(1) }
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .height(if (checked) (320 + 50 * numberDatesAdded).dp else 200.dp)
                    .width(320.dp)
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
                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .background(Color.Transparent)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Recurring event?")
                        Switch(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                            )
                        )
                    }
                    if (checked) {
                        var i = 0
                        Column {
                            while (i < numberDatesAdded) {
                                Row(
                                    modifier = Modifier
                                        .padding(start = 20.dp, end = 20.dp, bottom = 2.dp)
                                        .background(Color.Transparent)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    SmallFloatingActionButton(
                                        onClick = { numberDatesAdded++ }
                                    ) {
                                        Icon(Icons.Filled.Add, "Add button")
                                    }
                                    EventDateTimeDialogueBox()
                                }
                                i++
                            }
                            RepeatsWeekDropDown()
                            Row(
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp)
                                    .background(Color.Transparent)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "until")
                                UntilDateDialogueBox()
                            }
                        }
                    } else {
                        numberDatesAdded = 1
                    }
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

    @Composable
    fun RepeatsWeekDropDown() {
        val numberOfWeeksToChoose = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val expanded = remember { mutableStateOf(false) }
        var numberWeeks by rememberSaveable { mutableStateOf(numberOfWeeksToChoose[0]) }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 6.dp, top = 6.dp)
                .background(Color.Transparent)
                .fillMaxWidth(),
        ) {
            Text(
                text = "Repeats every ",
            )
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .size(40.dp, 32.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clickable { expanded.value = !expanded.value }
            ) {
                Text(
                    text = numberWeeks,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Icon (
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                )
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    numberOfWeeksToChoose.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                numberWeeks = item
                                expanded.value = false
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            Text(
                text = " weeks"
            )
        }
    }

    @Composable
    fun UntilDateDialogueBox() {
        var showDatePicker by remember { mutableStateOf(false) }
        var untilDate by rememberSaveable { mutableStateOf("Date") }

        if (showDatePicker) {
            WheelDatePickerView(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                showDatePicker = showDatePicker,
                title = "Add Date",
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
                    untilDate = ("Date: ").plus(it.toString())
                    showDatePicker = false },
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    borderColor = Color.DarkGray
                ),
                onDismiss = { showDatePicker = false }
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
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding()
            ) {
                Text(text = untilDate, color = Color.DarkGray)
            }
        }
    }
}