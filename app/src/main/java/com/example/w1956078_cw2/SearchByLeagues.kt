package com.example.w1956078_cw2

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import androidx.room.Room

lateinit var database: AppDatabase
lateinit var clubDao: ClubDao


class SearchByLeagues : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "MyAppDatabase").build()
        //val leagueDao = database.leagueDao()
        clubDao = database.clubDao()
        setContent {
            GUI()
        }
    }

    @Composable
    fun GUI() {
        var clubInfoDisplay by remember { mutableStateOf(" ") }
        // the league name to search for clubs
        var leagueName by remember { mutableStateOf("") }
        // Creates a CoroutineScope bound to the GUI composable lifecycle
        val scope = rememberCoroutineScope()


        Column(
            modifier = Modifier.fillMaxSize()
                .padding(vertical = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){

            TextField(value = leagueName, onValueChange = { leagueName = it })
            Button(onClick = {
                scope.launch {
                    clubInfoDisplay = fetchClubs(leagueName)
                }
            },
                modifier = Modifier
                    .padding(vertical = 40.dp)
                    .height(50.dp)
                    .width(250.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(red = 0, green = 153, blue = 153)
                )
            )
            {
                Text(text ="Retrieve  Clubs",
                    fontSize = 20.sp,)
            }
            Button(onClick = {
                scope.launch {
                    try {
                        saveToDb(leagueName)
                        clubInfoDisplay = "Clubs saved to database" // Update the display
                    } catch (e: Exception) {
                        e.printStackTrace()
                        clubInfoDisplay = "Error saving clubs to database: ${e.message}"
                    }
                }
            },
                modifier = Modifier
                    .height(50.dp)
                    .width(250.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(red = 0, green = 153, blue = 153)
                )
            )
            {
                Text(text="Save to Database ",
                    fontSize = 20.sp,)
            }

            Spacer(modifier = Modifier.height(50.dp))
            Text(
                modifier = Modifier
                    .height(10000.dp)
                    .verticalScroll(rememberScrollState()),
                text = clubInfoDisplay ,
                fontSize = 20.sp,

                )

        }}


    suspend fun fetchClubs(leagueName: String): String {
        val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$leagueName"
        val url = URL(url_string)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        // collecting all the JSON string
        var stb = StringBuilder()
        // run the code of the launched coroutine in a new thread
        withContext(Dispatchers.IO) {
            var bf = BufferedReader(InputStreamReader(con.inputStream))
            var line: String? = bf.readLine()
            while (line != null) { // keep reading until no more lines of text
                stb.append(line + "\n")
                line = bf.readLine()
            }
        }
        val allClubs = parseJSON(stb)
        return allClubs
    }

    fun parseJSON(stb: StringBuilder): String {
        // this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
        // Information about all the clubs extracted by this function
        var allClubs = StringBuilder()
        val teamsArray: JSONArray = json.getJSONArray("teams")
        // extract all the clubs from the JSON array
        for (i in 0 until teamsArray.length()) {
            val club: JSONObject = teamsArray[i] as JSONObject // this is a json object
            // extract club details
            val clubID = club.optString("idTeam")
            val clubName = club.optString("strTeam")
            val shortName = club.optString("strTeamShort")
            val alternate = club.optString("strAlternate")
            val formedYear = club.optString("intFormedYear")
            val leagueID = club.optString("idLeague")
            val league = club.optString("strLeague")
            val stadium = club.optString("strStadium")
            val keywords = club.optString("strKeywords")
            val thumb = club.optString("strStadiumThumb")
            val location = club.optString("strStadiumLocation")
            val capacity = club.optString("intStadiumCapacity")
            val website = club.optString("strWebsite")
            val jersey = club.optString("strTeamJersey")
            val logo = club.optString("strTeamLogo")
            // append club details to the result string
            allClubs.append("Club ID:$clubID\nClub Name:$clubName\nShort Name:$shortName\nAlternate:$alternate\nFormed Year: $formedYear\nLeague ID:$leagueID\nLeague: $league\nStadium: $stadium\nKeyword:$keywords\nStadium Thumbnail:$thumb\nStadium Location:$location\nStadium Capacity:$capacity\nWebsite: $website\nJersey: $jersey\nLogo: $logo\n\n")
        }
        return allClubs.toString()
    }

    suspend fun saveToDb(leagueName: String) {
        try {
            val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$leagueName"
            val url = URL(url_string)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            // collecting all the JSON string
            var stb = StringBuilder()
            // run the code of the launched coroutine in a new thread
            withContext(Dispatchers.IO) {
                var bf = BufferedReader(InputStreamReader(con.inputStream))
                var line: String? = bf.readLine()
                while (line != null) { // keep reading until no more lines of text
                    stb.append(line + "\n")
                    line = bf.readLine()
                }
            }
            // Parse JSON and get club list
            val clubs = parseJSONToClubs(stb)
            // Insert clubs into the database
            database.clubDao().insertAll(clubs)

        } catch (e: Exception) {
            // Handle the exception
            Log.e(TAG, "Error saving clubs to database: ${e.message}", e)
        }
    }


    fun parseJSONToClubs(stb: StringBuilder): List<Club> {
        val json = JSONObject(stb.toString())
        val teamsArray: JSONArray = json.getJSONArray("teams")
        val clubList = mutableListOf<Club>()
        for (i in 0 until teamsArray.length()) {
            val club: JSONObject = teamsArray[i] as JSONObject
            // Extract club details
            val clubID = club.optString("idTeam")
            val clubName = club.optString("strTeam")
            val shortName = club.optString("strTeamShort")
            val alternate = club.optString("strAlternate")
            val formedYear = club.optString("intFormedYear")
            val leagueID = club.optString("idLeague")
            val league = club.optString("strLeague")
            val stadium = club.optString("strStadium")
            val keywords = club.optString("strKeywords")
            val thumb = club.optString("strStadiumThumb")
            val location = club.optString("strStadiumLocation")
            val capacity = club.optString("intStadiumCapacity")
            val website = club.optString("strWebsite")
            val jersey = club.optString("strTeamJersey")
            val logo = club.optString("strTeamLogo")
            // Create Club object and add to list
            val newClub = Club(
                clubID = clubID,
                clubName = clubName,
                shortName = shortName,
                alternate = alternate,
                formedYear = formedYear,
                leagueID = leagueID,
                league = league,
                stadium = stadium,
                keywords = keywords,
                thumb = thumb,
                location = location,
                capacity = capacity,
                website = website,
                jersey = jersey,
                logo = logo
            )
            clubList.add(newClub)
        }
        return clubList
    }}