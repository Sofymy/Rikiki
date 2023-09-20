package bme.projlab.rikiki.data.entities

import com.google.firebase.firestore.PropertyName

data class Hand(
    @get: PropertyName("player") @set: PropertyName("player") var player: String = "",
    @get: PropertyName("cards") @set: PropertyName("cards") var cards: ArrayList<Card> = arrayListOf()
){
    fun getCards(player: String): ArrayList<Card> {
        return this.cards
    }
}
