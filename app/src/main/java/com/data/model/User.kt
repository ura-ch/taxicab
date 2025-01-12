// User.kt
package com.example.ridesharing.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String,
    val name: String,
    val phone: String,
    val role: String = "passenger"
    )