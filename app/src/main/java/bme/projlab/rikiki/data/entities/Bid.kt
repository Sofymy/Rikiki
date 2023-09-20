package bme.projlab.rikiki.data.entities

import com.google.firebase.firestore.PropertyName

data class Bid(
    @get: PropertyName("player") @set: PropertyName("player") var player: String = "",
    @get: PropertyName("bid") @set: PropertyName("bid") var bid: Int = 0
){
    fun getBid(player: String): Int {
        return this.bid
    }
}