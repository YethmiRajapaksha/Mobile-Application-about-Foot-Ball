package com.example.w1956078_cw2

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [League::class, Club::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun leagueDao(): LeagueDao
    abstract fun clubDao(): ClubDao // Add ClubDao declaration

}
