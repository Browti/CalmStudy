package com.example.calmstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGamesScreen(navController: NavController) {
    var currentGame by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }

    val games = listOf(
        "Знайдіть відмінності" to "Знайдіть 3 відмінності між зображеннями",
        "Складання пазлу" to "Складіть пазл з 9 частин",
        "Знайдіть пару" to "Знайдіть всі пари карток"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Міні-ігри") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = games[currentGame].first,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = games[currentGame].second,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            if (isPlaying) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ігрове поле",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Рахунок: $score",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(onClick = { isPlaying = false }) {
                        Text("Закінчити")
                    }
                }
            } else {
                Button(
                    onClick = { isPlaying = true },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Почати гру")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (currentGame > 0) currentGame--
                        },
                        enabled = currentGame > 0
                    ) {
                        Text("Попередня")
                    }
                    Button(
                        onClick = {
                            if (currentGame < games.size - 1) currentGame++
                        },
                        enabled = currentGame < games.size - 1
                    ) {
                        Text("Наступна")
                    }
                }
            }
        }
    }
}