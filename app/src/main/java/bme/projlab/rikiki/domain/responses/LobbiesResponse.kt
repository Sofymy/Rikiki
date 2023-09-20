package bme.projlab.rikiki.domain.responses

sealed class LobbiesResponse<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loading<T>(data: T? = null) : LobbiesResponse<T>(data = data)
    class Joining<T>(data: T) : LobbiesResponse<T>(data = data)
    class Leaving<T>(data: T) : LobbiesResponse<T>(data = data)
    class Creating<T> : LobbiesResponse<T>()
    class Deleting<T> : LobbiesResponse<T>()
    class Starting<T> : LobbiesResponse<T>()
    class Error<T>(errorMessage: String, data: T? = null) : LobbiesResponse<T>(
        data = data,
        errorMessage = errorMessage
    )
}