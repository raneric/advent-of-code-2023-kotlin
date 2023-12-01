package Day01

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { it.getFirstAndLastDigit() }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { it.getFirstAndLastDigitWithTxt() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01/Day01_test")

    val input = readInput("Day01/Day01")
    part1(input).println()
    part2(input).println()
}

private fun String.getFirstAndLastDigit(): Int {
    val firstDigit = this.first() { it.isDigit() }
    val lastDigit = this.last() { it.isDigit() }
    return "$firstDigit$lastDigit".toInt()
}


private fun String.getFirstAndLastDigitWithTxt(): Int {

    var firstIndex = Int.MAX_VALUE
    var lastIndex = Int.MIN_VALUE

    var firstDigit = 0
    var lastDigit = 0

    if (this.indexOfFirst { it.isDigit() } != -1) {
        firstDigit = this.first() { it.isDigit() }.digitToInt()
        lastDigit = this.last() { it.isDigit() }.digitToInt()

        firstIndex = this.indexOfFirst { it.isDigit() }
        lastIndex = this.indexOfLast { it.isDigit() }
    }

    stringDigit.forEachIndexed { index, value ->
        val firstStringIndex = this.indexOf(value)
        if (firstStringIndex != -1 && firstStringIndex < firstIndex) {
            firstIndex = firstStringIndex
            firstDigit = index + 1
        }

        val lastStringIndex = this.lastIndexOf(value)
        if (lastStringIndex != -1 && lastStringIndex > lastIndex) {
            lastIndex = lastStringIndex
            lastDigit = index + 1
        }
    }

    return "$firstDigit$lastDigit".toInt()
}

val stringDigit = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")