package com.example.calmstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

private val games = listOf(
    Game(
        "Хрестики-нулики",
        "Класична гра для двох гравців",
        Icons.Default.Close
    ),
    Game(
        "Знайди пару",
        "Тренуйте пам'ять та увагу",
        Icons.Default.Extension
    ),
    Game(
        "Змійка",
        "Класична гра змійка",
        Icons.Default.Games
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinigamesScreen(navController: NavController) {
    var selectedGame by remember { mutableStateOf<Game?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Міні-ігри") },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (selectedGame == null) {
                            navController.navigateUp()
                        } else {
                            selectedGame = null
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (selectedGame == null) {
            // Показуємо список ігор
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                games.forEach { game ->
                    Card(
                        onClick = {
                            when (game) {
                                games[0] -> selectedGame = game
                                games[1] -> selectedGame = game
                                games[2] -> navController.navigate("snake")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        ListItem(
                            headlineContent = { Text(game.title) },
                            supportingContent = { Text(game.description) },
                            leadingContent = {
                                Icon(
                                    imageVector = game.icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                    }
                }
            }
        } else {
            // Показуємо вибрану гру
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                when (selectedGame) {
                    games[0] -> TicTacToeGame()
                    games[1] -> MemoryGame()
                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun GameCard(game: Game, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = game.icon,
                contentDescription = game.title,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = game.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = game.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

private data class Game(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
private fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var isGameOver by remember { mutableStateOf(false) }

        Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Інформація про поточного гравця
        Text(
            text = if (winner != null) "Переможець: $winner" else "Хід гравця: $currentPlayer",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Ігрове поле
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(9) { index ->
                TicTacToeCell(
                    value = board[index],
                    onClick = {
                        if (!isGameOver && board[index].isEmpty()) {
                            val newBoard = board.toMutableList()
                            newBoard[index] = currentPlayer
                            board = newBoard

                            // Перевірка на перемогу
                            if (checkWinner(newBoard, currentPlayer)) {
                                winner = currentPlayer
                                isGameOver = true
                            } else if (newBoard.all { it.isNotEmpty() }) {
                                isGameOver = true
                            } else {
                                currentPlayer = if (currentPlayer == "X") "O" else "X"
                            }
                        }
                    }
                )
            }
        }

        // Кнопка "Нова гра"
        if (isGameOver) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    board = List(9) { "" }
                    currentPlayer = "X"
                    winner = null
                    isGameOver = false
                }
            ) {
                Text("Нова гра")
            }
        }
    }
}

@Composable
private fun TicTacToeCell(
    value: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
                modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

private fun checkWinner(board: List<String>, player: String): Boolean {
    // Перевірка рядків
    for (i in 0..6 step 3) {
        if (board[i] == player && board[i + 1] == player && board[i + 2] == player) {
            return true
        }
    }

    // Перевірка стовпців
    for (i in 0..2) {
        if (board[i] == player && board[i + 3] == player && board[i + 6] == player) {
            return true
        }
    }

    // Перевірка діагоналей
    if (board[0] == player && board[4] == player && board[8] == player) {
        return true
    }
    if (board[2] == player && board[4] == player && board[6] == player) {
        return true
    }

    return false
}

@Composable
private fun MemoryGame() {
    var cards by remember { mutableStateOf(generateMemoryCards()) }
    var firstCard by remember { mutableStateOf<Int?>(null) }
    var secondCard by remember { mutableStateOf<Int?>(null) }
    var canFlip by remember { mutableStateOf(true) }
    
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Знайдіть пари однакових карток",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

            LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cards.size) { index ->
                MemoryCard(
                    card = cards[index],
                        onClick = {
                        if (canFlip && !cards[index].isMatched && !cards[index].isFlipped) {
                            when {
                                firstCard == null -> {
                                    firstCard = index
                                    cards = cards.toMutableList().apply {
                                        this[index] = this[index].copy(isFlipped = true)
                                    }
                                }
                                secondCard == null && firstCard != index -> {
                                    secondCard = index
                                    canFlip = false
                                    cards = cards.toMutableList().apply {
                                        this[index] = this[index].copy(isFlipped = true)
                                    }
                                    
                                    scope.launch {
                                        delay(500) // Затримка перед перевіркою
                                        
                                        // Перевіряємо чи карти співпадають
                                        if (cards[firstCard!!].value == cards[index].value) {
                                            cards = cards.toMutableList().apply {
                                                this[firstCard!!] = this[firstCard!!].copy(isMatched = true)
                                                this[index] = this[index].copy(isMatched = true)
                                            }
                            } else {
                                            // Якщо не співпадають, перевертаємо назад
                                            cards = cards.toMutableList().apply {
                                                this[firstCard!!] = this[firstCard!!].copy(isFlipped = false)
                                                this[index] = this[index].copy(isFlipped = false)
                                            }
                                        }
                                        
                                        firstCard = null
                                        secondCard = null
                                        canFlip = true
                                    }
                                }
                                }
                            }
                        }
                    )
                }
            }

        // Кнопка "Нова гра"
        if (cards.all { it.isMatched }) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { 
                    cards = generateMemoryCards()
                    firstCard = null
                    secondCard = null
                    canFlip = true
                }
                ) {
                    Text("Нова гра")
            }
        }
    }
}

@Composable
private fun MemoryCard(
    card: MemoryCard,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (card.isFlipped || card.isMatched)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (card.isFlipped || card.isMatched) {
                Text(
                    text = card.value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

private data class MemoryCard(
    val value: String,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

private fun generateMemoryCards(): List<MemoryCard> {
    val emojis = listOf("🌟", "🎮", "🎨", "🎵", "🌈", "📚", "🎪", "🎭")
    return (emojis + emojis)
        .shuffled()
        .map { MemoryCard(it) }
}
