 // DriverDao.kt
package com.example.ridesharing.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ridesharing.data.model.Driver
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriver(driver: Driver)

    @Query("SELECT * FROM drivers")
    fun getAllDrivers(): Flow<List<Driver>>

    @Query("SELECT * FROM drivers WHERE userId = :userId")
    fun getDriver(userId: String): Flow<Driver?>
    
    @Query("UPDATE drivers SET searchRadius = :radius WHERE userId = :userId")
    suspend fun updateSearchRadius(userId: String, radius: Int)
}