package bme.projlab.rikiki.data.entities.game

import com.google.firebase.firestore.PropertyName

data class Bid(
    @get: PropertyName("player") @set: PropertyName("player") var player: String = "",
    @get: PropertyName("bid") @set: PropertyName("bid") var bid: Int = 0,
    @get: PropertyName("took") @set: PropertyName("took") var took: Int = 0
)