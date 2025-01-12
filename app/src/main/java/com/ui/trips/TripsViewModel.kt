package com.example.ridesharing.ui.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ridesharing.data.database.AppDatabase
import com.example.ridesharing.data.model.TripRequest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.ridesharing.utils.LocationUtils
import android.location.Location
import com.example.ridesharing.data.model.Driver
import com.example.ridesharing.data.model.User
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class TripsViewModel(
    private val database: AppDatabase,
    private val locationUtils: LocationUtils
) : ViewModel() {
    val tripRequests: Flow<List<TripRequest>> = database.tripRequestDao().getAllTripRequests()
    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val _driver = MutableStateFlow<Driver?>(null)
    val driver: StateFlow<Driver?> = _driver
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        fetchUserLocation()
        loadUser()
    }
   
    private fun loadUser() {
        viewModelScope.launch {
            val user = database.userDao().getAllUsers().firstOrNull()?.firstOrNull()
           _user.value = user
        }
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
  
    fun loadDriver(){
         viewModelScope.launch {
             val user = _user.value
             if(user != null) {
                 val driver = database.driverDao().getDriver(user.userId).firstOrNull()
                  _driver.value = driver
             }
          }
      }
    
     fun saveSearchRadius(radius: Int){
        viewModelScope.launch {
           val user = _user.value
            if(user != null){
                database.driverDao().updateSearchRadius(user.userId, radius)
                loadDriver()
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
    
    val filteredTripRequests: StateFlow<List<TripRequest>> = combine(
        tripRequests,
       userLocation,
        driver
    ) { tripRequests, location, driver ->
        filterTripsByRadius(tripRequests, location, driver)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    private fun filterTripsByRadius(trips: List<TripRequest>, location: Location?, driver: Driver?): List<TripRequest> {
        if (location == null || driver == null || driver.searchRadius == 0) {
             return trips
        }
        return trips.filter { trip ->
            val distance = calculateDistance(
                 location.latitude,
                 location.longitude,
                 trip.latitude,
                trip.longitude
            )
            distance <= (driver.searchRadius * 1000) // Convert km to meters
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371e3
        val φ1 = Math.toRadians(lat1)
        val φ2 = Math.toRadians(lat2)
        val Δφ = Math.toRadians(lat2 - lat1)
        val Δλ = Math.toRadians(lon2 - lon1)

        val a = sin(Δφ / 2) * sin(Δφ / 2) +
                cos(φ1) * cos(φ2) *
                sin(Δλ / 2) * sin(Δλ / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }
}