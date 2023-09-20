package bme.projlab.rikiki.data.utils

import bme.projlab.rikiki.domain.base.BaseAuthenticator
import bme.projlab.rikiki.domain.responses.LoginResponse
import bme.projlab.rikiki.domain.responses.SignupResponse
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticator: BaseAuthenticator {
    override suspend fun createUserWithEmailAndPassword(username: String,
                                                        email: String,
                                                        password: String): SignupResponse<FirebaseUser> {
        return try {
            val result = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            val db = Firebase.firestore
            val user = result.user
            user?.updateProfile(UserProfileChangeRequest
                .Builder()
                .setDisplayName(username)
                .build())
                ?.await()
            user?.let {
                db.collection("users").document(it.uid)
                .update("username", username)
                .await()
            }
            SignupResponse.Success(result.user!!)
        } catch (e: Exception) {
            SignupResponse.Failure(e)
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String,
                                                    password: String): LoginResponse<FirebaseUser> {
        return try {
            val result = Firebase.auth.signInWithEmailAndPassword(email, password).await()
            LoginResponse.Success(result.user!!)
        } catch (e: Exception) {
            LoginResponse.Failure(e)
        }
    }

    override val currentUser: FirebaseUser? = Firebase.auth.currentUser

    override fun signOut() = Firebase.auth.signOut()

}