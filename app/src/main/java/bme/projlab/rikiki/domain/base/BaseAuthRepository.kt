package bme.projlab.rikiki.domain.base

import bme.projlab.rikiki.domain.responses.LoginResponse
import bme.projlab.rikiki.domain.responses.SignupResponse
import com.google.firebase.auth.FirebaseUser

interface BaseAuthRepository {
    suspend fun createUserWithEmailAndPassword(username: String, email: String, password: String): SignupResponse<FirebaseUser>
    suspend fun signInWithEmailAndPassword(email: String, password: String): LoginResponse<FirebaseUser>
    val currentUser: FirebaseUser?
    fun signOut()
}
