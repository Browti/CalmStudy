package com.example.calmstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calmstudy.ui.screens.*
import com.example.calmstudy.ui.theme.CalmStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalmStudyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("breathing") {
                            BreathingExerciseScreen(navController)
                        }
                        composable("games") {
                            MiniGamesScreen(navController)
                        }
                        composable("quotes") {
                            PositiveQuotesScreen(navController)
                        }
                        composable("relax") {
                            RelaxMediaScreen(navController)
                        }
                    }
                }
            }
        }
    }
}