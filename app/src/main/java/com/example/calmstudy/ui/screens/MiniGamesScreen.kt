package com.example.calmstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calmstudy.ui.games.*
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGamesScreen(navController: NavController) {
    var game by remember { mutableStateOf(MemoryGame()) }
    val gameState by game.gameState.collectAsState()

    LaunchedEffect(Unit) {
        game.startGame(Difficulty.EASY)
        while (true) {
            delay(1.seconds)
            game.updateTimer()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Гра пам'яті") },
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
            // Інформаційна панель
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Очки: ${gameState.score}")
                Text("Час: ${gameState.timeRemaining.inWholeSeconds}с")
                Text("Ходи: ${gameState.moves}")
            }

            // Вибір складності
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Difficulty.values().forEach { difficulty ->
                    Button(
                        onClick = { game.changeDifficulty(difficulty) },
                        enabled = !gameState.isGameOver
                    ) {
                        Text(difficulty.name)
                    }
                }
            }

            // Сітка з картками
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 80.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(gameState.cards) { card ->
                    MemoryCardItem(
                        card = card,
                        onClick = {
                            if (game.selectCard(card)) {
                                // Карти співпали
                            } else {
                                // Карти не співпали, перевертаємо їх назад
                                CoroutineScope(Dispatchers.Main).launch {
                                    delay(1000)
                                    game.unflipCards()
                                }
                            }
                        }
                    )
                }
            }

            // Кнопка "Нова гра" та рекорд
            Column(
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (gameState.isGameOver) {
                    Text(
                        "Гра завершена! ${if (gameState.score > gameState.highScore) "Новий рекорд!" else ""}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Button(
                    onClick = { game.resetGame() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Нова гра")
                }

                Text(
                    "Рекорд: ${gameState.highScore}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}