package bme.projlab.rikiki.ui.screens.lobbies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.ui.core.Text
import bme.projlab.rikiki.domain.viewmodels.LobbiesViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun LobbiesScreen(
    viewModel: LobbiesViewModel = hiltViewModel(),
    navigateToCreateLobby: () -> Unit,
    navigateToGame: () -> Unit
){
    Column() {
        ClickableText(
            text = AnnotatedString("Create lobby") ,
            onClick = { navigateToCreateLobby() }
        )
        ClickableText(
            text = AnnotatedString("Game") ,
            onClick = { navigateToGame() }
        )
    }
}