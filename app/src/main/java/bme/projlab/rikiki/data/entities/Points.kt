package bme.projlab.rikiki.data.entities

import com.google.firebase.firestore.PropertyName

data class Points(
    @get: PropertyName("player") @set: PropertyName("player") var player: String = "",
    @get: PropertyName("allPoints") @set: PropertyName("allPoints") var allPoints: Int = 0,
    @get: PropertyName("roundPoints") @set: PropertyName("roundPoints") var roundPoints: ArrayList<RoundPoint> = arrayListOf()
)
