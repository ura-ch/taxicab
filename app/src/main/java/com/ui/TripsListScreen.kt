package com.example.ridesharing.ui.trips

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ridesharing.data.model.TripRequest
import com.example.ridesharing.data.database.AppDatabase
import com.example.ridesharing.utils.LocationUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Composable
fun TripsListScreen(
    navController: NavController,
    database: AppDatabase,
    locationUtils: LocationUtils
) {
    val viewModel: TripsViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TripsViewModel(database = database, locationUtils = locationUtils) as T
        }
    })
    val trips = viewModel.filteredTripRequests.collectAsState(initial = emptyList()).value
    val user = viewModel.user.collectAsState(initial = null).value
    var radius by remember { mutableStateOf(10) } // Default radius 10 km
  
    LaunchedEffect(user){
        if(user?.role == "driver") {
            viewModel.loadDriver()
         }
   }
   val driver = viewModel.driver.collectAsState(initial = null).value
   if(driver != null){
        radius = driver.searchRadius
   }
    
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Available Trips", style = MaterialTheme.typography.headlineMedium)
         if(user?.role == "driver") {
             Row(
                 horizontalArrangement = Arrangement.Center,
                 modifier = Modifier.fillMaxWidth()
             ) {
                 Text("Search Radius (km): $radius")
                 Slider(
                     value = radius.toFloat(),
                     onValueChange = { radius = it.toInt() },
                     valueRange = 1f..50f,
                     steps = 49,
                     modifier = Modifier.width(200.dp)
                 )
                 Button(onClick = {
                     viewModel.saveSearchRadius(radius)
                 }){
                   Text("Save radius")
                 }
             }
         }
        Spacer(modifier = Modifier.height(16.dp))
        if (trips.isEmpty()) {
            Text("There are no available trip requests.")
        } else {
            TripsList(trips = trips, navController = navController)
        }
        Button(onClick = { navController.navigate("requestTrip") }) {
            Text("Create request")
        }
    }
}


@Composable
fun TripsList(trips: List<TripRequest>, navController: NavController) {
    LazyColumn {
        items(trips) { trip ->
            TripItem(trip = trip, navController = navController)
        }
    }
}

@Composable
fun TripItem(trip: TripRequest, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Name: ${trip.name}")
            Text("Destination: ${trip.destination}")
            Text("Price: ${trip.price}")
            Button(onClick = {
                navController.navigate("map")
            }) {
                Text("View on Map")
            }
        }
    }
}