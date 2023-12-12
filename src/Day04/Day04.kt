package Day04

import println
import readInput

private const val TEST_DATA = false
private const val TEST_FILE = "Day04/Day04_test"
private const val INPUT_FILE = "Day04/Day04"
val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)
val cardList: List<Card> = getAllData(input)

fun main() {
    val game = Game(cardList)

    fun part1(): Int = game.getGameScore()

    fun part2(): Int = game.getTotalScratchcards()

    part1().println()
    part2().println()
}

data class Card(val cardNumber: Int,
                val winingNumber: List<Int>,
                val myNumber: List<Int>) {
    val myMatchNumber
        get() = this.winingNumber.intersect(this.myNumber.toSet())
}

class Game(private val cards: List<Card>) {
    fun getGameScore(): Int {
        return cards.map { card ->
            card.myMatchNumber.getScore()
        }.sumOf { it }
    }

    fun getTotalScratchcards(): Int {
        var newCardInstance = cardList.associate { card ->
            card.cardNumber to mutableListOf(card)
        }
        for (cards in newCardInstance) {
            for (card in cards.value) {
                var iteration = 1
                repeat(card.myMatchNumber.size) {
                    newCardInstance[card.cardNumber + iteration]?.add(newCardInstance[card.cardNumber + iteration]!!.first())
                    iteration++
                }
            }
        }
        return newCardInstance.map { it.value.size }.sumOf { it }
    }

}

private fun Set<Int>.getScore(): Int {
    var result = if (this.size > 0) 1 else 0
    repeat(this.size - 1) {
        result *= 2
    }
    return result
}


fun getAllData(input: List<String>): List<Card> {
    return input.map {
        Card(cardNumber = it.getCardNUmber(),
                winingNumber = it.getWiningNumber(),
                myNumber = it.getMyNumber())
    }
}

private fun String.getCardNUmber(): Int {
    return this.substring(this.indexOf(" "), this.indexOf(":")).trimStart().toInt()
}

private fun String.getWiningNumber(): List<Int> {
    val winingNumber = this.substring(this.indexOf(":") + 1, this.indexOf('|')).trimEnd().trimStart()
    return winingNumber.split(" ").filter { it != "" }.map { it.toInt() }
}

private fun String.getMyNumber(): List<Int> {
    val myNumber = this.substring(this.indexOf("|") + 1).trimEnd().trimStart()
    return myNumber.split(" ").filter { it != "" }.map { it.toInt() }
}
