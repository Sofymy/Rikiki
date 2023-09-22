package bme.projlab.rikiki.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.domain.base.BaseLobbiesRepository
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.responses.LoginResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbiesViewModel @Inject constructor(
    private val lobbiesRepository: BaseLobbiesRepository
): ViewModel(){

    private var _lobbies = mutableStateOf<List<Lobby>>(emptyList())
    val lobbies: State<List<Lobby>> = _lobbies

    private val _lobbiesFlow = MutableStateFlow<LobbiesResponse<Lobby>?>(null)
    val lobbiesFlow: MutableStateFlow<LobbiesResponse<Lobby>?> = _lobbiesFlow

    init {
        lobbiesRepository.getLobbies()
            .onEach { resource ->
                when (resource) {
                    is ResourceResponse.Error -> {
                        /* TODO: Handle the error */
                    }

                    is ResourceResponse.Success -> {
                        _lobbies.value = resource.data!!

                    }
                }
            }.launchIn(viewModelScope)
    }

    fun joinLobby(lobby: Lobby, code: String) = viewModelScope
        .launch {
            _lobbiesFlow.value = LobbiesResponse.Loading(lobby)
            val response = lobbiesRepository.joinLobby(lobby, code)
            _lobbiesFlow.value = response
        }


}