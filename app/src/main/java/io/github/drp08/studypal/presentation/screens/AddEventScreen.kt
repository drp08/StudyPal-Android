package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.presentation.viewmodels.AddEventViewModel
import io.github.drp08.studypal.presentation.viewmodels.AddEventViewModel.UiAction.ChangeConfidence
import io.github.drp08.studypal.presentation.viewmodels.AddEventViewModel.UiAction.ChangeExamDate
import io.github.drp08.studypal.presentation.viewmodels.AddEventViewModel.UiAction.ChangeStudyHours
import io.github.drp08.studypal.presentation.viewmodels.AddEventViewModel.UiAction.ChangeSubject
import io.github.drp08.studypal.utils.formatTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import network.chaintech.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.utils.DateTimePickerView
import network.chaintech.utils.MAX
import network.chaintech.utils.MIN
import network.chaintech.utils.TimeFormat
import network.chaintech.utils.WheelPickerDefaults
import network.chaintech.utils.now
import java.time.ZoneId

object AddEventScreen : Screen {

    private val viewModel = AddEventViewModel()

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsState()

        Column {
            SubjectNameTextField(
                name = state.name,
                onNameChange = { viewModel.on(ChangeSubject(it)) }
            )
            ExamDateDialogueBox(
                examEpoch = state.examEpoch,
                onDateChange = { viewModel.on(ChangeExamDate(it)) }
            )
            StudyHoursDropDown(
                studyHours = state.hoursPerWeek,
                onChange = { viewModel.on(ChangeStudyHours(it)) }
            )
            ConfidenceSlider(
                confidence = state.confidenceLevel,
                onConfidenceChange = { viewModel.on(ChangeConfidence(it)) }
            )
        }
    }

    @Composable
    fun SubjectNameTextField(
        name: String,
        onNameChange: (String) -> Unit
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Subject Name") }
        )
    }

    // Todo: Change exam date to not include time
    @Composable
    fun ExamDateDialogueBox(
        examEpoch: Long,
        onDateChange: (Long) -> Unit
    ) {
        var showDateTimePicker by remember { mutableStateOf(false) }

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
                onDoneClick = {
                    onDateChange(
                        it.toJavaLocalDateTime()
                            .atZone(ZoneId.systemDefault())
                            .toEpochSecond()
                    )
                    showDateTimePicker = false
                },
                selectorProperties = WheelPickerDefaults.selectorProperties(borderColor = Color.DarkGray),
                onDismiss = { showDateTimePicker = false }
            )
        }

        OutlinedButton(onClick = { showDateTimePicker = true }) {
            Text("Exam Date? (Optional)")
        }
        Text(
            text = formatTime(examEpoch, "HH:mm dd-MM-yyyy"),
            textAlign = TextAlign.Left
        )
    }

    @Composable
    fun StudyHoursDropDown(
        studyHours: Int,
        onChange: (Int) -> Unit
    ) {
        val expanded = remember { mutableStateOf(false) }

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.clickable { expanded.value = !expanded.value }
        ) {
            Text(text = studyHours.toString())
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Amount of study hours per week?"
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                (1..9).forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.toString()) },
                        onClick = {
                            onChange(item)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun ConfidenceSlider(
        confidence: Float,
        onConfidenceChange: (Float) -> Unit
    ) {
        Column {
            Slider(
                value = confidence,
                onValueChange = onConfidenceChange,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                steps = 9
            )
            Text(text = (confidence * 10).toInt().toString())
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddEventScreenPreview() {
    AddEventScreen.Content()
}

@Preview(showBackground = true)
@Composable
fun ConfidenceSliderPreview() {
    var confidence by remember { mutableFloatStateOf(0.3f) }

    AddEventScreen.ConfidenceSlider(
        confidence = confidence,
        onConfidenceChange = { confidence = it }
    )
}