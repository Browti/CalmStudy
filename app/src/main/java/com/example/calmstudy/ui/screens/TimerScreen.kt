package com.example.calmstudy.ui.screens

import android.media.MediaPlayer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import com.example.calmstudy.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(navController: NavController) {
    val context = LocalContext.current
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var selectedDuration by remember { mutableStateOf<Long?>(null) }
    var remainingTime by remember { mutableStateOf<Long?>(null) }
    
    val timeOptions = listOf(
        TimeOption(1f/60f, "1 хвилина"),
        TimeOption(0.5f, "30 хвилин"),
        TimeOption(1f, "1 година"),
        TimeOption(3f, "3 години"),
        TimeOption(6f, "6 годин"),
        TimeOption(8f, "8 годин")
    )

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    LaunchedEffect(isTimerRunning, selectedDuration) {
        if (isTimerRunning && selectedDuration != null) {
            remainingTime = selectedDuration
            while (remainingTime!! > 0) {
                delay(1000)
                remainingTime = remainingTime!! - 1
            }
            // Відтворюємо звук при закінченні таймера
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, R.raw.timer_end)
            mediaPlayer?.start()
            isTimerRunning = false
            selectedDuration = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Таймер сну/медитації") },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isTimerRunning && remainingTime != null) {
                // Показуємо таймер
                TimerDisplay(
                    remainingTime = remainingTime!!,
                    onCancel = {
                        isTimerRunning = false
                        selectedDuration = null
                    }
                )
            } else {
                // Показуємо опції вибору часу
                Text(
                    text = "Виберіть тривалість",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(timeOptions) { option ->
                        TimeOptionCard(
                            option = option,
                            onClick = {
                                selectedDuration = (option.hours * 3600).toLong()
                                isTimerRunning = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimerDisplay(
    remainingTime: Long,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatTime(remainingTime),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        Text(
            text = "Таймер активний",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onCancel,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.Stop, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Зупинити таймер")
        }
    }
}

@Composable
private fun TimeOptionCard(
    option: TimeOption,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = option.label,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatTime(seconds: Long): String {
    return String.format(
        "%02d:%02d:%02d",
        TimeUnit.SECONDS.toHours(seconds),
        TimeUnit.SECONDS.toMinutes(seconds) % 60,
        seconds % 60
    )
}

data class TimeOption(
    val hours: Float,
    val label: String
) 