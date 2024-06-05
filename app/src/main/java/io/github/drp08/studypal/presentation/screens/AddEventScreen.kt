package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.datetime.LocalDateTime
import network.chaintech.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.utils.DateTimePickerView
import network.chaintech.utils.MAX
import network.chaintech.utils.MIN
import network.chaintech.utils.TimeFormat
import network.chaintech.utils.WheelPickerDefaults
import network.chaintech.utils.dateTimeToString
import network.chaintech.utils.now
import kotlin.math.roundToInt

object AddEventScreen : Screen {
    @Composable
    override fun Content() {
        Column {
            SubjectNameTextField()
            ExamDateDialogueBox()
            StudyHoursDropDown()
            ConfidenceSlider()
        }
    }

    @Composable
    fun SubjectNameTextField() {
        var subjectName by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = subjectName,
            onValueChange = { subjectName = it },
            label = { Text("Subject Name") }
        )
    }

    // Todo: Change exam date to not include time
    @Composable
    fun ExamDateDialogueBox() {
        var showDateTimePicker by remember { mutableStateOf(false) }
        var dateTime by rememberSaveable { mutableStateOf("") }

        if (showDateTimePicker) {
            WheelDateTimePickerView(
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                showDatePicker = showDateTimePicker,
                title = "Add Date/Time",
                doneLabel = "Okay",
                timeFormat = TimeFormat.HOUR_24,
                titleStyle = LocalTextStyle.current,
                doneLabelStyle = LocalTextStyle.current,
                startDate = LocalDateTime.now(),
                minDate = LocalDateTime.MIN(),
                maxDate = LocalDateTime.MAX(),
                yearsRange = IntRange(2024, 2300),
                height = 170.dp,
                rowCount = 5,
                dateTextStyle = MaterialTheme.typography.displayMedium,
                dateTextColor = LocalContentColor.current,
                hideHeader = false,
                containerColor = Color.White,
                shape = RoundedCornerShape(10.dp),
                dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
                onDoneClick = { dateTime = dateTimeToString(it, "hh:mm dd-MM-yyyy")
                              showDateTimePicker = false},
                selectorProperties = WheelPickerDefaults.selectorProperties(borderColor = Color.DarkGray),
                onDismiss = { showDateTimePicker = false}
            )
        }

        OutlinedButton(onClick = { showDateTimePicker = true }) {
            Text("Exam Date? (Optional)")
        }
        Text(text = dateTime, textAlign = TextAlign.Left)
    }

    @Composable
    fun StudyHoursDropDown() {
        val studyHoursToChoose = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
        val expanded = remember { mutableStateOf(false) }
        var studyHours by rememberSaveable { mutableStateOf(studyHoursToChoose[0]) }

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.clickable {
                expanded.value = !expanded.value
            }
        ) {
            Text (
                text = studyHours,
            )
            Icon (
                Icons.Filled.ArrowDropDown, "Amount of study hours per week?"
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                studyHoursToChoose.forEach {
                        item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            studyHours = item
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun ConfidenceSlider() {
        var confidence by remember { mutableFloatStateOf(0f) }
        Column {
            Slider(
                value = confidence,
                onValueChange = { confidence = it.roundToInt().toFloat() },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                steps = 10,
                valueRange = 0f..10f
            )
            Text(text = confidence.toString())
        }
    }
}