package bme.projlab.rikiki.domain.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bme.projlab.rikiki.data.entities.Game
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.data.utils.Status
import bme.projlab.rikiki.domain.base.BaseGameRepository
import bme.projlab.rikiki.domain.responses.GameResponse
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: BaseGameRepository
): ViewModel() {

    private var _game = mutableStateOf(Game())
    val game: State<Game> = _game

    private val _gameFlow = MutableStateFlow<GameResponse<Game>?>(null)
    val gameFlow: MutableStateFlow<GameResponse<Game>?> = _gameFlow

    fun getGame(owner: String?) {
        if (owner != null) {
            gameRepository.getGame(owner)
                .onEach { resource ->
                    when (resource) {
                        is ResourceResponse.Success -> {
                            if(resource.data!=null){
                                _game.value = resource.data
                            }
                        }

                        else -> {}
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun addCards(game: Game) {
        gameRepository.addCards(game)
    }

    fun placeBet(game: Game, bet: Int) = viewModelScope
        .launch {
            _gameFlow.value = GameResponse.Loading()
            val response = gameRepository.placeBet(game, bet)
            _gameFlow.value = response
        }

    fun endRound(game: Game) = viewModelScope
        .launch {
            gameRepository.endRound(game)
        }
}