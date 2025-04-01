package com.example.calmstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class SurveyQuestion(
    val question: String,
    val options: List<String>,
    var selectedAnswer: Int = -1
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(navController: NavController) {
    var questions by remember {
        mutableStateOf(
            listOf(
                SurveyQuestion(
                    "Як ти оцінюєш свій рівень стресу сьогодні?",
                    listOf("😌 Майже без стресу", "😐 Помірний стрес", "😟 Високий рівень стресу")
                ),
                SurveyQuestion(
                    "Скільки ти сьогодні відпочивав(-ла) під час навчання?",
                    listOf("⏳ Часто робив(-ла) перерви", "🔄 Декілька разів відпочивав(-ла)", "🏃‍♂️ Весь день був(-ла) у русі без відпочинку")
                ),
                SurveyQuestion(
                    "Як би ти описав(-ла) свою мотивацію сьогодні?",
                    listOf("🔥 Висока — хотілося вчитися", "😐 Нейтральна — змушував(-ла) себе", "💤 Взагалі не було сил і бажання")
                ),
                SurveyQuestion(
                    "Як минув твій день у спілкуванні?",
                    listOf("🗣️ Було приємне спілкування", "🤐 Майже ні з ким не говорив(-ла)", "🚫 Було напружене спілкування")
                ),
                SurveyQuestion(
                    "Чи відчував(-ла) ти втому протягом дня?",
                    listOf("💪 Ні, енергії вистачало", "😴 Трохи втомлювався(-лася)", "💤 Був(-ла) виснажений(-на)")
                ),
                SurveyQuestion(
                    "Що найбільше вплинуло на твій стан сьогодні?",
                    listOf("🏫 Навчальні навантаження", "🤯 Особисті переживання", "🤩 Хороші моменти, які підняли настрій", "💤 Недосипання")
                ),
                SurveyQuestion(
                    "Чи займався(-лася) ти сьогодні чимось для розслаблення?",
                    listOf("🎶 Слухав(-ла) музику", "🏃‍♂️ Робив(-ла) фізичні вправи", "🎨 Займався(-лася) хобі", "❌ Нічого не робив(-ла)")
                )
            )
        )
    }

    var showResults by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Твій навчальний день") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!showResults) {
                questions.forEachIndexed { index, question ->
                    QuestionCard(
                        question = question,
                        onAnswerSelected = { answer ->
                            questions = questions.toMutableList().also {
                                it[index] = question.copy(selectedAnswer = answer)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (questions.all { it.selectedAnswer != -1 }) {
                    Button(
                        onClick = { showResults = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Подивитися результати")
                    }
                }
            } else {
                ResultsCard(questions = questions)
                
                Button(
                    onClick = { 
                        questions = questions.map { it.copy(selectedAnswer = -1) }
                        showResults = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Пройти опитування знову")
                }
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: SurveyQuestion,
    onAnswerSelected: (Int) -> Unit
) {
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
                text = question.question,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            question.options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = question.selectedAnswer == index,
                        onClick = { onAnswerSelected(index) }
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResultsCard(questions: List<SurveyQuestion>) {
    val goodAnswers = questions.count { it.selectedAnswer == 0 }
    val mediumAnswers = questions.count { it.selectedAnswer == 1 }
    val badAnswers = questions.count { it.selectedAnswer == 2 }
    
    val recommendation = when {
        goodAnswers >= maxOf(mediumAnswers, badAnswers) -> 
            "✅ Молодець! Продовжуй у тому ж дусі. Можеш виділити хвилинку на улюблену музику чи спокійне споглядання природи, щоб ще більше закріпити гарний настрій."
        mediumAnswers >= maxOf(goodAnswers, badAnswers) -> 
            "☕ Ти добре впорався(-лася), але тобі потрібно більше відпочивати. Спробуй сьогодні ввечері розслабитися: гарячий чай, приємна музика чи навіть коротка прогулянка допоможуть."
        else -> 
            "🛑 Тобі варто подбати про себе. Глибоке дихання, 10-хвилинна медитація або тепла ванна можуть допомогти. Завтра спробуй зробити хоча б одну маленьку приємну річ для себе."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📊 Аналіз настрою та рекомендації",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = recommendation,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Статистика відповідей
            Text(
                text = "Статистика відповідей:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text("😊 Позитивні відповіді: $goodAnswers")
            Text("😐 Нейтральні відповіді: $mediumAnswers")
            Text("😟 Негативні відповіді: $badAnswers")
        }
    }
} 