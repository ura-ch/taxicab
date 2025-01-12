// RegisterViewModel.kt
package com.example.ridesharing.ui.register
 
 import androidx.compose.runtime.mutableStateOf
 import androidx.lifecycle.ViewModel
 import androidx.lifecycle.viewModelScope
 import com.example.ridesharing.data.database.AppDatabase
 import com.example.ridesharing.data.model.Driver
 import com.example.ridesharing.data.model.User
 import kotlinx.coroutines.launch
 import java.util.UUID
 
 class RegisterViewModel(private val database: AppDatabase) : ViewModel() {
 
    val errorMessage = mutableStateOf<String?>(null)
     fun registerUser(name: String, phone: String, role: String, car: String?) {
         if (name.isBlank() || phone.isBlank()) {
            errorMessage.value = "Name and phone are required."
              return
         }
 
         viewModelScope.launch {
             try {
                 val userId = UUID.randomUUID().toString()
                val user = User(userId = userId, name = name, phone = phone, role = role)
               database.userDao().insertUser(user)
 
                if (role == "driver" && car != null) {
                      val driver = Driver(userId = userId, name = name, phone = phone, car = car, latitude = 0.0, longitude = 0.0)
                     database.driverDao().insertDriver(driver)
                }
            } catch (e: Exception) {
                  errorMessage.value = "Error in user registration, try again: ${e.message}"
             }
       }
    }
}