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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.drp08.studypal.data.SchedulingRepositoryImpl
import io.github.drp08.studypal.presentation.components.fab.ExpandableFab
import io.github.drp08.studypal.presentation.components.fab.FabItem
import io.github.drp08.studypal.presentation.viewmodels.HomeViewModel
import io.github.drp08.studypal.utils.LocalDatabase
import io.github.drp08.studypal.utils.client
import kotlinx.coroutines.delay

object HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = HomeViewModel(SchedulingRepositoryImpl(client), LocalDatabase.current.subjectDao, LocalDatabase.current.sessionDao)
        val items by viewModel.items.collectAsState()
        val currentTime = System.currentTimeMillis()

        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                ExpandableFab(
                    items = listOf(FabItem("Subject"), FabItem("Event"))
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "You don't have anything. Click the plus button",
                            modifier = Modifier.padding(all = 16.dp)
                        )
                    }
                } else {
                    val item = items[0]

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
                                text = "Subject",
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

                if (items.size > 1)
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
                                items(items.drop(1)) {
                                    Text(text = "Hi")
                                }
                            }
                        }
                    }

            }
        }
    }

    @Composable
    private fun Countdown(
        from: Long,
        modifier: Modifier = Modifier,
        onFinish: @Composable () -> Unit = {}
    ) {
        var timeLeft by remember { mutableStateOf(from) }
        var hasFinished by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = timeLeft) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            hasFinished = true
        }

        if (!hasFinished) {
            Text(
                text = "The time",
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
            onClick = { navigator.push(PomodoroScreen(startTime, endTime) )},
            modifier = modifier
        ) {
            Text(text = "Check-in")
        }
    }
}