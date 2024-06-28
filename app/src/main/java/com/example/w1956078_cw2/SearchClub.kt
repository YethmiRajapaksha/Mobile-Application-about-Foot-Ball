package com.example.w1956078_cw2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import androidx.compose.foundation.Image
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberImagePainter

class SearchClub : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(this, AppDatabase::class.java, "MyAppDatabase").build()

        setContent {

            SearchScreen(database)
        }
    }


    @Composable
    fun SearchScreen(database: AppDatabase) {
        var searchText by remember { mutableStateOf("") }
        var clubSearchResults by remember { mutableStateOf<List<Club>>(emptyList()) }
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .padding(50.dp)
                .padding(vertical = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text(text ="Enter league name or club name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 50.dp),

            )

            Button(
                onClick = {
                    scope.launch {
                        val clubs = performSearch(searchText, database)
                        clubSearchResults = clubs
                    }
                },
                modifier = Modifier
                    .height(50.dp)
                    .width(205.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(red = 0, green = 153, blue = 153)
                )

            ) {
                Text(text="Search",
                    fontSize = 25.sp,)
            }
            Spacer(modifier = Modifier.height(25.dp))

            if (clubSearchResults.isNotEmpty()) {
                LazyColumn {
                    items(clubSearchResults) { club ->ClubItem(club)
                        Text(
                            text = club.league,
                            modifier = Modifier.padding(vertical = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }
    @Composable
    fun ClubItem(club: Club) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // Display club logo thumbnail
            Image(
                painter = rememberImagePainter(club.thumb),
                contentDescription = "Club Logo",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Display club information
            Column {
                Text(text = club.clubName, modifier = Modifier.padding(bottom = 4.dp), fontWeight = FontWeight.Bold)
                Text(text = club.league, modifier = Modifier.padding(bottom = 4.dp))
                }
        }
    }


    suspend fun performSearch(query: String, database: AppDatabase): List<Club> {
        val lowercaseQuery = query.toLowerCase(Locale.getDefault())
        val clubsQuery = withContext(Dispatchers.IO) {
            database.clubDao().searchClubByName("%$lowercaseQuery%")
        }

        return clubsQuery
    }
}

