
package com.example.w1956078_cw2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clubs: List<Club>)

    @Query("SELECT * FROM clubs")
    suspend fun getAllClubs(): List<Club>

    @Query("SELECT * FROM clubs WHERE LOWER(league) LIKE :query")
    suspend fun searchClubByName(query: String): List<Club>

    @Query("SELECT * FROM clubs WHERE LOWER(clubName) LIKE '%' || LOWER(:query) || '%' OR LOWER(league) LIKE '%' || LOWER(:query) || '%'")
    suspend fun searchClubByNameOrLeague(query: String): List<Club>
}

