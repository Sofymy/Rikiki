package bme.projlab.rikiki.domain.responses

sealed class LoginResponse<out R> {
    data class Success<out R>(val result: R) : LoginResponse<R>()
    data class Failure(val exception: Exception) : LoginResponse<Nothing>()
    object Loading : LoginResponse<Nothing>()
}