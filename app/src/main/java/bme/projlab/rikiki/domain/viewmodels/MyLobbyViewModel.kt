package bme.projlab.rikiki.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.domain.base.BaseLobbiesRepository
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLobbyViewModel @Inject constructor(
    private var lobbiesRepository: BaseLobbiesRepository
):ViewModel() {
    private var _lobby = mutableStateOf(Lobby())
    val lobby: State<Lobby> = _lobby

    private val _lobbyFlow = MutableStateFlow<LobbiesResponse<Lobby>?>(null)
    val lobbyFlow: MutableStateFlow<LobbiesResponse<Lobby>?> = _lobbyFlow

    fun getLobby() {
        lobbiesRepository.getLobby()
            .onEach { resource ->
                when (resource) {
                    is ResourceResponse.Error -> {
                    }

                    is ResourceResponse.Success -> {
                        if(resource.data!=null)
                            _lobby.value = resource.data
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }

    fun deleteLobby(lobby: Lobby = _lobby.value) = viewModelScope
        .launch {
            _lobbyFlow.value = LobbiesResponse.Loading(lobby)
            val response = lobbiesRepository.deleteLobby()
            _lobbyFlow.value = response
        }

    fun startGame(lobby: Lobby) = viewModelScope
        .launch {
            _lobbyFlow.value = LobbiesResponse.Loading()
            val response = lobbiesRepository.startGame(lobby)
            _lobbyFlow.value = response
        }

    fun onPauseLobby() {
        lobbiesRepository.onPauseLobby()
    }


}
