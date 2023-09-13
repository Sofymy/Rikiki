package bme.projlab.rikiki.domain.responses

sealed class SignupResponse<out R> {
    data class Success<out R>(val result: R) : SignupResponse<R>()
    data class Failure(val exception: Exception) : SignupResponse<Nothing>()
    object Loading : SignupResponse<Nothing>()
}