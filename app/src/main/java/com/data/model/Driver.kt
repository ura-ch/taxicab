package com.example.ridesharing.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class Driver(
    @PrimaryKey val userId: String,
    val name: String,
    val phone: String,
    val car: String,
    val latitude: Double,
    val longitude: Double,
    val searchRadius: Int = 10 // Default search radius
)