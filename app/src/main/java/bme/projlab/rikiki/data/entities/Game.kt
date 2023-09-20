package bme.projlab.rikiki.data.entities

import com.google.firebase.firestore.PropertyName

data class Game(
    @get: PropertyName("owner") @set: PropertyName("owner") var owner: String = "",
    @get: PropertyName("round") @set: PropertyName("round") var round: Int = 1,
    @get: PropertyName("players") @set: PropertyName("players") var players: ArrayList<String> = arrayListOf(owner),
    @get: PropertyName("trump") @set: PropertyName("trump") var trump: Card = Card.values().random(),
    @get: PropertyName("hands") @set: PropertyName("hands") var hands: ArrayList<Hand> = arrayListOf(),
    @get: PropertyName("bids") @set: PropertyName("bids") var bids: ArrayList<Bid> = arrayListOf()
)
