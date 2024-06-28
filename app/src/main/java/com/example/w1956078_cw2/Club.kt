package com.example.w1956078_cw2


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "clubs")

data class Club(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clubID: String,
    val clubName: String,
    val shortName: String,
    val alternate: String,
    val formedYear: String,
    val leagueID: String,
    val league: String,
    val stadium: String,
    val keywords: String,
    val thumb: String,
    val location: String,
    val capacity: String,
    val website: String,
    val jersey: String,
    val logo: String
)

