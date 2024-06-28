//Demonstration Video Link -
// https://drive.google.com/drive/folders/1Xy1d7-bi5AOmydLLu-agxlF9IBYv_bW5

package com.example.w1956078_cw2


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w1956078_cw2.ui.theme.W1956078_CW2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            W1956078_CW2Theme {
                Surface(
                    color = Color(143, 188, 139), // Set background color
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 50.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, AddLeague::class.java)
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
                                text = "Add League",
                                fontSize = 25.sp
                            )
                        }
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, SearchByLeagues::class.java)
                                startActivity(intent)
                            },
                            modifier = Modifier
                                .height(70.dp)
                                .width(275.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(red = 0, green = 153, blue = 153)
                            )
                        ) {
                            Text(text = "Search for Clubs By League", fontSize = 25.sp)
                        }
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, SearchClub::class.java)
                                startActivity(intent)
                            },
                            modifier = Modifier
                                .height(70.dp)
                                .width(275.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(red = 0, green = 153, blue = 153)
                            )
                        ) {
                            Text(text = "Search for Clubs.", fontSize = 25.sp)

                        }
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, Jersey::class.java)
                                startActivity(intent)
                            },
                            modifier = Modifier
                                .height(70.dp)
                                .width(275.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(red = 0, green = 153, blue = 153)
                            )
                        ) {
                            Text(text = "Search Jerseys.",
                                fontSize = 25.sp
                            )
                        }

                    }
                }
            }
        }
    }}