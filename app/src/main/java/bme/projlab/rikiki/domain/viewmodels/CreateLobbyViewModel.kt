package bme.projlab.rikiki.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.domain.base.BaseLobbiesRepository
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateLobbyViewModel @Inject constructor(
    private var lobbiesRepository: BaseLobbiesRepository
): ViewModel() {

    private val _lobbyFlow = MutableStateFlow<LobbiesResponse<Lobby>?>(null)
    val lobbyFlow: MutableStateFlow<LobbiesResponse<Lobby>?> = _lobbyFlow

    fun createLobby(count: Int, code: String) = viewModelScope
        .launch{
            _lobbyFlow.value = LobbiesResponse.Loading()
            val result = lobbiesRepository.createLobby(count, code)
            _lobbyFlow.value = result
        }


}