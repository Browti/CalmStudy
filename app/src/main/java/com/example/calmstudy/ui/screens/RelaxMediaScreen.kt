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

data class MediaItem(
    val title: String,
    val description: String,
    val icon: @Composable () -> Unit,
    val type: MediaType
)

enum class MediaType {
    MUSIC, NATURE_SOUNDS, MEDITATION, ASMR, ANIMATION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelaxMediaScreen(navController: NavController) {
    var selectedType by remember { mutableStateOf<MediaType?>(null) }

    val mediaItems = listOf(
        MediaItem(
            "Спокійна музика",
            "Лофі, класична та амбієнт музика",
            { Icon(Icons.Default.MusicNote, contentDescription = null) },
            MediaType.MUSIC
        ),
        MediaItem(
            "Звуки природи",
            "Дощ, ліс, океан, вогонь у каміні",
            { Icon(Icons.Default.Forest, contentDescription = null) },
            MediaType.NATURE_SOUNDS
        ),
        MediaItem(
            "Медитації",
            "Гайдовані медитації та дихальні практики",
            { Icon(Icons.Default.SelfImprovement, contentDescription = null) },
            MediaType.MEDITATION
        ),
        MediaItem(
            "ASMR",
            "Заспокійливі звуки та аудіоказки",
            { Icon(Icons.Default.Hearing, contentDescription = null) },
            MediaType.ASMR
        ),
        MediaItem(
            "Анімації",
            "Релакс-анімації та візуальні ефекти",
            { Icon(Icons.Default.Animation, contentDescription = null) },
            MediaType.ANIMATION
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Релакс медіа") },
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(mediaItems) { item ->
                    ElevatedCard(
                        onClick = { selectedType = item.type },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item.icon()
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    // Модальне вікно для відтворення медіа
    if (selectedType != null) {
        AlertDialog(
            onDismissRequest = { selectedType = null },
            title = {
                Text(
                    when (selectedType) {
                        MediaType.MUSIC -> "Спокійна музика"
                        MediaType.NATURE_SOUNDS -> "Звуки природи"
                        MediaType.MEDITATION -> "Медитації"
                        MediaType.ASMR -> "ASMR"
                        MediaType.ANIMATION -> "Анімації"
                        null -> ""
                    }
                )
            },
            text = {
                Column {
                    when (selectedType) {
                        MediaType.MUSIC -> {
                            ListItem(
                                headlineContent = { Text("Лофі музика") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Класична музика") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Амбієнт") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                        }
                        MediaType.NATURE_SOUNDS -> {
                            ListItem(
                                headlineContent = { Text("Дощ") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Ліс") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Океан") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Вогонь у каміні") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                        }
                        MediaType.MEDITATION -> {
                            ListItem(
                                headlineContent = { Text("Ранкова медитація") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Вечірня медитація") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Дихальні практики") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                        }
                        MediaType.ASMR -> {
                            ListItem(
                                headlineContent = { Text("Шепіт") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Заспокійливі звуки") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Аудіоказки") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                        }
                        MediaType.ANIMATION -> {
                            ListItem(
                                headlineContent = { Text("Хвилі") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Водоспади") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                            ListItem(
                                headlineContent = { Text("Сонячні промені") },
                                leadingContent = { Icon(Icons.Default.PlayCircle, null) }
                            )
                        }
                        null -> {}
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedType = null }) {
                    Text("Закрити")
                }
            }
        )
    }
}