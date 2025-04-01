package com.example.calmstudy.ui.games

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class Card(
    val id: Int,
    val emoji: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false,
    var isAnimating: Boolean = false
)

enum class Difficulty(val pairsCount: Int, val timeLimit: Duration) {
    EASY(8, 120.seconds),
    MEDIUM(12, 180.seconds),
    HARD(16, 240.seconds)
}

data class GameState(
    val cards: List<Card> = emptyList(),
    val score: Int = 0,
    val moves: Int = 0,
    val timeRemaining: Duration = Duration.ZERO,
    val isGameOver: Boolean = false,
    val difficulty: Difficulty = Difficulty.EASY,
    val highScore: Int = 0
)

class MemoryGame {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState

    private val emojis = listOf(
        "🎮", "🎲", "🎯", "🎨", "🎭", "🎪", "🎟️", "🎠",
        "🎸", "🎺", "🎻", "🎹", "🎼", "🎧", "🎤", "🎵"
    )
    private var selectedCards = mutableListOf<Card>()
    private var highScores = mutableMapOf<Difficulty, Int>()

    fun startGame(difficulty: Difficulty) {
        val pairs = difficulty.pairsCount
        val gameEmojis = emojis.take(pairs)
        val cards = (gameEmojis + gameEmojis).shuffled().mapIndexed { index, emoji ->
            Card(id = index, emoji = emoji)
        }

        _gameState.value = GameState(
            cards = cards,
            timeRemaining = difficulty.timeLimit,
            difficulty = difficulty,
            highScore = highScores[difficulty] ?: 0
        )
    }

    fun updateTimer() {
        val currentState = _gameState.value
        if (currentState.timeRemaining > Duration.ZERO && !currentState.isGameOver) {
            _gameState.value = currentState.copy(
                timeRemaining = currentState.timeRemaining - 1.seconds,
                isGameOver = currentState.timeRemaining <= 1.seconds
            )
        }
    }

    fun selectCard(card: Card): Boolean {
        val currentState = _gameState.value
        if (currentState.isGameOver ||
            card.isMatched ||
            card.isFlipped ||
            selectedCards.size >= 2) return false

        val updatedCard = card.copy(isFlipped = true, isAnimating = true)
        val updatedCards = currentState.cards.map {
            if (it.id == card.id) updatedCard else it
        }
        selectedCards.add(updatedCard)

        _gameState.value = currentState.copy(cards = updatedCards)

        if (selectedCards.size == 2) {
            val newState = currentState.copy(moves = currentState.moves + 1)
            _gameState.value = newState
            return checkMatch()
        }
        return false
    }

    private fun checkMatch(): Boolean {
        val (card1, card2) = selectedCards
        val isMatch = card1.emoji == card2.emoji
        val currentState = _gameState.value

        if (isMatch) {
            val updatedCards = currentState.cards.map { card ->
                when (card.id) {
                    card1.id, card2.id -> card.copy(isMatched = true, isAnimating = false)
                    else -> card
                }
            }
            val newScore = currentState.score + calculateScore(currentState.timeRemaining)
            _gameState.value = currentState.copy(
                cards = updatedCards,
                score = newScore,
                isGameOver = updatedCards.all { it.isMatched }
            )

            if (currentState.isGameOver && newScore > (highScores[currentState.difficulty] ?: 0)) {
                highScores[currentState.difficulty] = newScore
            }
        }

        return isMatch
    }

    private fun calculateScore(timeRemaining: Duration): Int {
        return (20 + (timeRemaining.inWholeSeconds / 2)).toInt()
    }

    fun unflipCards() {
        val currentState = _gameState.value
        val updatedCards = currentState.cards.map { card ->
            if (selectedCards.any { it.id == card.id }) {
                card.copy(isFlipped = false, isAnimating = false)
            } else card
        }
        _gameState.value = currentState.copy(cards = updatedCards)
        selectedCards.clear()
    }

    fun resetGame() {
        startGame(_gameState.value.difficulty)
    }

    fun changeDifficulty(difficulty: Difficulty) {
        startGame(difficulty)
    }
}