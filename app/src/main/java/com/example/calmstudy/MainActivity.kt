package com.example.calmstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calmstudy.ui.components.BottomNavigation
import com.example.calmstudy.ui.screens.HomeScreen
import com.example.calmstudy.ui.screens.ProfileScreen
import com.example.calmstudy.ui.screens.BreathingExerciseScreen
import com.example.calmstudy.ui.screens.MinigamesScreen
import com.example.calmstudy.ui.screens.SurveyScreen
import com.example.calmstudy.ui.screens.PositiveQuotesScreen
import com.example.calmstudy.ui.screens.RelaxMediaScreen
import com.example.calmstudy.ui.screens.TimerScreen
import com.example.calmstudy.ui.theme.CalmStudyTheme
import com.example.calmstudy.ui.theme.ThemeViewModel
import com.example.calmstudy.ui.games.SnakeGame

class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()
            
            CalmStudyTheme(
                darkTheme = isDarkMode,
                dynamicColor = false
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    Scaffold(
                        bottomBar = {
                            BottomNavigation(navController = navController)
                        }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            NavHost(
                                navController = navController,
                                startDestination = "home",
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                composable("home") { HomeScreen(navController, themeViewModel) }
                                composable("profile") { ProfileScreen(navController) }
                                composable("breathing") { BreathingExerciseScreen(navController) }
                                composable("minigames") {
                                    MinigamesScreen(navController)
                                }
                                composable("snake") {
                                    SnakeGame(navController)
                                }
                                composable("survey") {
                                    SurveyScreen(navController)
                                }
                                composable("quotes") { PositiveQuotesScreen(navController) }
                                composable("relax") { RelaxMediaScreen(navController) }
                                composable("timer") { TimerScreen(navController) }
                            }
                        }
                    }
                }
            }
        }
    }
}