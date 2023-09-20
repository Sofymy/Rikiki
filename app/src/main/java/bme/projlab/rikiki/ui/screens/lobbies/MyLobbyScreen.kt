package bme.projlab.rikiki.ui.screens.lobbies

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.data.utils.ComposableLifecycle
import bme.projlab.rikiki.data.utils.Status
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.viewmodels.MyLobbyViewModel
import kotlinx.coroutines.launch

@Composable
fun MyLobbyScreen(
    myLobbyViewModel: MyLobbyViewModel = hiltViewModel(),
    navigateToLobbies: () -> Unit,
    navigateToGame: (String) -> Unit
){
    myLobbyViewModel.getLobby()
    val lobby = myLobbyViewModel.lobby.value
    val lobbyResponse = myLobbyViewModel.lobbyFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(lobbyResponse.value){
        when(lobbyResponse.value){
            is LobbiesResponse.Loading -> scope.launch {
                snackbarHostState.showSnackbar("Load message")
            }
            is LobbiesResponse.Starting -> scope.launch {
                navigateToGame(lobby.owner)
            }
            is LobbiesResponse.Deleting -> scope.launch {
                if(lobby.status != Status.START){
                    navigateToLobbies()
                }
            }
            is LobbiesResponse.Error -> {
                (lobbyResponse.value as LobbiesResponse.Error<Lobby>).errorMessage?.let { Log.d("mibaj", it) }
            }
            else -> {}
        }
    }
    Column {
        Text(text = "My lobby")
        LazyColumn {
            items(lobby.players) { player ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .border(2.dp, Color.White)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(player)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        var isEnabled by rememberSaveable {
            mutableStateOf(false)
        }
        Text(text = "Your room")
        Text(text = "${lobby.code}")
        Text(text = "Room for ${lobby.count} players")
        Text(text = "${lobby.count-lobby.players.size} players needed")
        isEnabled = lobby.players.size >= 3
        Button(
            enabled = isEnabled,
            onClick = {
                myLobbyViewModel.startGame(lobby = lobby)
            }
        ) {
            if(isEnabled) Text("Start game")
            else Text("Waiting for at least ${3-lobby.players.size} players")
        }
        Button(onClick = {
            myLobbyViewModel.deleteLobby()
        }) {
            Text("Delete")
        }
    }

    ComposableLifecycle { owner, event ->
        if (event == Lifecycle.Event.ON_PAUSE) {
            myLobbyViewModel.onPauseLobby()
        }
    }

}

