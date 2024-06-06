package io.github.drp08.studypal.presentation.components.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.drp08.studypal.presentation.screens.AddEventScreen
import io.github.drp08.studypal.presentation.screens.AddSubjectScreen

@Composable
fun ExpandableFab(
    items: List<FabItem>,
    modifier: Modifier = Modifier
) {
    var fabState by rememberSaveable { mutableStateOf(FabState.COLLAPSED) }
    val iconRotation = remember(key1 = fabState) {
        when (fabState) {
            FabState.EXPANDED -> 45f
            FabState.COLLAPSED -> 0f
        }
    }

    val animatedIconRotation by animateFloatAsState(
        targetValue = iconRotation,
        animationSpec = tween(durationMillis = 150),
        label = "iconRotation"
    )

    Column(
        modifier = modifier.fillMaxSize().padding(all = 16.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom)
    ) {
        FabMenu(
            items = items,
            visible = fabState == FabState.EXPANDED
        )
        Fab(
            state = fabState,
            onStateChange = { state -> fabState = state },
            modifier = Modifier.rotate(animatedIconRotation)
        )
    }
}

@Composable
private fun Fab(
    state: FabState,
    onStateChange: (FabState) -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
        onClick = {
            if (state == FabState.EXPANDED)
                onStateChange(FabState.COLLAPSED)
            else onStateChange(FabState.EXPANDED)
        },
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}

@Composable
private fun FabMenu(
    items: List<FabItem>,
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val screenToChoose = arrayOf(AddSubjectScreen, AddEventScreen)
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEachIndexed { i, menuItem ->
            AnimatedVisibility(
                visible = visible,
                enter = expandVertically(animationSpec = tween(durationMillis = 150))
                        + fadeIn(animationSpec = tween(durationMillis = 150)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 150))
                        + fadeOut(animationSpec = tween(durationMillis = 150))
            ) {
                FabMenuItem(
                    item = menuItem,
                    onItemClick = { navigator.push(screenToChoose[i]) }
                )
            }
        }
    }
}

@Composable
private fun FabMenuItem(
    item: FabItem,
    onItemClick: (FabItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(6.dp),
            color = Color.Black.copy(alpha = 0.8f)
        ) {
            Text(
                text = item.label,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp),
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        FloatingActionButton(
            modifier = modifier,
            onClick = { onItemClick(item) },
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}
