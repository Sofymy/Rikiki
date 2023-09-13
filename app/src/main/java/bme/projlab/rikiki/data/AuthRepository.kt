package bme.projlab.rikiki.data

import bme.projlab.rikiki.domain.base.BaseAuthRepository
import bme.projlab.rikiki.domain.base.BaseAuthenticator
import bme.projlab.rikiki.domain.responses.LoginResponse
import bme.projlab.rikiki.domain.responses.SignupResponse
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authenticator: BaseAuthenticator
): BaseAuthRepository {
    override suspend fun createUserWithEmailAndPassword(username: String,
                                                        email: String,
                                                        password: String): SignupResponse<FirebaseUser> {
        return authenticator.createUserWithEmailAndPassword(username, email, password)
    }

    override suspend fun signInWithEmailAndPassword(email: String,
                                                    password: String): LoginResponse<FirebaseUser> {
        return authenticator.signInWithEmailAndPassword(email, password)
    }

    override val currentUser: FirebaseUser? = authenticator.currentUser

    override fun signOut() = authenticator.signOut()

}