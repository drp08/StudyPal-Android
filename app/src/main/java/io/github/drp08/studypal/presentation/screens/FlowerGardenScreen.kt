package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

object FlowerViewScreen : Screen {

    @Composable
    override fun Content() {
        FlowerGardenScreen(flowerCount = 10) // Example flower count
    }

    @Composable
    fun FlowerGardenScreen(flowerCount: Int) {
        var flowers by remember { mutableStateOf<List<Offset>>(emptyList()) }

        for (i in 0..flowerCount)
            flowers = generateFlowerCenters(flowerCount)



        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    //.background(Color(0xFF87CEFA)) // Sky blue background
                    .background(Color(0xFF308B0D))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawFlowerGarden(flowers)
                }
            }
        }
    }

    @Composable
    private fun generateFlowerCenters(flowerCount: Int): List<Offset> {
        val blueBoxHeight = 750.dp.toPx()
        val blueBoxWidth = 375.dp.toPx()
        val flowerRadius = 60.dp.toPx()

        return List(flowerCount) {
            val x = Random.nextFloat() * (blueBoxWidth - 2 * flowerRadius) + flowerRadius
            val y = Random.nextFloat() * (blueBoxHeight - 2 * flowerRadius) + flowerRadius
            Offset(x, y)
        }
    }

    private fun DrawScope.drawFlowerGarden(flowers: List<Offset>) {
        for (position in flowers) {
            drawFlower(position)
        }
    }

    private fun DrawScope.drawFlower(position: Offset) {
        val petalRadius = 20.dp.toPx()
        val flowerRadius = 10.dp.toPx()
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

@Preview(showSystemUi = true)
@Composable
fun PreviewCanvasScreen() {
    FlowerViewScreen.FlowerGardenScreen(flowerCount = 10)
}