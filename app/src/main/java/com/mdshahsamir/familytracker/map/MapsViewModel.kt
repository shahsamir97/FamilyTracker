package com.mdshahsamir.familytracker.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapsViewModel() : ViewModel() {
     var allPermissionsGranted  = MutableLiveData<Boolean>()

     init {
         allPermissionsGranted.value = false
     }
}