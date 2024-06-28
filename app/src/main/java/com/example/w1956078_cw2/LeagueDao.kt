package com.example.w1956078_cw2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


// Define DAO Interface
@Dao
interface LeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(leagues: List<League>)
    @Query("select * from leagues")
    suspend fun getAll(): List<League>

    @Query("SELECT * FROM leagues WHERE LOWER(strLeague) LIKE :query")
    suspend fun searchLeaguesByName(query: String): List<League>

    // insert one user without replacing an identical one- duplicates allowed
    @Insert
    suspend fun insertLeague(league: League)
    @Delete
    suspend fun deleteLeague(league: League)

    @Query("delete from leagues")
    suspend fun deleteAll()


}
