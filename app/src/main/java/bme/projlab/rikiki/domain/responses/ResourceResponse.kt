package bme.projlab.rikiki.domain.responses

sealed class ResourceResponse<T>(val data: T? = null, val errorMessage: String? = null) {
    class Success<T>(data: T?) : ResourceResponse<T>(data = data)
    class Error<T>(errorMessage: String, data: T? = null) : ResourceResponse<T>(
        data = data,
        errorMessage = errorMessage
    )
}