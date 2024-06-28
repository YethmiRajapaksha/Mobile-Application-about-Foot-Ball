package com.example.w1956078_cw2


import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.w1956078_cw2.ui.theme.W1956078_CW2Theme

lateinit var leagueDao: LeagueDao

class AddLeague : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room database and DAO
        val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "MyAppDatabase").build()
        val leagueDao = database.leagueDao()
        //val clubDao = database.clubDao()



        // Hardcoded league data from the provided JSON
        val leagues = listOf(
            League("4328", "English Premier League", "Soccer", "Premier League, EPL"),
            League("4329", "English League Championship", "Soccer", "Championship"),
            League("4330", "Scottish Premier League", "Soccer", "Scottish Premiership, SPFL"),
            League("4331", "German Bundesliga", "Soccer", "Bundesliga, Fußball-Bundesliga"),
            League("4332", "Italian Serie A", "Soccer", "Serie A"),
            League("4334", "French Ligue 1", "Soccer", "Ligue 1 Conforama"),
            League("4335", "Spanish La Liga", "Soccer", "LaLiga Santander, La Liga"),
            League("4336", "Greek Superleague Greece", "Soccer", ""),
            League("4337", "Dutch Eredivisie", "Soccer", "Eredivisie"),
            League("4338", "Belgian First Division A", "Soccer", "Jupiler Pro League"),
            League("4339", "Turkish Super Lig", "Soccer", "Super Lig"),
            League("4340", "Danish Superliga", "Soccer", ""),
            League("4344", "Portuguese Primeira Liga", "Soccer", "Liga NOS"),
            League("4346", "American Major League Soccer", "Soccer", "MLS, Major League Soccer"),
            League("4347", "Swedish Allsvenskan", "Soccer", "Fotbollsallsvenskan"),
            League("4350", "Mexican Primera League", "Soccer", "Liga MX"),
            League("4351", "Brazilian Serie A", "Soccer", ""),
            League("4354", "Ukrainian Premier League", "Soccer", ""),
            League("4355", "Russian Football Premier League", "Soccer", "Чемпионат России по футболу"),
            League("4356", "Australian A-League", "Soccer", "A-League"),
            League("4358", "Norwegian Eliteserien", "Soccer", "Eliteserien"),
            League("4359", "Chinese Super League", "Soccer", "")

        )


        GlobalScope.launch(Dispatchers.IO) {
            try {
                leagueDao.insertAll(leagues)
                // Show a success message using Toast
                showToast("Leagues added successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the exception (e.g., show error message)
                showToast("Failed to add leagues: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}

