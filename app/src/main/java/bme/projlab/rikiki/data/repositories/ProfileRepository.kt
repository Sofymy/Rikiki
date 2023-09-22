package bme.projlab.rikiki.data.repositories

import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.data.entities.User
import bme.projlab.rikiki.domain.base.BaseProfileRepository
import bme.projlab.rikiki.domain.responses.ResourceResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ProfileRepository: BaseProfileRepository {

    private val usersRef = Firebase.firestore.collection("users")

    override fun getProfile(): Flow<ResourceResponse<User>> = callbackFlow {
        val userUid = Firebase.auth.currentUser?.uid
        val docRef = userUid?.let {
            usersRef.document(it)
        }
        val listener = docRef?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    trySend(ResourceResponse.Success(data = user)).isSuccess
                }
            }
        }
        awaitClose{
            listener?.remove()
            close()
        }
    }
}