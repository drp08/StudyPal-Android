package io.github.drp08.studypal.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.db.session.UserSession.Companion.ActiveUser
import io.github.drp08.studypal.presentation.viewmodels.ProfileViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .height(320.dp)
                    .width(320.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(3.dp)
                    )
                    .padding(start = 12.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
                contentAlignment = Alignment.TopStart,
            ) {
                Column {
                    Text("Existing Subjects", fontSize = 16.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .animateContentSize()
                            .height(200.dp)
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(3.dp)
                            )
                            .padding(start = 16.dp, top = 10.dp, end = 16.dp, bottom = 10.dp),
                        contentAlignment = Alignment.TopStart,
                    ) {

                    }
                }
            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen.Content()
}

