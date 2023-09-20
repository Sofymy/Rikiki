package bme.projlab.rikiki.domain.base

import bme.projlab.rikiki.data.entities.Game
import bme.projlab.rikiki.domain.responses.GameResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import kotlinx.coroutines.flow.Flow

interface BaseGameRepository {
    fun getGame(owner: String?): Flow<ResourceResponse<Game>>
    fun dealCards(game: Game)
    fun makeBid(game: Game, bid: Int)
    fun endRound(game: Game)
}