package bme.projlab.rikiki.domain.responses

sealed class GameResponse<T>(val data: T? = null, val errorMessage: String? = null) {
    class Error<T>(errorMessage: String, data: T? = null) : GameResponse<T>(
        data = data,
        errorMessage = errorMessage
    )

    class Loading<T> : GameResponse<T>()
    class Betting<T>(data: T) : GameResponse<T>(data = data)
    class Ending<T>(data: T) : GameResponse<T>(data = data)
    class Gameing<T> : GameResponse<T>() {

    }
}