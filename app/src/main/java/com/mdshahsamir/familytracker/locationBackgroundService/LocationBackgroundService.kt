package com.mdshahsamir.familytracker.locationBackgroundService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationResult
import com.mdshahsamir.familytracker.RemoteDatabase.RemoteDatabase
import com.mdshahsamir.familytracker.data_models.UserLocationDataModel
import java.lang.Exception


class LocationBackgroundService : BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("OnReceive", "Wroking")

        if(intent != null){
            if (intent.action == ACTION_PROCESS_UPDATES) {
               val result  = LocationResult.extractResult(intent)
                if (result != null){
                    val location = result.lastLocation
                    try {
                        RemoteDatabase.updateLocationOnRemoteDatabase(location)
                       // Toast.makeText(context, location.toString(), Toast.LENGTH_SHORT).show()
                    }catch (ex : Exception){
                        RemoteDatabase.updateLocationOnRemoteDatabase(location)
                       // Toast.makeText(context, location.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private class Task(
            private val pendingResult: PendingResult,
            private val intent: Intent
    ) : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg params: String?): String {
            val sb = StringBuilder()
            sb.append("Action: ${intent.action}\n")
            sb.append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
            return toString().also { log ->
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish()
        }
    }


    companion object{
        const val ACTION_PROCESS_UPDATES = "com.mdshahsamir.familytracker.locationBackgroundService.UPDATE_LOCATION"
    }
}