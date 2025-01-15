package com.example.ridesharing.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ridesharing.ui.components.InputField
import com.example.ridesharing.ui.trips.TripsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ridesharing.data.database.AppDatabase
import com.example.ridesharing.utils.LocationUtils
import com.ui.RegisterViewModel

@Composable
fun RequestTripScreen(database: AppDatabase, locationUtils: LocationUtils) {
    val viewModel: TripsViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TripsViewModel(database = database, locationUtils = locationUtils) as T
        }
    })
    var destination by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val location = viewModel.userLocation.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Request Trip", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        InputField(label = "Destination", value = destination, onValueChange = { destination = it })
        Spacer(modifier = Modifier.height(8.dp))
        InputField(label = "Price", value = price, onValueChange = { price = it })
        Spacer(modifier = Modifier.height(8.dp))
        InputField(label = "Name", value = name, onValueChange = { name = it })

        Spacer(modifier = Modifier.height(16.dp))
        val currentLocation = location.value
        if (currentLocation != null) {
            Button(onClick = {
                viewModel.createTripRequest(
                    name,
                    destination,
                    price,
                    currentLocation.latitude,
                    currentLocation.longitude
                )
            }) {
                Text("Request Trip")
            }
        } else {
            Text("Location not available.")
        }
    }
}