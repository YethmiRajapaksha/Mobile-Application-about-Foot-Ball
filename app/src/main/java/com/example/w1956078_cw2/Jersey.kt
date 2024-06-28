package com.example.w1956078_cw2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL



class Jersey : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 30.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                val scope = rememberCoroutineScope()
                Spacer(modifier = Modifier.height(50.dp))
                // Jerseys along with their seasons for the corresponding club entered
                var keyword by rememberSaveable { mutableStateOf("") }
                var showSearchField by rememberSaveable { mutableStateOf(false) }
                var showsJersey by rememberSaveable { mutableStateOf(false) }
                if (showSearchField) {
                    Button(
                        onClick = {
                            val intent = Intent(this@Jersey, MainActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier
                            .height(80.dp)
                            .width(275.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(red = 0, green = 153, blue = 153)
                        )
                    ) {
                        Text(
                            text = "Home",
                            fontSize = 25.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    // TextField to enter the club name
                    TextField(
                        value = keyword,
                        onValueChange = { keyword = it },
                        label = { Text("Enter search string") },

                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    // After the button click the corresponding jerseys along with the season is displayed
                    Button(
                        onClick = {
                            scope.launch {
                                showsJersey = true
                            }
                        },
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .height(70.dp)
                            .width(280.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(red = 0, green = 153, blue = 153)
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text = "Search", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    if (showsJersey) {
                        displayJersey(word = keyword)
                    }
                }
                else {
                    // The button is clicked for the Search and the club name TextField to be appeared
                    Button(
                        onClick = { showSearchField = true },
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .height(70.dp)
                            .width(325.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(red = 0, green = 153, blue = 153)
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text = "Enter Club Name", fontSize = 22.sp)
                    }
                }
            }
        }
    }

//All the league ID's are fetched
suspend fun fetchClubInfoLeaguesId(): MutableList<String> {
    val urlString = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
    val url = URL(urlString)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
    var stb = StringBuilder()
    withContext(Dispatchers.IO) {
        var bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null) {
            stb.append(line + "\n")
            line = bf.readLine()
        }
    }
    val allLeagues = parseJSONLeaguesId(stb)
    return allLeagues
}

fun parseJSONLeaguesId(stb: StringBuilder): MutableList<String>{
    val json = JSONObject(stb.toString())
    var allClubs = StringBuilder()
    val leagueIds = mutableListOf<String>()
    var jsonArray: JSONArray = json.getJSONArray("leagues")
    for (i in 0..jsonArray.length()-1){
        try {
            val teamInfo: JSONObject = jsonArray[i] as JSONObject
            val leagueId = teamInfo["idLeague"] as String
            leagueIds.add(leagueId)
        } catch (jen: JSONException) {
        }
    }
    return leagueIds
}

suspend fun fetchClubLeagueInfo(word: String): MutableList<String> {
    var allLeagueId = fetchClubInfoLeaguesId()
    val filteredTeamIds = mutableListOf<String>()
    for (leagueId in allLeagueId){
        val urlString = "https://www.thesportsdb.com/api/v1/json/3/lookuptable.php?l=$leagueId&s=2020-2021"
        try {
            val url = URL(urlString)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            var stb = StringBuilder()
            withContext(Dispatchers.IO) {
                var bf = BufferedReader(InputStreamReader(con.inputStream))
                var line: String? = bf.readLine()
                while (line != null) {
                    stb.append(line + "\n")
                    line = bf.readLine()
                }
            }
            var allLeagues = parseJSOLeagueInfo(stb)
            for ((teamId, teamName) in allLeagues) {
                if (teamName.contains(word, ignoreCase = true)) {
                    filteredTeamIds.add(teamId)
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
    return filteredTeamIds
}
fun parseJSOLeagueInfo(stb: StringBuilder): MutableList<Pair<String, String>>{
    val json = JSONObject(stb.toString())
    var allClubs = StringBuilder()
    var jsonArray: JSONArray = json.getJSONArray("table")
    val teamInfoList = mutableListOf<Pair<String, String>>()
    for (i in 0..jsonArray.length()-1){
        try {
            val teamInfo: JSONObject = jsonArray[i] as JSONObject
            val teamID = teamInfo.getString("idTeam")
            val teamName = teamInfo.getString("strTeam")
            teamInfoList.add(Pair(teamID, teamName))
        } catch (jen: JSONException) {
        }
    }
    return teamInfoList
}

suspend fun fetchClubJersey(word: String): MutableList<Pair<String, String>>{
    var allClubsId = fetchClubLeagueInfo(word)
    val jerseysSeasons = mutableListOf<Pair<String, String>>()
    for (clubId in allClubsId){
        val urlString = "https://www.thesportsdb.com/api/v1/json/3/lookupequipment.php?id=$clubId"
        try {
            val url = URL(urlString)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            var stb = StringBuilder()
            withContext(Dispatchers.IO) {
                var bf = BufferedReader(InputStreamReader(con.inputStream))
                var line: String? = bf.readLine()
                while (line != null) {
                    stb.append(line + "\n")
                    line = bf.readLine()
                }
            }
            var allClubJersey = parseJSONClubJersey(stb)
            for ((jerseySeason, jerseyEquipment) in allClubJersey) {
                jerseysSeasons.add(Pair(jerseySeason, jerseyEquipment))
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
    return jerseysSeasons
}
fun parseJSONClubJersey(stb: StringBuilder): MutableList<Pair<String, String>>{
    val json = JSONObject(stb.toString())
    var allClubs = StringBuilder()
    var jsonArray: JSONArray = json.getJSONArray("equipment")
    val clubJerseySeason = mutableListOf<Pair<String, String>>()
    for (i in 0..jsonArray.length()-1){
        try {
            val teamInfo: JSONObject = jsonArray[i] as JSONObject
            val jerseySeason = teamInfo["strSeason"] as String
            val jerseyEquipment = teamInfo["strEquipment"] as String
            clubJerseySeason.add(Pair(jerseySeason, jerseyEquipment))
        } catch (jen: JSONException) {
        }
    }
    return clubJerseySeason
}

@Composable
fun displayJersey(word: String) {
    val logoUrls = remember { mutableStateListOf<String>() }
    val seasonsList = rememberSaveable { mutableListOf<String>() }
    val jerseyUrlsList = rememberSaveable { mutableListOf<String>() }
    LaunchedEffect(word) {
        val clubTestData = fetchClubJersey(word)
        seasonsList.clear()
        jerseyUrlsList.clear()
        clubTestData.forEach { (season, jerseyDetails) ->
            seasonsList.add(season)
            jerseyUrlsList.add(jerseyDetails)
        }
        logoUrls.addAll(jerseyUrlsList)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        logoUrls.forEachIndexed { index, logoUrl ->
            Text(text = seasonsList[index], fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(15.dp))
            val painter = rememberImagePainter(
                data = logoUrl,
                builder = {
                    crossfade(true)
                }
            )
            Image(
                painter = painter,
                contentDescription = seasonsList[index],
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}}

