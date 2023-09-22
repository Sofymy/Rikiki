package bme.projlab.rikiki.data.entities

import bme.projlab.rikiki.data.utils.Status
import com.google.firebase.firestore.PropertyName

data class User(
    @get: PropertyName("creationTime") @set: PropertyName("creationTime") var creationTime: String = "",
    @get: PropertyName("icon") @set: PropertyName("icon") var icon: String = "",
    @get: PropertyName("uid") @set: PropertyName("uid") var uid: String = "",
    @get: PropertyName("username") @set: PropertyName("username") var username: String = "",
    @get: PropertyName("statistics") @set: PropertyName("statistics") var statistics: Statistics = Statistics()
)