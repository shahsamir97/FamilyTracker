package com.mdshahsamir.familytracker.locationBackgroundService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mdshahsamir.familytracker.data_models.UserLocationDataModel
import java.lang.Exception


class LocationBackgroundService : BroadcastReceiver() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("OnReceive", "Wroking")
        auth = FirebaseAuth.getInstance()
        if(intent != null){
            if (intent.action == ACTION_PROCESS_UPDATES) {
               val result  = LocationResult.extractResult(intent)
                if (result != null){
                    val location = result.lastLocation
                    try {
                        updateLocationOnRemoteDatabase(location)
                        Toast.makeText(context, location.toString(), Toast.LENGTH_SHORT).show()
                    }catch (ex : Exception){
                        updateLocationOnRemoteDatabase(location)
                        Toast.makeText(context, location.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateLocationOnRemoteDatabase(location: Location){
        database = Firebase.database.reference
        database.child("location").child(auth.currentUser?.uid.toString())
                .setValue(UserLocationDataModel(userId = auth.currentUser?.uid!!,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        userDisplayName = auth.currentUser?.displayName?: "Anonymous"))
    }

    companion object{
        const val ACTION_PROCESS_UPDATES = "com.mdshahsamir.familytracker.locationBackgroundService.UPDATE_LOCATION"
    }
}