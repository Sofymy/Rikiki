package bme.projlab.rikiki.data.repositories

import android.util.Log
import bme.projlab.rikiki.data.entities.Game
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.data.utils.Status
import bme.projlab.rikiki.domain.base.BaseLobbiesRepository
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LobbiesRepository: BaseLobbiesRepository {

    private val lobbiesRef = Firebase.firestore.collection("lobbies")

    override fun getLobbies(): Flow<ResourceResponse<List<Lobby>>> = callbackFlow {
        val listener =  lobbiesRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val lobbies= snapshot.toObjects(Lobby::class.java)
                trySend(ResourceResponse.Success(data = lobbies)).isSuccess
            }
        }

        awaitClose {
            listener.remove()
            close()
        }
    }

    override fun getLobby(owner: String): Flow<ResourceResponse<Lobby>> = callbackFlow {
        val docRef = lobbiesRef.document(owner)
        val listener = docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val lobby = snapshot.toObject(Lobby::class.java)
                    if (lobby != null) {
                        trySend(ResourceResponse.Success(data = lobby)).isSuccess
                    }
                }
            }
        awaitClose{
            listener.remove()
            close()
        }
    }

    override fun getLobby(): Flow<ResourceResponse<Lobby>> = callbackFlow {
        val docRef = Firebase.auth.currentUser?.displayName?.let {
            lobbiesRef.document(
                it
            )
        }
        val listener = docRef?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val lobby = snapshot.toObject(Lobby::class.java)
                trySend(ResourceResponse.Success(data = lobby)).isSuccess
            }
        }
        awaitClose {
            listener?.remove()
            close()
        }
    }

    override suspend fun createLobby(count: Int, code: String): LobbiesResponse<Lobby> {
        return try{
            val username = Firebase.auth.currentUser?.displayName
            val lobbyData = username?.let {
                Lobby(owner = it,
                    count= count,
                    code = code)
            }
            if (lobbyData != null) {
                lobbiesRef
                    .document(username)
                    .set(lobbyData)
                    .await()
            }
            LobbiesResponse.Creating()
        }catch (e: Exception){
            LobbiesResponse.Error(e.message.toString())
        }
    }

    override fun onPauseLobby() {
        try{
            Firebase.auth.currentUser?.displayName?.let {
                lobbiesRef
                    .document(it)
                    .delete()
            }
        }catch (e: Exception){
            //LobbiesResponse.Error(e.message.toString())
        }
    }

    override suspend fun deleteLobby(): LobbiesResponse<Lobby> {
        return try{
            Firebase.auth.currentUser?.displayName?.let {
                lobbiesRef
                    .document(it)
                    .update("status", Status.DELETE.toString())
                    .await()
            }
            Firebase.auth.currentUser?.displayName?.let {
                lobbiesRef
                    .document(it)
                    .delete()
                    .await()
            }
            LobbiesResponse.Deleting()
        }catch (e: Exception){
            LobbiesResponse.Error(e.message.toString())
        }
    }


    override suspend fun startGame(lobby: Lobby): LobbiesResponse<Lobby> {
        return try {
            Firebase.auth.currentUser?.displayName?.let {
                lobbiesRef
                    .document(it)
                    .update("status", Status.START.toString())
                    .await()
            }
            val db = Firebase.firestore
            val username = Firebase.auth.currentUser?.displayName
            val gameData = username?.let {
                Game(
                    owner = it,
                    players = lobby.players,
                )
            }
            if (username != null && gameData != null) {
                db.collection("games")
                    .document(username)
                    .set(gameData)
                    .await()
            }
            LobbiesResponse.Starting()
        }catch (e: Exception) {
            LobbiesResponse.Error(e.message.toString())
        }
    }

    override suspend fun joinLobby(lobby: Lobby, code: String): LobbiesResponse<Lobby> {
        return try {
            val correctCode = lobbiesRef.document(lobby.owner).get().await().toObject(Lobby::class.java)
            if (correctCode != null) {
                if(code != correctCode.code){
                    return LobbiesResponse.Error("", lobby)
                }
            }
            lobbiesRef
                .document(lobby.owner)
                .update("players", FieldValue.arrayUnion(Firebase.auth.currentUser?.displayName ?: "n/a"))
                .await()
            LobbiesResponse.Joining(lobby)
        }catch (e: Exception) {
            LobbiesResponse.Error(e.message.toString())
        }
      }

    override suspend fun leaveLobby(lobby: Lobby): LobbiesResponse<Lobby> {
        return try{
            lobbiesRef
                .document(lobby.owner)
                .update("players", FieldValue.arrayRemove(Firebase.auth.currentUser?.displayName ?: "n/a"))
                .await()
            LobbiesResponse.Leaving(lobby)
        }catch (e: Exception){
            LobbiesResponse.Error(e.message.toString())
        }
    }
}