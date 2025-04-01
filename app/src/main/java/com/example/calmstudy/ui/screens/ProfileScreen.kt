package com.example.calmstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.calmstudy.ui.theme.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = viewModel()
) {
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var showRegisterDialog by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(false) }
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()
    var notificationsEnabled by remember { mutableStateOf(true) }

    // Змінні для входу
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    // Змінні для реєстрації
    var registerName by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isLoggedIn) {
            // Кнопки авторизації
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { showLoginDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = "Увійти",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Увійти")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { showRegisterDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Реєстрація",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Реєстрація")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Аватар користувача
        Surface(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Аватар користувача",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ім'я користувача
        Text(
            text = if (isLoggedIn) "Користувач" else "Гість",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoggedIn) {
            // Налаштування
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Налаштування",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Повзунок сповіщень
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Сповіщення",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Досягнення
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Досягнення",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AchievementItem(
                            icon = Icons.Default.Star,
                            label = "Перша сесія",
                            isUnlocked = true
                        )
                        AchievementItem(
                            icon = Icons.Default.Timer,
                            label = "30 хвилин",
                            isUnlocked = false
                        )
                        AchievementItem(
                            icon = Icons.Default.EmojiEvents,
                            label = "7 днів",
                            isUnlocked = false
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Допомога
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            onClick = { showHelpDialog = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = "Допомога",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Допомога",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Перейти",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        if (isLoggedIn) {
            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка виходу з профілю
            Button(
                onClick = { isLoggedIn = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Вийти з профілю",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Вийти з профілю")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка видалення профілю
            Button(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Видалити профіль",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Видалити профіль")
            }
        }
    }

    // Діалог входу
    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text("Вхід") },
            text = {
                Column {
                    OutlinedTextField(
                        value = loginEmail,
                        onValueChange = { loginEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it },
                        label = { Text("Пароль") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLoginDialog = false
                        isLoggedIn = true
                        // Скидаємо поля після входу
                        loginEmail = ""
                        loginPassword = ""
                    }
                ) {
                    Text("Увійти")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showLoginDialog = false
                        // Скидаємо поля при скасуванні
                        loginEmail = ""
                        loginPassword = ""
                    }
                ) {
                    Text("Скасувати")
                }
            }
        )
    }

    // Діалог реєстрації
    if (showRegisterDialog) {
        AlertDialog(
            onDismissRequest = { showRegisterDialog = false },
            title = { Text("Реєстрація") },
            text = {
                Column {
                    OutlinedTextField(
                        value = registerName,
                        onValueChange = { registerName = it },
                        label = { Text("Ім'я") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = registerEmail,
                        onValueChange = { registerEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = registerPassword,
                        onValueChange = { registerPassword = it },
                        label = { Text("Пароль") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRegisterDialog = false
                        isLoggedIn = true
                        // Скидаємо поля після реєстрації
                        registerName = ""
                        registerEmail = ""
                        registerPassword = ""
                    }
                ) {
                    Text("Зареєструватися")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showRegisterDialog = false
                        // Скидаємо поля при скасуванні
                        registerName = ""
                        registerEmail = ""
                        registerPassword = ""
                    }
                ) {
                    Text("Скасувати")
                }
            }
        )
    }

    // Діалог налаштувань
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Налаштування") },
            text = {
                Column {
                    // Тут можна додати додаткові налаштування
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Діалог допомоги
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Зв'язатися з нами") },
            text = {
                Column {
                    Text("Електронна пошта для зв'язку:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("noname1693693963@gmail.com")
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Діалог підтвердження видалення
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Видалення профілю") },
            text = {
                Text("Ви впевнені, що хочете видалити свій профіль? Ця дія незворотна.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        isLoggedIn = false
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Видалити")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Скасувати")
                }
            }
        )
    }
}

@Composable
private fun AchievementItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isUnlocked: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isUnlocked) 
                MaterialTheme.colorScheme.onSecondaryContainer 
            else 
                MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isUnlocked) 
                MaterialTheme.colorScheme.onSecondaryContainer 
            else 
                MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
        )
    }
}