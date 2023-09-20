package bme.projlab.rikiki.ui.screens.lobbies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.viewmodels.LobbiesViewModel
import com.chargemap.compose.numberpicker.NumberPicker
import kotlinx.coroutines.launch

@Composable
fun LobbiesScreen(
    lobbyViewModel: LobbiesViewModel = hiltViewModel(),
    navigateToCreateLobby: () -> Unit,
    navigateToLobby: (String) -> Unit,
    navigateToGame: () -> Unit
){
    val lobbiesResponse = lobbyViewModel.lobbiesFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(lobbiesResponse.value){
        when(lobbiesResponse.value){
            is LobbiesResponse.Error -> scope.launch {
                snackbarHostState.showSnackbar("Error message")
            }
            is LobbiesResponse.Joining -> scope.launch {
                (lobbiesResponse.value as LobbiesResponse.Joining<Lobby>).data?.let {
                    navigateToLobby(
                        it.owner)
                }
            }
            is LobbiesResponse.Loading -> scope.launch {
                snackbarHostState.showSnackbar("LOad message", duration = SnackbarDuration.Short)
            }
            else -> {}
        }
    }
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.testTag("responseSnackbar")
        )
        Button(
            onClick = { navigateToCreateLobby() }
        ){
            Text("Create lobby")
        }
        LazyColumn {
            items(lobbyViewModel.lobbies.value) { lobby ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .border(2.dp, Color.White)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LobbyItem(lobbyViewModel, lobby)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun LobbyItem(lobbyViewModel: LobbiesViewModel, lobby: Lobby){
    Text(text = lobby.owner)
    Text(text = "Room for ${lobby.count} players")
    Text(text = "${lobby.count-lobby.players.size} players needed")
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        var pickerValueFirst by remember { mutableIntStateOf(1) }
        var pickerValueSecond by remember { mutableIntStateOf(0) }
        var pickerValueThird by remember { mutableIntStateOf(0) }
        NumberPicker(
            value = pickerValueFirst,
            range = 1..9,
            onValueChange = {
                pickerValueFirst = it
            }
        )
        NumberPicker(
            value = pickerValueSecond,
            range = 0..9,
            onValueChange = {
                pickerValueSecond = it
            }
        )
        NumberPicker(
            value = pickerValueThird,
            range = 0..9,
            onValueChange = {
                pickerValueThird = it
            }
        )

        Button(onClick = {
            lobbyViewModel.joinLobby(lobby, concatValues(pickerValueFirst, pickerValueSecond, pickerValueThird))
        }) {
            Text("Join")
        }
    }
}

fun concatValues(pickerValueFirst: Int, pickerValueSecond: Int, pickerValueThird: Int): String {
    return pickerValueFirst.toString()+pickerValueSecond.toString()+pickerValueThird.toString()
}