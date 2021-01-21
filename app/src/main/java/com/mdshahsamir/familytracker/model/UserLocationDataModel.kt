package com.mdshahsamir.familytracker.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserLocationDataModel() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    constructor(latitude: Double, longitude: Double) : this() {
        this.latitude = latitude
        this.longitude = longitude
    }

}

