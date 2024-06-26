package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.AsyncImage
import io.github.drp08.studypal.presentation.screens.NoLeaderboardFlowerGarden.SingleGardenScreen
import io.github.drp08.studypal.presentation.viewmodels.FlowerViewModel
import io.github.drp08.studypal.presentation.viewmodels.FriendsViewModel
import io.github.drp08.studypal.presentation.viewmodels.LeaderboardItem
import io.github.drp08.studypal.presentation.viewmodels.LeaderboardViewModel


object LeaderboardScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: LeaderboardViewModel = viewModel()
        val showLeaderboard by viewModel.isLeaderboard.collectAsState()
        val showSingleGarden by viewModel.isLeaderboardEnabled.collectAsState()
        val isLeaderboard by viewModel.currentlyInLeaderBoardView.collectAsState()
        val isFlowerView by viewModel.currentlyInFlowerView.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Switch(
                    checked = showSingleGarden,
                    onCheckedChange = { viewModel.toggleLeaderboard() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF258a40),
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = Color.Red,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
                    )

                )
            }
            if (!showSingleGarden) {
                SingleGardenScreen()
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = { if(!isLeaderboard) {viewModel.toggleLeaderboardAndFlowers()} }) {
                        Text("Leaderboard")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = { if(!isFlowerView) {viewModel.toggleLeaderboardAndFlowers()} }) {
                        Text("FlowerGarden")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (showLeaderboard) {
                    val leaderboardItems by viewModel.leaderboardItems.collectAsState()

                    LeaderboardScreen(leaderboardItems = leaderboardItems)
                } else {
                    val viewModel2: FlowerViewModel = viewModel()
                    val boxCount by viewModel2.boxCount.collectAsState()
                    val flowerCounts by viewModel2.flowerCounts.collectAsState()
                    val names by viewModel2.names.collectAsState()

                    FlowerViewScreen.FlowerGardenScreen(
                        boxCount = 3,
                        flowerCounts = flowerCounts,
                        names = names
                    )
                }
            }
        }
    }

    @Composable
    fun LeaderboardScreen(leaderboardItems: List<LeaderboardItem>) {
        TrophyIcon()
        Spacer(modifier = Modifier.height(24.dp))
        LeaderboardList(items = leaderboardItems)
    }

    @Composable
    fun TrophyIcon() {
        Box(
            Modifier
                .height(100.dp)
                .width(100.dp)
                .padding(16.dp)
        ) {
            InternetImageBackground(
                url = "https://static-00.iconduck.com/assets.00/trophy-emoji-512x512-x32hyhlp.png",
            )
        }
    }

    @Composable
    fun LeaderboardList(items: List<LeaderboardItem>) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items.forEach { item ->
                LeaderboardItemView(item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    @Composable
    fun LeaderboardItemView(item: LeaderboardItem) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${item.xp} XP",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    fun calculateXp(min: Float, decimalCompletedSessions: Float): Float {
        val bonusPoints = min%100 * decimalCompletedSessions
        return (min * decimalCompletedSessions) + bonusPoints
    }

    @Composable
    fun InternetImageBackground(url: String) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

}