package com.example.calmstudy.ui.games

data class MemoryCard(
    val id: Int,
    val emoji: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

class MemoryGame {
    private val emojis = listOf("A", "B", "C", "D", "E", "F", "G", "H")  // Замінили емодзі на літери
    private var cards = mutableListOf<MemoryCard>()
    private var selectedCards = mutableListOf<MemoryCard>()
    private var score = 0
    private var moves = 0

    init {
        resetGame()
    }

    fun resetGame() {
        cards.clear()
        for (symbol in emojis) {
            cards.add(MemoryCard(cards.size, symbol))
            cards.add(MemoryCard(cards.size, symbol))
        }
        cards.shuffle()
        selectedCards.clear()
        score = 0
        moves = 0
    }

    fun selectCard(card: MemoryCard): Boolean {
        if (card.isMatched || card.isFlipped || selectedCards.size >= 2) return false

        card.isFlipped = true
        selectedCards.add(card)

        if (selectedCards.size == 2) {
            moves++
            return checkMatch()
        }
        return false
    }

    private fun checkMatch(): Boolean {
        val (card1, card2) = selectedCards
        val isMatch = card1.emoji == card2.emoji

        if (isMatch) {
            card1.isMatched = true
            card2.isMatched = true
            score += 10
        }

        return isMatch
    }

    fun unflipCards() {
        selectedCards.forEach { it.isFlipped = false }
        selectedCards.clear()
    }

    fun getCards(): List<MemoryCard> = cards
    fun getScore(): Int = score
    fun getMoves(): Int = moves
    fun isGameComplete(): Boolean = cards.all { it.isMatched }
}