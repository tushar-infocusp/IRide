package com.example.iride.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rides")
data class RideEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rideId: String,
    val origin: String,
    val destination: String,
    val seats: Int,
    val price: Double,
    val startDateTime: Long,
    val endDateTime: Long
)
