package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.AsyncImage
import io.github.drp08.studypal.presentation.viewmodels.FlowerViewModel
import io.github.drp08.studypal.presentation.viewmodels.LeaderboardViewModel
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

object FlowerViewScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: FlowerViewModel = viewModel()
        val boxCount by viewModel.boxCount.collectAsState()
        val flowerCounts by viewModel.flowerCounts.collectAsState()
        val names by viewModel.names.collectAsState()

        var showFlowerGarden by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { showFlowerGarden = true }) {
                    Text("Flower Garden")
                }
                Spacer(modifier = Modifier.width(16.dp))
                TextButton(onClick = { showFlowerGarden = false }) {
                    Text("Leaderboard")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (showFlowerGarden) {
                FlowerGardenScreen(boxCount, flowerCounts, names)
            }
            else {
                val viewModel2: LeaderboardViewModel = viewModel()
                val leaderboardItems by viewModel2.leaderboardItems.collectAsState()
                io.github.drp08.studypal.presentation.screens.LeaderboardScreen.LeaderboardScreen(leaderboardItems)
            }
        }
    }

    @Composable
    fun FlowerGardenScreen(boxCount: Int, flowerCounts: List<Int>, names: List<String>) {
                Text(
                    text = "LEADERBOARD",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                for (index in 0 until boxCount step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BoxWithFlowers(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            flowers = generateFlowerCenters(
                                flowerCounts[index],
                                200.dp - 45.dp,
                                200.dp - 45.dp
                            ),
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                        if (index + 1 < boxCount) {
                            BoxWithFlowers(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f),
                                flowers = generateFlowerCenters(
                                    flowerCounts[index + 1],
                                    200.dp - 45.dp,
                                    200.dp - 45.dp
                                ),
                            )

                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = names[index],
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = names[index + 1],
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }


    @Composable
    fun BoxWithFlowers(modifier: Modifier, flowers: List<Offset>) {
        Box(modifier = modifier.background(Color.Green)) {
            InternetImageBackground(
                url = "https://media.istockphoto.com/id/1287348587/vector/green-lawn-view-from-top-grass-and-bushes-summer-field.jpg?s=612x612&w=0&k=20&c=beeLyqiIditNPu-zQSZEznVz40bNEphK9Y6pcYHQWLk="
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawFlowerGarden(flowers)
            }
        }

    }

    @Composable
    private fun generateFlowerCenters(flowerCount: Int, boxWidth: Dp, boxHeight: Dp): List<Offset> {
        val boxWidthPx = boxWidth.toPx()
        val boxHeightPx = boxHeight.toPx()
        val maxPetalRadius = 10.dp.toPx()

        val imagePadding = 16.dp.toPx()

        val maxX = boxWidthPx - imagePadding
        val maxY = boxHeightPx - imagePadding

        return List(flowerCount) {
            val x = Random.nextFloat() * (maxX - 2 * maxPetalRadius) + maxPetalRadius
            val y = Random.nextFloat() * (maxY - 2 * maxPetalRadius) + maxPetalRadius
            Offset(x, y)
        }
    }

    private fun DrawScope.drawFlowerGarden(flowers: List<Offset>) {
        for (position in flowers) {
            drawFlower(position)
        }
    }

    private fun DrawScope.drawFlower(position: Offset) {
        val petalRadius = 10.dp.toPx() // Smaller petal radius
        val flowerRadius = 5.dp.toPx() // Smaller flower radius
        val petalColor = randomColor()
        val centerColor = randomColor()

        for (i in 0..4) {
            val angle = (72 * i).toFloat()
            val petalX = position.x + petalRadius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val petalY = position.y + petalRadius * sin(Math.toRadians(angle.toDouble())).toFloat()

            drawCircle(
                color = petalColor,
                radius = petalRadius / 2,
                center = Offset(petalX, petalY)
            )
        }
        drawCircle(
            color = centerColor,
            radius = flowerRadius,
            center = position
        )
    }

    private fun isExcludedColor(color: Color): Boolean {
        val red = color.red
        val green = color.green
        val blue = color.blue

        // Exclude all shades of grey
        if (red == green && green == blue) return true

        // Exclude all shades of green
        if (green > red && green > blue) return true

        // Exclude all shades of brown
        if (red > green && green > blue) return true

        return false
    }

    private fun randomColor(): Color {
        while (true) {
            val color = Color(
                red = Random.nextFloat(),
                green = Random.nextFloat(),
                blue = Random.nextFloat(),
                alpha = 1f
            )
            if (!isExcludedColor(color)) {
                return color
            }
        }
    }

    @Composable
    fun Dp.toPx(): Float {
        val density = LocalDensity.current
        return with(density) { this@toPx.toPx() }
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

