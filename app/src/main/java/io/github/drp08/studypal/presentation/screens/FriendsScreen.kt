package io.github.drp08.studypal.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import io.github.drp08.studypal.presentation.viewmodels.FriendsViewModel

object FriendsScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<FriendsViewModel>()
        var search by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SearchBar(
                query = search,
                onQueryChange = { search = it },
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(viewModel.friends) {
                    FriendItem(
                        name = it,
                        email = "${it.lowercase().replace(" ", "-")}@example.com",
                        onRemove = viewModel::removeFriend,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
            AddFriend(onAdd = { viewModel.addFriend(it) })
        }
    }

    @Composable
    fun FriendItem(
        name: String,
        email: String,
        onRemove: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }

                RemoveButton(onRemove = { onRemove(email) })
            }
        }
    }

    @Composable
    fun SearchBar(
        query: String,
        onQueryChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = modifier,
            placeholder = { Text(text = "Search through your friend list") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search friends"
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }

    @Composable
    fun AddFriend(modifier: Modifier = Modifier, onAdd: (String) -> Unit) {
        var query by remember { mutableStateOf("") }

        Row(
            modifier = modifier
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(1f)
            )

            SmallFloatingActionButton(
                onClick = { onAdd(query) },
                containerColor = Color.Green,
                contentColor = Color.White,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add friend"
                )
            }
        }
    }

    @Composable
    fun RemoveButton(modifier: Modifier = Modifier, onRemove: () -> Unit) {
        SmallFloatingActionButton(
            onClick = onRemove,
            containerColor = Color.Red,
            contentColor = Color.White,
            modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove friend"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFriendItem() {
    FriendsScreen.FriendItem(
        name = "Nishant",
        email = "nishant@example.com",
        onRemove = {},
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {
    FriendsScreen.SearchBar(
        query = "",
        onQueryChange = {},
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddFriend() {
    FriendsScreen.AddFriend(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth(),
        onAdd = {}
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewContent(modifier: Modifier = Modifier) {
    FriendsScreen.Content()
}