package Day09

import println
import readInput

private const val TEST_DATA = false
private const val TEST_FILE = "Day09/Day09_test"
private const val INPUT_FILE = "Day09/Day09"

private val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)


fun main() {
    val data = extractData()

    fun part1(): Int {
        return data.map {
            predictValue(it)
        }.sumOf { it }
    }

    fun part2(): Int {

        return data.map {
            extrapolateBackward(it)
        }.sumOf { it }
    }

    part1().println()
    part2().println()
}

fun predictValue(row: List<Int>): Int {
    val predictionData = listOf(row).toMutableList()
    getDataHistory(row, predictionData)
    val interestedValue = predictionData.map { it.last() }.reversed()
    return interestedValue.reduce { acc, ints ->
        acc + ints
    }
}

fun extrapolateBackward(row: List<Int>): Int {
    val predictionData = listOf(row).toMutableList()
    getDataHistory(row, predictionData)
    val interestedValue = predictionData.map { it.first() }.reversed()
    return interestedValue.reduce { acc, ints ->
        ints - acc
    }
}

fun getDataHistory(previousData: List<Int>, predictionData: MutableList<List<Int>>): List<Int> {
    val result = previousData.windowed(2, 1).map {
        it[1] - it[0]
    }
    predictionData.add(result)
    val isResultZero = result.map { it == 0 }.reduce { acc, current -> acc && current }
    return if (isResultZero) return result else getDataHistory(result, predictionData)
}

private fun extractData(): List<List<Int>> {
    return input.map { rowString ->
        rowString.split(" ").map {
            it.toInt()
        }
    }
}