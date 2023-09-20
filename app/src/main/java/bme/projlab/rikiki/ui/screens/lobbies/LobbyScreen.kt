package bme.projlab.rikiki.ui.screens.lobbies

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import bme.projlab.rikiki.data.utils.ComposableLifecycle
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.viewmodels.LobbyViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LobbyScreen(
    owner: String?,
    lobbyViewModel: LobbyViewModel = hiltViewModel(),
    navigateToLobbies: () -> Unit,
    navigateToGame: (String) -> Unit
) {
    val lobby = lobbyViewModel.lobby.value
    val lobbyResponse = lobbyViewModel.lobbyFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(lobbyResponse.value){
        lobbyViewModel.getLobby(owner)
        when(lobbyResponse.value){
            is LobbiesResponse.Error -> scope.launch {
                snackbarHostState.showSnackbar("Error message")
            }
            is LobbiesResponse.Starting -> scope.launch {
                navigateToGame(lobby.owner)
            }
            is LobbiesResponse.Leaving -> {
                navigateToLobbies()
            }
            is LobbiesResponse.Deleting -> scope.launch {
                navigateToLobbies()
            }
            is LobbiesResponse.Loading -> scope.launch {
                snackbarHostState.showSnackbar("Load message")
            }
            else -> {}
        }
    }

    Column {
        Text(text = lobby.owner)
        LazyColumn {
            items(lobby.players) { player ->
                var visible by remember { mutableStateOf(true) }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    val background by transition.animateColor(label = "color") { state ->
                        if (state == EnterExitState.Visible) Color.Blue else Color.Gray
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(background)
                            .border(2.dp, Color.White)
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(player, modifier = Modifier.clickable {
                            visible = !visible
                        })
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        Button(onClick = {
            lobbyViewModel.leaveLobby(lobby)
        }) {
            Text("Leave")
        }
    }

    ComposableLifecycle { _, event ->
        if (event == Lifecycle.Event.ON_PAUSE) {
            lobbyViewModel.leaveLobby()
        }
    }
}



