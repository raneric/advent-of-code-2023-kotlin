package Day07

import println
import readInput

private const val TEST_DATA = false
private const val TEST_FILE = "Day07/Day07_test"
private const val INPUT_FILE = "Day07/Day07"
private val strengthWithJoker = mapOf(
        'A' to 13,
        'K' to 12,
        'Q' to 11,
        'J' to 1,
        'T' to 10
)
private val strengthWithoutJoker = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to 11,
        'T' to 10
)

val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)

fun main() {
    fun part1(): Int {
        val dataInput = extractData(input)
        val cardListByRank = dataInput.sortedByDescending { it }
        return getTotalWinnings(cardListByRank)
    }

    fun part2(): Int {
        val dataInput = extractDataWithJoker(input)
        val cardListByRank = dataInput.sortedByDescending { it }
        return getTotalWinnings(cardListByRank)
    }

    part1().println()
    part2().println()
}

data class Hand(val card: String,
                val bid: Int,
                val cardRank: Int,
                val withJoker: Boolean = false
) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        return if (this.cardRank > other.cardRank) {
            1
        } else if (this.cardRank < other.cardRank) {
            -1
        } else {
            this.card.findFirstHigherCard(other.card, withJoker)
        }
    }
}

private fun getTotalWinnings(hands: List<Hand>): Int {
    var rank = hands.size
    return hands.fold(0) { acc, hand ->
        val tempRank = rank
        rank--
        acc + (hand.bid * tempRank)
    }
}

private fun String.findFirstHigherCard(other: String, withJoker: Boolean): Int {
    var result = 0
    for (i in 0..this.lastIndex) {
        if (this[i] == other[i]) continue
        if (this[i].getCardStrength(withJoker) > other[i].getCardStrength(withJoker)) {
            result = 1
            break
        } else {
            result = -1
            break
        }
    }
    return result
}

private fun Char.getCardStrength(withJoker: Boolean): Int {
    val cardStrength = if (withJoker) strengthWithJoker else strengthWithoutJoker
    return if (this.isDigit()) {
        this.digitToInt()
    } else {
        cardStrength[this]!!
    }
}

private fun String.findHandType(): Int {
    val visitedChar = emptyMap<Char, Int>().toMutableMap()
    this.forEach { charItem ->
        if (charItem in visitedChar) {
            visitedChar[charItem] = visitedChar[charItem]!! + 1
        } else {
            visitedChar[charItem] = 1
        }
    }
    return visitedChar.findHandScore()
}

private fun Map<Char, Int>.findHandScore(): Int {
    val maxCardNb = this.values.maxOf { it }
    return when (maxCardNb) {
        5    -> {
            7
        }

        4    -> {
            6
        }

        3    -> {
            if (this.size == 2) 5 else 4
        }

        2    -> {
            if (this.size == 3) 3 else 2
        }

        else -> {
            1
        }
    }
}

private fun extractData(input: List<String>): List<Hand> {
    return input.map {
        val result = it.split(" ")
        Hand(card = result[0], bid = result[1].toInt(), cardRank = result[0].findHandType())
    }
}

private fun extractDataWithJoker(input: List<String>): List<Hand> {
    return input.map { handInput ->
        val result = handInput.split(" ")
        val handWithJoker = result[0].replaceJoker()
        Hand(card = result[0], bid = result[1].toInt(), cardRank = handWithJoker.findHandType(), withJoker = true)
    }
}

private fun String.replaceJoker(): String {
    var charToAdd = '_'
    var result = "${this[0]}"
    var found = false
    for (i in 1..this.lastIndex) {
        if (this[i] == 'J' && !found) {
            charToAdd = this[i - 1]
            found = true
        } else if (this[i] != 'J') {
            charToAdd = this[i]
            found = false
        }
        result += charToAdd
    }
    return result
}