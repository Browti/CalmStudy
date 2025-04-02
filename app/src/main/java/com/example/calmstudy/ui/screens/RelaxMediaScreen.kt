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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import android.media.MediaPlayer
import android.media.AudioManager
import android.widget.Toast
import android.widget.VideoView
import android.net.Uri
import android.content.Intent
import com.example.calmstudy.R

enum class MediaType {
    AUDIO, VIDEO
}

@Composable
fun RelaxMediaScreen(navController: NavController) {
    var currentlyPlayingIndex by remember { mutableStateOf<Int?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var showOnlyFavorites by remember { mutableStateOf(false) }
    var selectedMediaType by remember { mutableStateOf(MediaType.AUDIO) }
    val context = LocalContext.current
    
    // Зберігаємо стан улюблених
    var favoriteAudios by remember { mutableStateOf(setOf<Int>()) }
    var favoriteVideos by remember { mutableStateOf(setOf<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Верхній рядок з кнопками
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

        // Перемикач між аудіо та відео
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TabRow(
                selectedTabIndex = selectedMediaType.ordinal,
                modifier = Modifier.width(300.dp)
            ) {
                Tab(
                    selected = selectedMediaType == MediaType.AUDIO,
                    onClick = { selectedMediaType = MediaType.AUDIO },
                    text = { Text("Аудіо") },
                    icon = { Icon(Icons.Default.AudioFile, contentDescription = null) }
                )
                Tab(
                    selected = selectedMediaType == MediaType.VIDEO,
                    onClick = { selectedMediaType = MediaType.VIDEO },
                    text = { Text("Відео") },
                    icon = { Icon(Icons.Default.VideoLibrary, contentDescription = null) }
                )
            }
        }

        // Заголовок
                            Text(
            text = when {
                selectedMediaType == MediaType.AUDIO && showOnlyFavorites -> "Улюблені аудіо"
                selectedMediaType == MediaType.AUDIO -> "Релаксаційні аудіо"
                selectedMediaType == MediaType.VIDEO && showOnlyFavorites -> "Улюблені відео"
                else -> "Релаксаційні відео"
            },
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (selectedMediaType) {
            MediaType.AUDIO -> {
                // Фільтруємо аудіо
                val displayedAudios = if (showOnlyFavorites) {
                    relaxAudios.filter { it.id in favoriteAudios }
                } else {
                    relaxAudios
                }

                if (displayedAudios.isEmpty() && showOnlyFavorites) {
                    EmptyFavoritesMessage()
                } else {
                    AudioGrid(
                        audios = displayedAudios,
                        currentlyPlayingIndex = currentlyPlayingIndex,
                        favoriteAudios = favoriteAudios,
                        onFavoriteClick = { audioId ->
                            favoriteAudios = if (audioId in favoriteAudios) {
                                favoriteAudios - audioId
                            } else {
                                favoriteAudios + audioId
                            }
                        },
                        onAudioClick = { audio ->
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
            MediaType.VIDEO -> {
                // Фільтруємо відео
                val displayedVideos = if (showOnlyFavorites) {
                    relaxVideos.filter { it.id in favoriteVideos }
                } else {
                    relaxVideos
                }

                if (displayedVideos.isEmpty() && showOnlyFavorites) {
                    EmptyFavoritesMessage()
                } else {
                    VideoGrid(
                        videos = displayedVideos,
                        favoriteVideos = favoriteVideos,
                        onFavoriteClick = { videoId ->
                            favoriteVideos = if (videoId in favoriteVideos) {
                                favoriteVideos - videoId
                            } else {
                                favoriteVideos + videoId
                            }
                        },
                        onVideoClick = { video ->
                            try {
                                val uri = "android.resource://${context.packageName}/${video.resourceId}"
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(Uri.parse(uri), "video/*")
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Помилка відтворення відео", Toast.LENGTH_SHORT).show()
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
private fun EmptyFavoritesMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
                Text(
            text = "У вас ще немає улюблених медіа.\nДодайте їх, натиснувши на сердечко.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun AudioGrid(
    audios: List<RelaxAudio>,
    currentlyPlayingIndex: Int?,
    favoriteAudios: Set<Int>,
    onFavoriteClick: (Int) -> Unit,
    onAudioClick: (RelaxAudio) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(audios) { audio ->
            AudioCard(
                audio = audio,
                isPlaying = currentlyPlayingIndex == audio.id,
                isFavorite = audio.id in favoriteAudios,
                onFavoriteClick = { onFavoriteClick(audio.id) },
                onClick = { onAudioClick(audio) }
            )
        }
    }
}

@Composable
private fun VideoGrid(
    videos: List<RelaxVideo>,
    favoriteVideos: Set<Int>,
    onFavoriteClick: (Int) -> Unit,
    onVideoClick: (RelaxVideo) -> Unit
) {
    var selectedVideo by remember { mutableStateOf<RelaxVideo?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(videos) { video ->
            VideoCard(
                video = video,
                isFavorite = video.id in favoriteVideos,
                onFavoriteClick = { onFavoriteClick(video.id) },
                onClick = { selectedVideo = video }
            )
        }
    }

    // Показуємо відеоплеєр, коли вибрано відео
    selectedVideo?.let { video ->
        VideoPlayer(
            video = video,
            onDismiss = { selectedVideo = null }
        )
    }
}

@Composable
private fun VideoPlayer(
    video: RelaxVideo,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Закрити",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            },
            text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f/9f)
            ) {
                val context = LocalContext.current
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setVideoURI(Uri.parse("android.resource://${context.packageName}/${video.resourceId}"))
                            setOnPreparedListener { mp ->
                                mp.isLooping = true
                                start()
                            }
                            setOnErrorListener { mp, what, extra ->
                                Toast.makeText(context, "Помилка відтворення відео", Toast.LENGTH_SHORT).show()
                                true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}

@Composable
private fun VideoCard(
    video: RelaxVideo,
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
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Відтворити",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
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
                text = video.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = video.duration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
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
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Зупинити" else "Відтворити",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
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

data class RelaxVideo(
    val id: Int,
    val title: String,
    val duration: String,
    val resourceId: Int
)

// Список відео
private val relaxVideos = listOf(
    RelaxVideo(
        id = 0,
        title = "Дощ за вікном",
        duration = "10:00",
        resourceId = R.raw.rain_video
    ),
    RelaxVideo(
        id = 1,
        title = "Океанські хвилі",
        duration = "15:00",
        resourceId = R.raw.ocean_video
    ),
    RelaxVideo(
        id = 2,
        title = "Ліс вдень",
        duration = "20:00",
        resourceId = R.raw.forest_video
    ),
    RelaxVideo(
        id = 3,
        title = "Медитативний пейзаж",
        duration = "30:00",
        resourceId = R.raw.meditation_video
    )
)

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