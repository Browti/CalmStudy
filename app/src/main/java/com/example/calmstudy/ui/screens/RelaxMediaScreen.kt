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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.media.MediaPlayer
import android.media.AudioManager
import android.widget.Toast
import com.example.calmstudy.R

@Composable
fun RelaxMediaScreen(navController: NavController) {
    var currentlyPlayingIndex by remember { mutableStateOf<Int?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var showOnlyFavorites by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    // Зберігаємо стан улюблених звуків
    var favoriteAudios by remember { mutableStateOf(setOf<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Кнопка повернення на головну
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    navController.navigate("home")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "На головну"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("На головну")
            }
            
            // Перемикач для показу улюблених
            IconToggleButton(
                checked = showOnlyFavorites,
                onCheckedChange = { showOnlyFavorites = it }
            ) {
                Icon(
                    imageVector = if (showOnlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (showOnlyFavorites) "Показати всі" else "Показати улюблені",
                    tint = if (showOnlyFavorites) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Заголовок
        Text(
            text = if (showOnlyFavorites) "Улюблені аудіо" else "Релаксаційні аудіо",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Фільтруємо аудіо в залежності від вибраного режиму
        val displayedAudios = if (showOnlyFavorites) {
            relaxAudios.filter { it.id in favoriteAudios }
        } else {
            relaxAudios
        }

        if (displayedAudios.isEmpty() && showOnlyFavorites) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "У вас ще немає улюблених аудіо.\nДодайте їх, натиснувши на сердечко.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            // Сітка аудіо
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(displayedAudios) { audio ->
                    AudioCard(
                        audio = audio,
                        isPlaying = currentlyPlayingIndex == audio.id,
                        isFavorite = audio.id in favoriteAudios,
                        onFavoriteClick = {
                            favoriteAudios = if (audio.id in favoriteAudios) {
                                favoriteAudios - audio.id
                            } else {
                                favoriteAudios + audio.id
                            }
                        },
                        onClick = {
                            if (currentlyPlayingIndex == audio.id) {
                                try {
                                    mediaPlayer?.stop()
                                    mediaPlayer?.release()
                                    mediaPlayer = null
                                    currentlyPlayingIndex = null
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Помилка при зупинці аудіо", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                mediaPlayer?.stop()
                                mediaPlayer?.release()

                                try {
                                    mediaPlayer = MediaPlayer.create(context, audio.resourceId).apply {
                                        setAudioStreamType(AudioManager.STREAM_MUSIC)
                                        setVolume(1.0f, 1.0f)
                                        setOnPreparedListener { start() }
                                        setOnCompletionListener {
                                            currentlyPlayingIndex = null
                                            release()
                                            mediaPlayer = null
                                        }
                                        setOnErrorListener { mp, what, extra ->
                                            Toast.makeText(context, "Помилка відтворення аудіо", Toast.LENGTH_SHORT).show()
                                            currentlyPlayingIndex = null
                                            mp.release()
                                            mediaPlayer = null
                                            true
                                        }
                                    }
                                    currentlyPlayingIndex = audio.id
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Помилка при створенні аудіо плеєра", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
            } catch (e: Exception) {
                // Ігноруємо помилки при очищенні
            }
        }
    }
}

@Composable
private fun AudioCard(
    audio: RelaxAudio,
    isPlaying: Boolean,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка відтворення
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Зупинити" else "Відтворити",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                // Кнопка улюбленого
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Видалити з улюблених" else "Додати до улюблених",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = audio.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = audio.duration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class RelaxAudio(
    val id: Int,
    val title: String,
    val duration: String,
    val resourceId: Int
)

// Список аудіо
private val relaxAudios = listOf(
    RelaxAudio(
        id = 0,
        title = "Дощ",
        duration = "5:00",
        resourceId = R.raw.rain
    ),
    RelaxAudio(
        id = 1,
        title = "Океан",
        duration = "5:00",
        resourceId = R.raw.ocean
    ),
    RelaxAudio(
        id = 2,
        title = "Ліс",
        duration = "5:00",
        resourceId = R.raw.forest
    ),
    RelaxAudio(
        id = 3,
        title = "Медитація",
        duration = "5:00",
        resourceId = R.raw.meditation
    )
)