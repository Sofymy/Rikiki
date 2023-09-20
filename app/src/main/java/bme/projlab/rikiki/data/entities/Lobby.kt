package bme.projlab.rikiki.data.entities

import bme.projlab.rikiki.data.utils.Status
import com.google.firebase.firestore.PropertyName

data class Lobby(
    @get: PropertyName("owner") @set: PropertyName("owner") var owner: String = "",
    @get: PropertyName("count") @set: PropertyName("count") var count: Int = 3,
    @get: PropertyName("code") @set: PropertyName("code") var code: Int = 100,
    @get: PropertyName("players") @set: PropertyName("players") var players: ArrayList<String> = arrayListOf(owner),
    @get: PropertyName("status") @set: PropertyName("status") var status: Status = Status.WAIT

    ){
    init {
        require(count in 3..7) { "Length must be between 3 and 7" }
        require(code.toString().length == 3) { "Code must be consist of 3 numbers" }
        require(players.size in 1..count) { "Players must be between 1 and $count" }
    }
}
