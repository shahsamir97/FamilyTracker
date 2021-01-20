package com.mdshahsamir.familytracker.locationBackgroundService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationResult
import java.lang.Exception


class LocationBackgroundService : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("OnReceive", "wroking")
        if(intent != null){
            if (intent.action == ACTION_PROCESS_UPDATES) {
               val result  = LocationResult.extractResult(intent)
                if (result != null){
                    val location = result.lastLocation
                    try {

                        //Toast.makeText(context, location.toString(), Toast.LENGTH_SHORT).show()
                    }catch (ex : Exception){
                        //Toast.makeText(context, location.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }

    companion object{
        const val ACTION_PROCESS_UPDATES = "com.mdshahsamir.familytracker.locationBackgroundService.UPDATE_LOCATION"
    }
}