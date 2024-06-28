package com.example.w1956078_cw2

import androidx.room.Entity
import androidx.room.PrimaryKey


// Define Room Entity Class
@Entity(tableName = "leagues")
data class League(
    @PrimaryKey var idLeague: String,
    val strLeague: String,
    val strSport: String,
    val strLeagueAlternate: String
)

