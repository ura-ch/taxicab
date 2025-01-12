package com.example.ridesharing.ui
 
import android.Manifest
import android.content.Intent
 import android.content.pm.PackageManager
 import android.os.Bundle
 import androidx.activity.ComponentActivity
 import androidx.activity.compose.setContent
 import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
 import androidx.compose.runtime.Composable
 import androidx.compose.ui.Modifier
 import androidx.core.app.ActivityCompat
 import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
 import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
 import com.example.ridesharing.data.database.AppDatabase
import com.example.ridesharing.ui.theme.RideSharingTheme
import com.example.ridesharing.ui.register.RegisterScreen
import com.example.ridesharing.ui.trips.TripsListScreen
 import com.example.ridesharing.utils.LocationUtils

class MainActivity : ComponentActivity() {
 
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var database: AppDatabase
    private lateinit var locationUtils: LocationUtils
 
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
        checkLocationPermission()
          database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "ride_sharing_db").build()
        locationUtils = LocationUtils(applicationContext)
         setContent {
           RideSharingTheme {
                 // A surface container using the 'background' color from the theme
                  Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                     AppNavigation(database = database, locationUtils = locationUtils)
                 }
             }
          }
     }
 
     override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
         grantResults: IntArray
     ) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
             LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // permission is granted
                 } else {
                     // Show some message for user
               }
             }
        }
    }
     private fun checkLocationPermission() {
         if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
         }
    }
 }
 
 @Composable
 fun AppNavigation(database: AppDatabase, locationUtils: LocationUtils) {
     val navController = rememberNavController()
     NavHost(navController = navController, startDestination = "register") {
         composable("register") { RegisterScreen(navController = navController, database = database) }
         composable("trips") { TripsListScreen(navController = navController, database = database, locationUtils = locationUtils) }
        composable("map") { MapActivity() }
        composable("requestTrip") { RequestTripScreen( database = database, locationUtils = locationUtils) }
     }
 }