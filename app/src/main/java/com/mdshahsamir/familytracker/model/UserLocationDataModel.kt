package com.mdshahsamir.familytracker.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserLocationDataModel(  var latitude: Double,
                                   var longitude: Double)