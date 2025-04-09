package com.example.calmstudy.ui.games

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnakeGame(navController: NavController) {
    var gameState by remember { mutableStateOf(SnakeGameState()) }
    var isGameActive by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Отримуємо кольори заздалегідь
    val snakeColor = MaterialTheme.colorScheme.primary
    val foodColor = MaterialTheme.colorScheme.error
    val backgroundColor = MaterialTheme.colorScheme.surface

    LaunchedEffect(isGameActive) {
        while (isGameActive) {
            delay(200L) // Швидкість гри
            gameState = gameState.update()
            if (gameState.isGameOver) {
                isGameActive = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Змійка") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
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
            // Ігрове поле
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        val cellSize = size.width / BOARD_SIZE

                        // Малюємо змійку
                        gameState.snake.forEach { pos ->
                            drawRect(
                                color = snakeColor,
                                topLeft = Offset(pos.x * cellSize, pos.y * cellSize),
                                size = Size(cellSize, cellSize)
                            )
                        }

                        // Малюємо їжу
                        drawRect(
                            color = foodColor,
                            topLeft = Offset(gameState.food.x * cellSize, gameState.food.y * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }

                    // Показуємо рахунок поверх Canvas
                    Text(
                        text = "Рахунок: ${gameState.score}",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Показуємо повідомлення про кінець гри поверх Canvas
                    if (gameState.isGameOver) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Гра закінчена!",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            // Кнопки керування
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (!isGameActive) {
                            gameState = SnakeGameState()
                            isGameActive = true
                        }
                    },
                    enabled = !isGameActive
                ) {
                    Icon(Icons.Default.PlayArrow, "Почати")
                    Spacer(Modifier.width(8.dp))
                    Text("Почати")
                }

                Button(
                    onClick = {
                        gameState = SnakeGameState()
                        isGameActive = false
                    }
                ) {
                    Icon(Icons.Default.Refresh, "Скинути")
                    Spacer(Modifier.width(8.dp))
                    Text("Скинути")
                }
            }

            // Кнопки напрямку
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { gameState = gameState.copy(direction = Direction.UP) },
                    enabled = isGameActive && gameState.direction != Direction.DOWN
                ) {
                    Text("↑")
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { gameState = gameState.copy(direction = Direction.LEFT) },
                        enabled = isGameActive && gameState.direction != Direction.RIGHT
                    ) {
                        Text("←")
                    }
                    Button(
                        onClick = { gameState = gameState.copy(direction = Direction.RIGHT) },
                        enabled = isGameActive && gameState.direction != Direction.LEFT
                    ) {
                        Text("→")
                    }
                }
                Button(
                    onClick = { gameState = gameState.copy(direction = Direction.DOWN) },
                    enabled = isGameActive && gameState.direction != Direction.UP
                ) {
                    Text("↓")
                }
            }
        }
    }
}

private const val BOARD_SIZE = 20

data class Position(val x: Int, val y: Int)

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

data class SnakeGameState(
    val snake: List<Position> = listOf(Position(5, 5)),
    val direction: Direction = Direction.RIGHT,
    val food: Position = Position(10, 10),
    val isGameOver: Boolean = false,
    val score: Int = 0
) {
    fun update(): SnakeGameState {
        if (isGameOver) return this

        // Обчислюємо нову позицію голови
        val head = snake.first()
        val newHead = when (direction) {
            Direction.UP -> Position(head.x, (head.y - 1 + BOARD_SIZE) % BOARD_SIZE)
            Direction.DOWN -> Position(head.x, (head.y + 1) % BOARD_SIZE)
            Direction.LEFT -> Position((head.x - 1 + BOARD_SIZE) % BOARD_SIZE, head.y)
            Direction.RIGHT -> Position((head.x + 1) % BOARD_SIZE, head.y)
        }

        // Перевіряємо зіткнення з тілом
        if (snake.contains(newHead)) {
            return copy(isGameOver = true)
        }

        // Оновлюємо змійку
        val newSnake = if (newHead == food) {
            listOf(newHead) + snake
        } else {
            listOf(newHead) + snake.dropLast(1)
        }

        // Генеруємо нову їжу, якщо змійка з'їла стару
        val newFood = if (newHead == food) {
            generateFood(newSnake)
        } else {
            food
        }

        return copy(
            snake = newSnake,
            food = newFood,
            score = if (newHead == food) score + 1 else score
        )
    }

    private fun generateFood(snake: List<Position>): Position {
        var newFood: Position
        do {
            newFood = Position(
                Random.nextInt(BOARD_SIZE),
                Random.nextInt(BOARD_SIZE)
            )
        } while (snake.contains(newFood))
        return newFood
    }
} 