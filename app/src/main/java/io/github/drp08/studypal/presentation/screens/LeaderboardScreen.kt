package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.R
import io.github.drp08.studypal.presentation.viewmodels.LeaderboardViewModel

object LeaderboardScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: LeaderboardViewModel = viewModel()
        val leaderboardItems by viewModel.leaderboardItems.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TrophyIcon()
            Spacer(modifier = Modifier.height(24.dp))
            LeaderboardList(items = leaderboardItems)
        }
    }

    @Composable
    fun TrophyIcon() {
        Image(
            painter = painterResource(id = R.drawable.ic_trophy),
            contentDescription = "Trophy",
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD700))
                .padding(16.dp)
        )
    }

    @Composable
    fun LeaderboardList(items: List<io.github.drp08.studypal.presentation.viewmodels.LeaderboardItem>) {
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
    fun LeaderboardItemView(item: io.github.drp08.studypal.presentation.viewmodels.LeaderboardItem) {
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

}