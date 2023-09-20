package bme.projlab.rikiki.domain.base

import bme.projlab.rikiki.data.entities.Game
import bme.projlab.rikiki.domain.responses.GameResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import kotlinx.coroutines.flow.Flow

interface BaseGameRepository {
    fun getCard()
    fun getGame(owner: String?): Flow<ResourceResponse<Game>>
    fun addCards(game: Game)
    suspend fun placeBet(game: Game, bet: Int): GameResponse<Game>
    fun endRound(game: Game)
}