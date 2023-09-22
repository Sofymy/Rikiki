package bme.projlab.rikiki.data.entities.game

import bme.projlab.rikiki.data.entities.cards.Card
import com.google.firebase.firestore.PropertyName

data class Leader(
    @get: PropertyName("player") @set: PropertyName("player") var player: String = "",
    @get: PropertyName("card") @set: PropertyName("card") var card: Card
) {
    constructor() : this("", Card.values().random())
}
