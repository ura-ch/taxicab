package com.example.ridesharing.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ridesharing.data.model.TripRequest
 import kotlinx.coroutines.flow.Flow

@Dao
interface TripRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTripRequest(tripRequest: TripRequest)
 
     @Query("SELECT * FROM trip_requests")
    fun getAllTripRequests(): Flow<List<TripRequest>>
}