package io.github.drp08.studypal.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.db.session.UserSession.Companion.ActiveUser
import io.github.drp08.studypal.presentation.viewmodels.ProfileViewModel
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import kotlin.random.Random

data object ProfileScreen : Screen {
    @Composable
    override fun Content() {
        //val viewModel = hiltViewModel<ProfileViewModel>()
        val viewModel: ProfileViewModel = hiltViewModel()
        val user = ActiveUser.current
        Column(
            modifier = Modifier
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
                Icon(Icons.Default.Face , contentDescription = "Student icon",
                    modifier = Modifier.size(128.dp))
                Spacer(modifier = Modifier.width(30.dp))
                Column (
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Name: ${user.name}", fontSize = 24.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Total XP: ")
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                OutlinedButton(
                    onClick = { ProfileScreen}
                ) {
                    Text("Add Friends")
                }
                Spacer(modifier = Modifier.width(45.dp))
                OutlinedButton(
                    onClick = { ProfileScreen }
                ) {
                    Text("Your Friends")
                }
            }
            var numTopicsExpanded by remember { mutableIntStateOf(0) }
            var numberSubjects by remember { mutableIntStateOf(0) }
            val subjectList = viewModel.subjects.collectAsState().value
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
                Column (
                    modifier = Modifier.animateContentSize()
                ) {
                    Text("Existing Subjects", fontSize = 20.sp, color = Color.DarkGray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    for (subject in subjectList) {
                        var expanded by remember { mutableStateOf(false)}
                        Column (
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
                                        Text(subject.name, fontSize = 18.sp, color = Color.DarkGray, fontWeight = FontWeight.Bold)
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
                                        "Exam Date: 10-12-2024", //Todo: Change this to actual exam date of subject
                                        fontSize = 16.sp,
                                        color = Color.DarkGray,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }
                            // Todo: change topics to be actual list of topics for the specific subject
                            val topics = arrayOf("Markov Chains", "Binomial Theorem", "Central Limit Theorem", "Chi-squared")
                            val numTopics : Int = topics.size
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
                                    }
                                ,
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
                        fontWeight = FontWeight.Bold )
                    Spacer(modifier = Modifier.height(4.dp))
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
                        color = Color.DarkGray)
                }
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
            .startDrawPadding(180.dp)
            .labelData { index -> barsData[index].label }
            .axisLineColor(MaterialTheme.colorScheme.tertiary)
            .axisLabelColor(MaterialTheme.colorScheme.tertiary)
            .build()

        val xAxisData = AxisData.Builder()
            .steps(stepSize)
            .endPadding(10.dp)
            .bottomPadding(16.dp)
            .labelData { index -> (index * ( maxHours/stepSize)).toString() }
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
        var index = 0
        for (subject in array) {
            val point =
                Point(
                "%.2f".format(Random.nextDouble(1.0, maxRange.toDouble())).toFloat(), //Todo: Change to number of hours spent on that subject
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
            index++
        }
        return list
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen.Content()
}

