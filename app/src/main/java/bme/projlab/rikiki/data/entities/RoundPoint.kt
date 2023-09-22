package bme.projlab.rikiki.data.entities

import com.google.firebase.firestore.PropertyName

data class RoundPoint(
    @get: PropertyName("round") @set: PropertyName("round") var round: Int = 1,
    @get: PropertyName("point") @set: PropertyName("point") var point: Int = 0
)
