package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import io.github.drp08.studypal.presentation.components.fab.ExpandableFab
import io.github.drp08.studypal.presentation.components.fab.FabItem
import io.github.drp08.studypal.presentation.models.HomeSessionItem
import io.github.drp08.studypal.presentation.viewmodels.HomeViewModel
import io.github.drp08.studypal.presentation.viewmodels.PomodoroViewModel
import io.github.drp08.studypal.utils.formatTime
import kotlinx.coroutines.delay

data object HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<HomeViewModel>()
        val items by viewModel.items.collectAsState()
        val currentTime = System.currentTimeMillis()
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                ExpandableFab(
                    items = listOf(
                        FabItem("Subject", AddEventScreen),
                        FabItem("Event", AddEventScreen)
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(vertical = 24.dp, horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (items.isEmpty()) {
                    NoSubjectsCard()
                } else {
                    val item = items[0]

                    UpcomingSubjectCard(item, currentTime, navigator)
                }

                if (items.size > 1)
                    RemainingSessions(items)

                Button(onClick = {viewModel.updateXP()}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Finish Session")
                }
            }

        }
    }

    @Composable
    private fun RemainingSessions(items: List<HomeSessionItem>) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Upcoming sessions/events today",
                    fontSize = 18.sp
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items.drop(1)) { item ->
                        RemainingSessionCard(item, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun RemainingSessionCard(item: HomeSessionItem, modifier: Modifier = Modifier) {
        val startFormat = formatTime(
            item.session.startTime + 3 * 60 * 60 * 1000,
            "HH:mm a"
        )
        val endFormat = formatTime(
            item.session.endTime + 3 * 60 * 60 * 1000,
            "HH:mm a"
        )
        Card(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 6.dp)
            ) {
                Text(text = "$startFormat - $endFormat", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "Subject: ${item.subject.name}, Topic: ${item.topic.name} (Session ${item.subject.completedSessions + 1}/${item.subject.totalSessions})",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    @Composable
    private fun UpcomingSubjectCard(
        item: HomeSessionItem,
        currentTime: Long,
        navigator: Navigator
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp)
            ) {
                Text(text = "Next Revision/Event: ")
                Text(
                    text = "Subject: ${item.subject.name}, Topic: ${item.topic.name}",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    fontSize = 18.sp
                )
                if (item.session.startTime > currentTime) {
                    Text(text = "Starts in")
                    Countdown(
                        from = item.session.startTime - currentTime,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        CheckInButton(
                            navigator,
                            item.session.startTime,
                            item.session.endTime,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                } else {
                    CheckInButton(
                        navigator = navigator,
                        startTime = item.session.startTime,
                        endTime = item.session.endTime,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

    @Composable
    private fun NoSubjectsCard() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "You don't have anything. Click the plus button",
                modifier = Modifier.padding(all = 16.dp)
            )
        }
    }

    @Composable
    private fun Countdown(
        from: Long,
        modifier: Modifier = Modifier,
        onFinish: @Composable () -> Unit = {}
    ) {
        var timeLeft by remember { mutableLongStateOf(from) }
        var hasFinished by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1000L
            }
            hasFinished = true
        }

        if (!hasFinished) {
            Text(
                text = formatTime(timeLeft, "HH:mm:ss"),
                modifier = modifier,
                fontSize = 18.sp
            )
        } else {
            onFinish()
        }
    }


    @Composable
    private fun CheckInButton(
        navigator: Navigator,
        startTime: Long,
        endTime: Long,
        modifier: Modifier = Modifier
    ) {
        Button(
            onClick = { navigator.push(PomodoroScreen(startTime, endTime)) },
            modifier = modifier
        ) {
            Text(text = "Check-in")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemainingSessionCardPreview() {
    val item = HomeSessionItem(
        subject = SubjectEntity(
            "Math",
            0.4f,
            8,
            2,
            6,
            10,
            null
        ),
        topic = TopicEntity(
            name = "Algebra",
            subject = "Math"
        ),
        session = SessionEntity(
            topic = "Algebra",
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis() + 3 * 60 * 60 * 1000,
        )
    )
    HomeScreen.RemainingSessionCard(
        item, modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    )
}