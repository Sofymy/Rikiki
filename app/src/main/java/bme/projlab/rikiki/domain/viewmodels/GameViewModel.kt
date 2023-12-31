package bme.projlab.rikiki.domain.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bme.projlab.rikiki.data.entities.game.Game
import bme.projlab.rikiki.domain.base.BaseGameRepository
import bme.projlab.rikiki.domain.responses.GameResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    fun dealCards(game: Game) {
        gameRepository.dealCards(game)
    }

    fun makeBid(game: Game, bid: Int){
        gameRepository.makeBid(game, bid)
    }
}