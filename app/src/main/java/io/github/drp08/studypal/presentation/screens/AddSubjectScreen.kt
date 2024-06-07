package io.github.drp08.studypal.presentation.screens

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.drp08.studypal.data.SchedulingRepositoryImpl
import io.github.drp08.studypal.presentation.viewmodels.AddSubjectViewModel
import io.github.drp08.studypal.presentation.viewmodels.AddSubjectViewModel.UiAction.ChangeConfidence
import io.github.drp08.studypal.presentation.viewmodels.AddSubjectViewModel.UiAction.ChangeExamDate
import io.github.drp08.studypal.presentation.viewmodels.AddSubjectViewModel.UiAction.ChangeStudyHours
import io.github.drp08.studypal.presentation.viewmodels.AddSubjectViewModel.UiAction.ChangeSubject
import io.github.drp08.studypal.presentation.viewmodels.AddSubjectViewModel.UiAction.AddSubject
import io.github.drp08.studypal.utils.LocalDatabase
import io.github.drp08.studypal.utils.client
import io.github.drp08.studypal.utils.formatTime
import kotlinx.datetime.toJavaLocalDate
import network.chaintech.ui.datepicker.WheelDatePickerView
import network.chaintech.utils.DateTimePickerView
import network.chaintech.utils.WheelPickerDefaults
import java.time.ZoneId

object AddSubjectScreen : Screen {
    @Composable
    override fun Content() {
        val subjectDao = LocalDatabase.current.subjectDao
        val topicsDao = LocalDatabase.current.topicDao
        val sessionDao = LocalDatabase.current.sessionDao
        val user = LocalDatabase.current.userDao
        val navigator = LocalNavigator.currentOrThrow
        val repository = SchedulingRepositoryImpl(client, subjectDao, topicsDao, sessionDao, user)

        val viewModel = viewModel {
            AddSubjectViewModel(
                subjectDao = subjectDao,
                topicDao = topicsDao,
                schedulingRepository = repository
            )
        }
        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(320.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(3.dp)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Column {
                    SubjectNameTextField(
                        name = state.name,
                        onNameChange = { viewModel.on(ChangeSubject(it)) }
                    )
                    ExamDateDialogueBox(
                        examDate = state.examEpoch,
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
                    Button(
                        onClick = { viewModel.on(AddSubject(navigator)) },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 12.dp)
                    ) {
                        Text(text = "Add Subject")
                    }
                }
            }
        }
    }

    @Composable
    fun SubjectNameTextField(
        name: String,
        onNameChange: (String) -> Unit
    ) {
        Box(
            modifier = Modifier
                .padding(start = 1.dp, end = 1.dp)
                .height(80.dp)
                .background(Color.Transparent)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text(text = "Subject Name", color = Color.DarkGray) },
            )
        }
    }

    @Composable
    fun ExamDateDialogueBox(
        examDate: Long?,
        onDateChange: (Long) -> Unit
    ) {
        var showDatePicker by remember { mutableStateOf(false) }

        if (showDatePicker) {
            WheelDatePickerView(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                showDatePicker = showDatePicker,
                title = "Add Exam Date",
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
                    onDateChange(
                        it.toJavaLocalDate()
                            .atStartOfDay()
                            .atZone(ZoneId.systemDefault())
                            .toEpochSecond() * 1000
                    )
                    showDatePicker = false
                },
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
                if (examDate == null)
                    Text(
                        text = "Exam Date (Optional)",
                        color = Color.DarkGray
                    )
                else {
                    val date = formatTime(examDate, "dd-MM-yyyy")
                    Text(text = "Exam Date: $date", color = Color.DarkGray)
                }
            }
        }
    }


    @Composable
    fun StudyHoursDropDown(
        studyHours: Int,
        onChange: (Int) -> Unit
    ) {
        val studyHoursToChoose = (1..30).toList().toIntArray()
        val expanded = remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 6.dp, top = 6.dp)
                .background(Color.Transparent)
                .fillMaxWidth(),
        ) {
            Text(
                text = "Hours of study per week: ",
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
                    text = studyHours.toString(),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Icon(
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
                    studyHoursToChoose.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.toString()) },
                            onClick = {
                                onChange(item)
                                expanded.value = false
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
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
            Text(
                text = "How confident are you with this",
                modifier = Modifier
                    .padding(start = 20.dp)
            )
            Text(
                text = "subject?",
                modifier = Modifier
                    .padding(start = 20.dp)
            )
            Slider(
                value = confidence,
                onValueChange = onConfidenceChange,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                steps = 9,
            )
            Row(
                modifier = Modifier
                    .padding(start = 35.dp, end = 35.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Not", fontSize = 12.sp)
                Text(text = "Very", fontSize = 12.sp)
            }
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Confident", fontSize = 12.sp)
                Text(text = "Confident", fontSize = 12.sp)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddSubjectScreenPreview() {
    AddSubjectScreen.Content()
}