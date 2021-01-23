package com.mdshahsamir.familytracker.map

import com.mdshahsamir.familytracker.R
import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mdshahsamir.familytracker.databinding.FragmentMapsBinding
import com.mdshahsamir.familytracker.locationBackgroundService.LocationBackgroundService
import com.mdshahsamir.familytracker.data_models.UserLocationDataModel


class MapsFragment : Fragment(),OnMapReadyCallback, LocationListener {

    private lateinit var binding : FragmentMapsBinding

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var mapsViewModel: MapsViewModel
    private var connectedMembers: ArrayList<String> = ArrayList()

    private lateinit var database: DatabaseReference

    val FOREGROUND_LOCATION_REQUEST = 2
    val FOREGROUND_AND_BACKGROUND_LOCATION_REQUEST_CODE = 33
    val ACCESS_FINE_LOCATION_INDEX = 0
    val ACCESS_BACKGROUND_LOCATION_INDEX = 1




    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)

        return binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geofencingClient = LocationServices.getGeofencingClient(requireContext())

        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        //Checks all permissions
        checkPermissions()


    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap


            mapsViewModel.allPermissionsGranted.observe(viewLifecycleOwner, Observer {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = it
                }
            })

            database = Firebase.database.reference
            getConnectedMembersLocation()
        }
    }

    private  fun getConnectedMembers(){
        //TODO:: get connected members list and place them
        database.child("connections").child(FirebaseAuth.getInstance().currentUser?.uid!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    connectedMembers.add(it.getValue<String>()!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun getConnectedMembersLocation(){

        database.child("location").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mMap.clear()
                snapshot.children.forEach {
                    if (it.key == FirebaseAuth.getInstance().currentUser?.uid) {
                        return
                    }
                    val location = it.getValue<UserLocationDataModel>()

                    if (location != null) {
                        mMap.addMarker(MarkerOptions()
                                .position(LatLng(location.latitude, location.longitude))
                                .title(location.userDisplayName))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


    private fun checkPermissions(){
        //checking all location permissions
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mapsViewModel.allPermissionsGranted.value = true
                }else{
                    requestPermission()
                }
            }else{
                mapsViewModel.allPermissionsGranted.value = true
            }
        }else{
            requestPermission()
        }
    }

    //Requests for all permissions
    private fun requestPermission(){

        var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            permissions += Manifest.permission.ACCESS_BACKGROUND_LOCATION
            requestPermissions(permissions, FOREGROUND_AND_BACKGROUND_LOCATION_REQUEST_CODE)
            return
        }
        requestPermissions(permissions, FOREGROUND_LOCATION_REQUEST)
    }


    //Checking whether user GPS location is turned on. If not ask to turn on
    private fun checkUserGPSStatus(){
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    //val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    //startActivity(intent)
                    exception.startResolutionForResult(requireActivity(), 1)
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }else{
                Toast.makeText(requireContext(), "Location denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    //Checking whether user decided to turn on  the  GPS location from settings  page
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(requireContext(), "Location Accepted", Toast.LENGTH_LONG).show()
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(requireContext(), "Location denied", Toast.LENGTH_LONG).show()
                }
                else -> {
                }
            }
        }
    }


    //Request location update even when the app is in background or killed
    private fun updateLocation() {
        context?.let { context ->
        buildLocationRequest()
            checkUserGPSStatus()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) { return }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent())

        }
    }

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(context, LocationBackgroundService::class.java)
        intent.action = LocationBackgroundService.ACTION_PROCESS_UPDATES
        return PendingIntent.getBroadcast(context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 1F

    }



    //Handling location  permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            //Foreground location permission
            FOREGROUND_LOCATION_REQUEST -> {
                if (grantResults[ACCESS_FINE_LOCATION_INDEX] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
                    updateLocation()
                    mapsViewModel.allPermissionsGranted.value = true
                } else {
                    //permission not granted
                    Snackbar.make(binding.map, " You need to grant location permission in order to use this app", Snackbar.LENGTH_INDEFINITE)
                    mapsViewModel.allPermissionsGranted.value = false
                }
            }
            //background location permission
            FOREGROUND_AND_BACKGROUND_LOCATION_REQUEST_CODE -> {
                if (grantResults[ACCESS_FINE_LOCATION_INDEX] == PackageManager.PERMISSION_GRANTED
                        && grantResults[ACCESS_BACKGROUND_LOCATION_INDEX] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    updateLocation()
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, this)
                    mapsViewModel.allPermissionsGranted.value = true
                } else {
                    //Permission not granted
                    Snackbar.make(binding.map, " You need to grant location permission in order to use this app", Snackbar.LENGTH_INDEFINITE)
                    mapsViewModel.allPermissionsGranted.value = false
                }
            }
        }
    }



    //STARTS...Location listener callbacks start here
    //Tells about users current location updated every time the user device change it's current location

    var temp = 0
    override fun onLocationChanged(location: Location) {
        if(temp == 0) {
            val myLocation = LatLng(location.latitude, location.longitude)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12F))
        }
        temp = 1
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    //ENDS... location listener callbacks ends here


}
