package com.example.ridesharing.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ridesharing.data.model.User
import com.example.ridesharing.data.model.TripRequest
import com.example.ridesharing.data.model.Driver

@Database(entities = [User::class, TripRequest::class, Driver::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tripRequestDao(): TripRequestDao
    abstract fun driverDao(): DriverDao
}