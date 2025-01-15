package com.example.ridesharing.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ridesharing.data.database.AppDatabase
import com.example.ridesharing.ui.components.InputField
import com.example.ridesharing.ui.components.CustomButton
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ui.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavController, database: AppDatabase) {
    val viewModel: RegisterViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(database = database) as T
        }
    })
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("passenger") }
    var car by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registration", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        InputField(label = "Name", value = name, onValueChange = { name = it })

        Spacer(modifier = Modifier.height(8.dp))

        InputField(label = "Phone", value = phone, onValueChange = { phone = it })

        if (role == "driver") {
            Spacer(modifier = Modifier.height(8.dp))
            InputField(label = "Car Model", value = car, onValueChange = { car = it })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(selected = role == "passenger", onClick = { role = "passenger" })
            Text("Passenger")

            Spacer(modifier = Modifier.width(8.dp))

            RadioButton(selected = role == "driver", onClick = { role = "driver" })
            Text("Driver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(text = "Register", onClick = {
            viewModel.registerUser(name, phone, role, car)
            navController.navigate("trips")
        })
    }
}