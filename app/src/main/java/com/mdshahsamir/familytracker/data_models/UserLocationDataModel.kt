package com.mdshahsamir.familytracker.data_models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserLocationDataModel() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var userDisplayName = ""
    var userId = ""

    constructor(userId: String, latitude: Double, longitude: Double, userDisplayName: String) : this() {
        this.latitude = latitude
        this.longitude = longitude
        this.userDisplayName = userDisplayName
        this.userId = userId
    }

}

