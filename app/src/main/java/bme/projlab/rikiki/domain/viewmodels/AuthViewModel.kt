package bme.projlab.rikiki.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bme.projlab.rikiki.domain.base.BaseAuthRepository
import bme.projlab.rikiki.domain.responses.LoginResponse
import bme.projlab.rikiki.domain.responses.SignupResponse
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: BaseAuthRepository
): ViewModel(){

    private val _signupFlow = MutableStateFlow<SignupResponse<FirebaseUser>?>(null)
    val signupFlow: StateFlow<SignupResponse<FirebaseUser>?> = _signupFlow

    private val _loginFlow = MutableStateFlow<LoginResponse<FirebaseUser>?>(null)
    val loginFlow: StateFlow<LoginResponse<FirebaseUser>?> = _loginFlow

    fun createUserWithEmailAndPassword(username: String,
                                       email: String,
                                       password: String,
                                       passwordAgain: String) = viewModelScope
        .launch {
            when{
                email.isEmpty() -> {}
                password.isEmpty() -> {}
                passwordAgain.isEmpty() -> {}
                password != passwordAgain -> {}
                else -> {
                    _signupFlow.value = SignupResponse.Loading
                    val response = authRepository.createUserWithEmailAndPassword(username, email, password)
                    _signupFlow.value = response
                }
            }
    }

    fun signInWithEmailAndPassword(email: String,
                                   password: String) = viewModelScope
        .launch {
            when{
                email.isEmpty() -> {}
                password.isEmpty() -> {}
                else -> {
                    _loginFlow.value = LoginResponse.Loading
                    val response = authRepository.signInWithEmailAndPassword(email, password)
                    _loginFlow.value = response
                }
            }
    }

    val currentUser  = authRepository.currentUser

    fun signOut() {
        authRepository.signOut()
    }
}