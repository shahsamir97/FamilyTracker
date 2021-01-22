package com.mdshahsamir.familytracker.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserLocationDataModel() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var userDisplayName = ""

    constructor(latitude: Double, longitude: Double, userDisplayName: String) : this() {
        this.latitude = latitude
        this.longitude = longitude
        this.userDisplayName = userDisplayName
    }

}

