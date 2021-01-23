package com.mdshahsamir.familytracker.RemoteDatabase

import android.location.Location
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mdshahsamir.familytracker.data_models.UserLocationDataModel

class RemoteDatabase {




    companion object{
        private lateinit var database: DatabaseReference
        private lateinit var auth: FirebaseAuth

        fun updateLocationOnRemoteDatabase(location: Location) {
            auth = FirebaseAuth.getInstance()
            database = Firebase.database.reference
            database.child("location").child(auth.currentUser?.uid.toString())
                    .setValue(UserLocationDataModel(userId = auth.currentUser?.uid!!,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            userDisplayName = auth.currentUser?.displayName ?: "Anonymous"))
        }
    }
}