package com.example.calmstudy.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class BreathingState {
    INHALE,
    HOLD,
    EXHALE,
    REST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingExerciseScreen(navController: NavController) {
    var isExerciseActive by remember { mutableStateOf(false) }
    var breathingState by remember { mutableStateOf(BreathingState.REST) }
    var currentCycle by remember { mutableStateOf(0) }
    val maxCycles = 5

    val coroutineScope = rememberCoroutineScope()

    // Анімація розміру кола
    val targetScale = when (breathingState) {
        BreathingState.INHALE -> 2.0f
        BreathingState.HOLD -> 2.0f
        BreathingState.EXHALE -> 1.0f
        BreathingState.REST -> 1.0f
    }

    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(
            durationMillis = when (breathingState) {
                BreathingState.INHALE -> 4000
                BreathingState.HOLD -> 100
                BreathingState.EXHALE -> 4000
                BreathingState.REST -> 100
            }
        ),
        label = "breathing"
    )

    // Анімація кольору
    val targetAlpha = when (breathingState) {
        BreathingState.INHALE -> 0.6f
        BreathingState.HOLD -> 0.8f
        BreathingState.EXHALE -> 0.3f
        BreathingState.REST -> 0.2f
    }

    val backgroundColor by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )

    // Логіка дихальної вправи
    LaunchedEffect(isExerciseActive) {
        if (isExerciseActive) {
            while (currentCycle < maxCycles && isExerciseActive) {
                // Вдих
                breathingState = BreathingState.INHALE
                delay(4000)

                // Затримка
                breathingState = BreathingState.HOLD
                delay(4000)

                // Видих
                breathingState = BreathingState.EXHALE
                delay(4000)

                // Пауза
                breathingState = BreathingState.REST
                delay(2000)

                currentCycle++
            }
            // Завершення циклу
            if (currentCycle >= maxCycles) {
                isExerciseActive = false
                currentCycle = 0
                breathingState = BreathingState.REST
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дихальні вправи") },
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
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = when (breathingState) {
                    BreathingState.INHALE -> "Вдихайте..."
                    BreathingState.HOLD -> "Затримайте дихання..."
                    BreathingState.EXHALE -> "Видихайте..."
                    BreathingState.REST -> "Підготуйтесь..."
                },
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = backgroundColor),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (breathingState) {
                            BreathingState.INHALE -> "4"
                            BreathingState.HOLD -> "4"
                            BreathingState.EXHALE -> "4"
                            BreathingState.REST -> "2"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    if (isExerciseActive) {
                        Text(
                            text = "Цикл ${currentCycle + 1}/$maxCycles",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (!isExerciseActive) {
                        currentCycle = 0
                    }
                    isExerciseActive = !isExerciseActive
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(if (isExerciseActive) "Зупинити" else "Почати")
            }

            Text(
                text = "Техніка 4-4-4-2:\n" +
                        "• Вдих на 4 секунди\n" +
                        "• Затримка на 4 секунди\n" +
                        "• Видих на 4 секунди\n" +
                        "• Пауза 2 секунди",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}