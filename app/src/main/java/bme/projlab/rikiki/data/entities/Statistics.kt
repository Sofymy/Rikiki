package bme.projlab.rikiki.data.entities

import com.google.firebase.firestore.PropertyName

data class Statistics(
    @get: PropertyName("avgBid") @set: PropertyName("avgBid") var avgBid: Double = 0.0,
    @get: PropertyName("points") @set: PropertyName("points") var points: Int = 0,
    @get: PropertyName("games") @set: PropertyName("games") var games: Int = 0
)
