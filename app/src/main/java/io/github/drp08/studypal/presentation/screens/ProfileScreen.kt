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
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data object ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<ProfileViewModel>()

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
            val subjectList = arrayOf("Statistics")
            numberSubjects = subjectList.size
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
                                        Text(subject, fontSize = 18.sp, color = Color.DarkGray, fontWeight = FontWeight.Bold)
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
                                        "Total Hours studied so far: 12",
                                        fontSize = 16.sp,
                                        color = Color.DarkGray,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        "Exam Date: 10-12-2024",
                                        fontSize = 16.sp,
                                        color = Color.DarkGray,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }
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
//            Box(
//
//            ) {
//
//            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen.Content()
}

