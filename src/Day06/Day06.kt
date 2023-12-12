package Day06

import println
import readInput

private const val TEST_DATA = false
private const val TEST_FILE = "Day06/Day06_test"
private const val INPUT_FILE = "Day06/Day06"
val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)
val dataForMultipleRace = extractMultipleRaceData(input)
val dataForOneRace = extractOneRaceData(input)


fun main() {
    fun part1(): Int {
        return beatRecordNumber(dataForMultipleRace)
    }

    fun part2(): Int {
        val accRange = LongRange(1, dataForOneRace.first)
        return accRange.getWinNUmber(dataForOneRace.second)
    }

    part1().println()
    part2().println()
}

private fun beatRecordNumber(data: Map<Int, Int>): Int {
    return data.map { (time, record) ->
        IntRange(1, time).getWinNUmber(record)
    }.reduce { acc, value -> acc * value }
}

// FIXME: TO REFACTOR USING GENERICS
private fun IntRange.getWinNUmber(record: Int): Int {
    var winChance = 0
    this.forEach { currentHoldTime ->
        val reachDistance = (this.last - currentHoldTime) * currentHoldTime
        if (reachDistance > record) winChance++
    }
    return winChance
}

// FIXME: TO REFACTOR USING GENERICS
private fun LongRange.getWinNUmber(record: Long): Int {
    var winChance = 0
    this.forEach { currentHoldTime ->
        val reachDistance = (this.last - currentHoldTime) * currentHoldTime
        if (reachDistance > record) winChance++
    }
    return winChance
}

private fun extractMultipleRaceData(input: List<String>): Map<Int, Int> {
    val time = input[0].split(" ").filter { it != "" && it[0].isDigit() }.map { it.toInt() }
    val distance = input[1].split(" ").filter { it != "" && it[0].isDigit() }.map { it.toInt() }
    return time.zip(distance).toMap()
}


fun extractOneRaceData(input: List<String>): Pair<Long, Long> {
    val time = input[0].split(" ").filter { it != "" && it[0].isDigit() }.joinToString("").toLong()
    val distance = input[1].split(" ").filter { it != "" && it[0].isDigit() }.joinToString("").toLong()
    return Pair(time, distance)
}
