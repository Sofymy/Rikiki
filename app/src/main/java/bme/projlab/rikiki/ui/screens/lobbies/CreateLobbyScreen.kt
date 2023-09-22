package bme.projlab.rikiki.ui.screens.lobbies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.viewmodels.CreateLobbyViewModel
import com.chargemap.compose.numberpicker.NumberPicker
import kotlinx.coroutines.launch

@Composable
fun CreateLobbyScreen(
    viewModel: CreateLobbyViewModel = hiltViewModel(),
    navigateToMyLobby: () -> Unit
){
    val lobbyResponse = viewModel.lobbyFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(lobbyResponse.value){
        when(lobbyResponse.value){
            is LobbiesResponse.Creating -> scope.launch {
                navigateToMyLobby()
            }
            is LobbiesResponse.Loading -> scope.launch {
                snackbarHostState.showSnackbar("Load message")
            }
            else -> {}
        }
    }

    var pickerValueFirst by remember { mutableIntStateOf(0) }
    var pickerValueSecond by remember { mutableIntStateOf(0) }
    var pickerValueThird by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create lobby",
            modifier = Modifier)
        Spacer(modifier = Modifier.height(20.dp))
        Row{
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
        }

        Text("Players")

        val radioOptions = listOf(3,4,5,6,7)
        val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(radioOptions[1] ) }
        Row {
            radioOptions.forEach { players ->
                Row(
                    Modifier
                        .selectable(
                            selected = (players == selectedOption),
                            onClick = {
                                onOptionSelected(players)
                            }
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    RadioButton(
                        selected = (players == selectedOption),
                        onClick = { onOptionSelected(players) }
                    )
                    Text(
                        text = players.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

            }

        }

        Button(
            onClick = { viewModel.createLobby(selectedOption, concatValues(pickerValueFirst, pickerValueSecond, pickerValueThird)) }
        ){
            Text("Create")
        }

    }
}