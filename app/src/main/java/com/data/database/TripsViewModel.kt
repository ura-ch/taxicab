package com.example.ridesharing.ui.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ridesharing.data.database.AppDatabase
import com.example.ridesharing.data.model.TripRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.ridesharing.utils.LocationUtils
import android.location.Location
import java.util.UUID

class TripsViewModel(
    private val database: AppDatabase,
    private val locationUtils: LocationUtils
) : ViewModel() {
    val tripRequests: Flow<List<TripRequest>> = database.tripRequestDao().getAllTripRequests()
    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchUserLocation()
    }

    private fun fetchUserLocation() {
        viewModelScope.launch {
            try {
                val location = locationUtils.getLastLocation()
                _userLocation.value = location
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching location: ${e.message}"
            }
        }
    }

    fun createTripRequest(
        name: String,
        destination: String,
        price: String,
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            try {
                val id = UUID.randomUUID().toString()
                val tripRequest = TripRequest(
                    id = id,
                    name = name,
                    destination = destination,
                    price = price,
                    latitude = latitude,
                    longitude = longitude,
                    userId = ""
                )
                database.tripRequestDao().insertTripRequest(tripRequest)
            } catch (e: Exception) {
                _errorMessage.value = "Error creating trip request: ${e.message}"
            }
        }
    }
}