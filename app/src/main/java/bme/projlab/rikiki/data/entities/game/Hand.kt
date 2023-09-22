package bme.projlab.rikiki.data.entities.game

import bme.projlab.rikiki.data.entities.cards.Card
import com.google.firebase.firestore.PropertyName

data class Hand(
    @get: PropertyName("player") @set: PropertyName("player") var player: String = "",
    @get: PropertyName("cards") @set: PropertyName("cards") var cards: ArrayList<Card> = arrayListOf(),
    @get: PropertyName("points") @set: PropertyName("points") var points: Int = 0
)
