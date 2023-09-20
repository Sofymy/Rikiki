package bme.projlab.rikiki.domain.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.data.utils.Status
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
class LobbyViewModel @Inject constructor(
    private val lobbiesRepository: BaseLobbiesRepository
): ViewModel(){

    private var _lobby = mutableStateOf(Lobby())
    val lobby: State<Lobby> = _lobby

    private val _lobbyFlow = MutableStateFlow<LobbiesResponse<Lobby>?>(null)
    val lobbyFlow: MutableStateFlow<LobbiesResponse<Lobby>?> = _lobbyFlow

    fun getLobby(owner: String?) {
        if (owner != null) {
            lobbiesRepository.getLobby(owner)
                .onEach { resource ->
                    when (resource) {
                        is ResourceResponse.Success -> {
                            if(resource.data!=null){
                                _lobby.value = resource.data
                                when(_lobby.value.status){
                                    Status.WAIT -> _lobbyFlow.value = LobbiesResponse.Loading()
                                    Status.DELETE -> _lobbyFlow.value = LobbiesResponse.Deleting()
                                    Status.START -> _lobbyFlow.value = LobbiesResponse.Starting()
                                }
                            }
                        }

                        else -> {}
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun leaveLobby(lobby: Lobby = _lobby.value) = viewModelScope
        .launch {
            _lobbyFlow.value = LobbiesResponse.Loading(lobby)
            val response = lobbiesRepository.leaveLobby(lobby)
            _lobbyFlow.value = response
        }
}
