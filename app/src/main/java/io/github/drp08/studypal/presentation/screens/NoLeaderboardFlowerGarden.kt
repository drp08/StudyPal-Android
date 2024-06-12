package io.github.drp08.studypal.presentation.screens
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.presentation.viewmodels.SingleGardenViewModel
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


object NoLeaderboardFlowerGarden : Screen {
    @Composable
    override fun Content() {
        SingleGardenScreen()
    }

    @Composable
    fun SingleGardenScreen() {
        val viewModel: SingleGardenViewModel = viewModel()
        val flowerCount by viewModel.flowerCount.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You have turned off the leaderboard, your friends can't see how you are doing. Turn it back on to continue",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            BoxWithFlowers(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(8.dp),
                flowers = generateFlowerCenters(flowerCount, 600.dp - 300.dp, 600.dp - 300.dp)
            )
        }
    }

    @Composable
    fun generateFlowerCenters(flowerCount: Int, boxWidth: Dp, boxHeight: Dp): List<Offset> {
        val boxWidthPx = boxWidth.toPx()
        val boxHeightPx = boxHeight.toPx()
        val maxPetalRadius = 15.dp.toPx()

        val imagePadding = 16.dp.toPx()

        val maxX = boxWidthPx - imagePadding
        val maxY = boxHeightPx - imagePadding

        return List(flowerCount) {
            val x = Random.nextFloat() * (maxX - 2 * maxPetalRadius) + maxPetalRadius
            val y = Random.nextFloat() * (maxY - 2 * maxPetalRadius) + maxPetalRadius
            Offset(x, y)
        }
    }

    @Composable
    fun BoxWithFlowers(modifier: Modifier, flowers: List<Offset>) {
        Box(modifier = modifier.background(Color.Green)) {
            FlowerViewScreen.InternetImageBackground(
                url = "https://media.istockphoto.com/id/1287348587/vector/green-lawn-view-from-top-grass-and-bushes-summer-field.jpg?s=612x612&w=0&k=20&c=beeLyqiIditNPu-zQSZEznVz40bNEphK9Y6pcYHQWLk="
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawFlowerGarden(flowers)
            }
        }

    }

    private fun DrawScope.drawFlowerGarden(flowers: List<Offset>) {
        for (position in flowers) {
            drawFlower(position)
        }
    }

    private fun DrawScope.drawFlower(position: Offset) {
        val petalRadius = 15.dp.toPx()
        val flowerRadius = 7.dp.toPx()
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

        if (red == green && green == blue) return true

        if (green > red && green > blue) return true

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

}

