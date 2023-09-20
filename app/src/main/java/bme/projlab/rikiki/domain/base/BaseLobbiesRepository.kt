package bme.projlab.rikiki.domain.base

import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import kotlinx.coroutines.flow.Flow
interface BaseLobbiesRepository {
    suspend fun createLobby(count: Int, code: String): LobbiesResponse<Lobby>
    fun getLobbies(): Flow<ResourceResponse<List<Lobby>>>
    suspend fun joinLobby(lobby: Lobby, code: String):  LobbiesResponse<Lobby>
    suspend fun leaveLobby(lobby: Lobby): LobbiesResponse<Lobby>
    fun getLobby(owner: String): Flow<ResourceResponse<Lobby>>
    fun getLobby(): Flow<ResourceResponse<Lobby>>
    suspend fun deleteLobby(): LobbiesResponse<Lobby>
    suspend fun startGame(lobby: Lobby): LobbiesResponse<Lobby>?
    fun onPauseLobby()
}