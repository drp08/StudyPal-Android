package io.github.drp08.studypal.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import io.github.drp08.studypal.data.UserRepositoryImpl.Companion.ActiveUser
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.presentation.viewmodels.ProfileViewModel
import kotlin.random.Random

data object ProfileScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<ProfileViewModel>()
        val user = ActiveUser.current
        val subjects by viewModel.subjects.collectAsState()

        InnerContent(
            userName = user.name,
            xp = user.xp,
            subjectList = subjects,
            navigator = LocalNavigator.currentOrThrow
        )
    }

    @Composable
    fun InnerContent(
        userName: String,
        xp: Int,
        subjectList: List<SubjectEntity>,
        navigator: Navigator,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .verticalScroll(state = rememberScrollState())
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp, end = 10.dp, bottom = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Face, contentDescription = "Student icon",
                    modifier = Modifier.size(128.dp)
                )
                Spacer(modifier = Modifier.width(30.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Name: $userName", fontSize = 24.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Total XP: $xp")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                OutlinedButton(
                    onClick = { navigator.push(FriendsScreen) }
                ) {
                    Text("Manage your Friends")
                }
            }
            var numTopicsExpanded by remember { mutableIntStateOf(0) }
            var numberSubjects by remember { mutableIntStateOf(0) }
            numberSubjects = subjectList.count()
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .height((56 + 132 * numberSubjects + 31.3 * numTopicsExpanded).dp)
                    .width(320.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(3.dp)
                    )
                    .padding(start = 12.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
                contentAlignment = Alignment.TopStart,
            ) {
                Column(
                    modifier = Modifier.animateContentSize()
                ) {
                    Text(
                        "Existing Subjects",
                        fontSize = 20.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    for (subject in subjectList) {
                        var expanded by remember { mutableStateOf(false) }
                        Column(
                            modifier = Modifier
                                .animateContentSize()
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(95.dp)
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                    )
                                    .padding(start = 16.dp, top = 6.dp, end = 16.dp),
                                contentAlignment = Alignment.TopStart,
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            subject.name,
                                            fontSize = 18.sp,
                                            color = Color.DarkGray,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(60.dp))
                                        LinearProgressIndicator(
                                            progress = { 0.65F },
                                            modifier = Modifier
                                                .width(120.dp)
                                                .height(18.dp),
                                            color = Color.Blue,
                                            trackColor = Color.LightGray
                                        )
                                    }
                                    Text(
                                        "Total Hours studied so far: 12", // Todo: Change this to the actual hours studied of the subject b¬so far
                                        fontSize = 16.sp,
                                        color = Color.DarkGray,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        "Exam Date: 10-12-2024", // Todo: Change this to actual exam date of subject
                                        fontSize = 16.sp,
                                        color = Color.DarkGray,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }
                            // Todo: change topics to be actual list of topics for the specific subject
                            val topics = arrayOf(
                                "Markov Chains",
                                "Binomial Theorem",
                                "Central Limit Theorem",
                                "Chi-squared"
                            )
                            val numTopics: Int = topics.size
                            Box(
                                modifier = Modifier
                                    .animateContentSize()
                                    .height(if (expanded) (44 + numTopics * 30).dp else 32.dp)
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                    )
                                    .padding(start = 8.dp, end = 8.dp, top = 4.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        expanded = !expanded
                                        if (expanded) {
                                            numTopicsExpanded += numTopics
                                        } else {
                                            numTopicsExpanded -= numTopics
                                        }
                                    },
                                contentAlignment = Alignment.TopStart,
                            ) {
                                Column {
                                    if (!expanded) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                "Show Topics",
                                                fontSize = 16.sp,
                                                color = Color.DarkGray,
                                            )
                                            Icon(
                                                Icons.Default.KeyboardArrowDown,
                                                "Show topics"
                                            )
                                        }
                                    } else {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth()
                                                .padding(top = 4.dp, bottom = 4.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    "Hide Topics",
                                                    fontSize = 16.sp,
                                                    color = Color.DarkGray,
                                                )
                                                Icon(
                                                    Icons.Default.KeyboardArrowUp,
                                                    "Hide topics"
                                                )
                                            }
                                            for (topic in topics) {
                                                Box(
                                                    modifier = Modifier
                                                        .height(30.dp)
                                                        .fillMaxWidth()
                                                        .border(
                                                            width = 1.dp,
                                                            color = Color.LightGray,
                                                        )
                                                        .padding(
                                                            start = 8.dp,
                                                            end = 8.dp,
                                                            top = 4.dp
                                                        ),
                                                    contentAlignment = Alignment.TopStart,
                                                ) {
                                                    Text(
                                                        topic,
                                                        fontSize = 16.sp,
                                                        color = Color.DarkGray,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .height((144 + numberSubjects * 44).dp)
                    .width(320.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(3.dp)
                    )
                    .padding(start = 12.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            ) {
                Column {
                    Text(
                        "Statistics",
                        fontSize = 20.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (subjectList.isNotEmpty()) { //Todo: Probs will be fucked up in the merge
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((72 + 44 * numberSubjects).dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(3.dp)
                                )
                                .padding(start = 12.dp, end = 12.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            val arrayStrings = subjectList.map { t -> t.name }.toTypedArray()
                            SubjectBarChart(arrayStrings)
                            Spacer(modifier = Modifier.height(40.dp))
                            Text(
                                text = "hours",
                                fontSize = 10.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Total study hours: 106", //Todo: Add actual total number of hours studied
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    } else {
                        Text(
                            "Total study hours: 0", //Todo: Add actual total number of hours studied
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "More statistics will appear here after you add a subject",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .width(320.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                var showDialog by remember { mutableStateOf(false) }
                val timeToChoose = (5..60 step 5).toList().toIntArray()
                val expandedWork = remember { mutableStateOf(false) }
                val expandedShort = remember { mutableStateOf(false) }
                val expandedLong = remember { mutableStateOf(false) }
                var shortBreakTime = 5
                var longBreakTime = 5
                var workTime = 5
                if (showDialog) {
                    Dialog(onDismissRequest = { showDialog = false }) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(30.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Work for: ",
                                        modifier = Modifier.padding(1.dp),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .size(45.dp, 32.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .border(
                                                width = 1.dp,
                                                color = Color.LightGray,
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .clickable { expandedWork.value = !expandedWork.value }
                                    ) {
                                        Text(
                                            text = workTime.toString(),
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            Modifier
                                                .align(Alignment.CenterEnd)
                                                .fillMaxHeight()
                                        )
                                        DropdownMenu(
                                            expanded = expandedWork.value,
                                            onDismissRequest = { expandedWork.value = false }
                                        ) {
                                            timeToChoose.forEach { item ->
                                                DropdownMenuItem(
                                                    text = { Text(text = item.toString()) },
                                                    onClick = {
                                                        workTime =
                                                            item //Todo: Connect to pomodoro timer
                                                        expandedWork.value = false
                                                    },
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = " minutes",
                                        modifier = Modifier.padding(1.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Short break for: ",
                                        modifier = Modifier.padding(1.dp),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .size(45.dp, 32.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .border(
                                                width = 1.dp,
                                                color = Color.LightGray,
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .clickable {
                                                expandedShort.value = !expandedShort.value
                                            }
                                    ) {
                                        Text(
                                            text = shortBreakTime.toString(),
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            Modifier
                                                .align(Alignment.CenterEnd)
                                                .fillMaxHeight()
                                        )
                                        DropdownMenu(
                                            expanded = expandedShort.value,
                                            onDismissRequest = { expandedShort.value = false }
                                        ) {
                                            timeToChoose.forEach { item ->
                                                DropdownMenuItem(
                                                    text = { Text(text = item.toString()) },
                                                    onClick = {
                                                        shortBreakTime =
                                                            item //Todo: Connect to pomodoro timer
                                                        expandedShort.value = false
                                                    },
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = " minutes",
                                        modifier = Modifier.padding(1.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Long break for: ",
                                        modifier = Modifier.padding(1.dp),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .size(45.dp, 32.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .border(
                                                width = 1.dp,
                                                color = Color.LightGray,
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .clickable { expandedLong.value = !expandedLong.value }
                                    ) {
                                        Text(
                                            text = longBreakTime.toString(),
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            Modifier
                                                .align(Alignment.CenterEnd)
                                                .fillMaxHeight()
                                        )
                                        DropdownMenu(
                                            expanded = expandedLong.value,
                                            onDismissRequest = { expandedLong.value = false }
                                        ) {
                                            timeToChoose.forEach { item ->
                                                DropdownMenuItem(
                                                    text = { Text(text = item.toString()) },
                                                    onClick = {
                                                        longBreakTime =
                                                            item //Todo: Connect to pomodoro timer
                                                        expandedLong.value = false
                                                    },
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = " minutes",
                                        modifier = Modifier.padding(1.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            // Todo: Store value in database / change
                                            showDialog = false
                                        },
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Text("Confirm")
                                    }
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .width(196.dp)
                        .height(40.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(3.dp)
                        )
                        .padding(start = 0.dp, end = 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = { showDialog = true },
                    ) {
                        Text(
                            text = "Change Pomodoro Timings",
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(124.dp))
            }
        }
    }

    @Composable
    fun SubjectBarChart(list: Array<String>) {
        val stepSize = 6
        val maxHours = 30 //Todo: Change to max of the total hours studied per subject
        val barsData = getBarChartData(
            maxRange = maxHours,
            array = list,
            dataCategoryOptions = DataCategoryOptions(isDataCategoryInYAxis = true)
        )

        val yAxisData = AxisData.Builder()
            .axisStepSize(20.dp)
            .steps(barsData.size - 1)
            .labelAndAxisLinePadding(1.dp)
            .axisOffset(20.dp)
            .setDataCategoryOptions(
                DataCategoryOptions(
                    isDataCategoryInYAxis = true,
                    isDataCategoryStartFromBottom = false
                )
            )
            .startDrawPadding(20.dp)
            .labelData { index -> barsData[index].label }
            .axisLineColor(MaterialTheme.colorScheme.tertiary)
            .axisLabelColor(MaterialTheme.colorScheme.tertiary)
            .build()

        val xAxisData = AxisData.Builder()
            .steps(stepSize)
            .endPadding(10.dp)
            .bottomPadding(16.dp)
            .labelData { index -> (index * (maxHours / stepSize)).toString() }
            .axisLineColor(MaterialTheme.colorScheme.tertiary)
            .axisLabelColor(MaterialTheme.colorScheme.tertiary)
            .axisLabelDescription { "hours" }
            .build()

        val barChartData = BarChartData(
            chartData = barsData,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            barStyle = BarStyle(
                isGradientEnabled = false,
                paddingBetweenBars = 10.dp,
                barWidth = 35.dp,
            ),
            showYAxis = true,
            showXAxis = true,
            barChartType = BarChartType.HORIZONTAL,
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        BarChart(
            modifier = Modifier.height((67 + list.size * 45).dp),
            barChartData = barChartData
        )
    }

    private fun getBarChartData(
        maxRange: Int,
        array: Array<String>,
        dataCategoryOptions: DataCategoryOptions
    ): List<BarData> {
        val list = arrayListOf<BarData>()
        for ((index, subject) in array.withIndex()) {
            val point =
                Point(
                    "%.2f".format(Random.nextDouble(1.0, maxRange.toDouble()))
                        .toFloat(), //Todo: Change to number of hours spent on that subject
                    index.toFloat()
                )
            list.add(
                BarData(
                    point = point,
                    color = Color(
                        Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)
                    ),
                    dataCategoryOptions = dataCategoryOptions,
                    label = subject.take(5),
                )
            )
        }
        return list
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen.InnerContent(
        userName = "Nishant",
        xp = 0,
        subjectList = ProfileViewModel.dummySubjects,
        navigator = LocalNavigator.currentOrThrow // doesn't effect preview
    )
}