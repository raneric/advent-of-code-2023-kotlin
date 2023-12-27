package Day11

import println
import readInput
import kotlin.math.abs

private const val TEST_DATA = false
private const val TEST_FILE = "Day11/Day11_test"
private const val INPUT_FILE = "Day11/Day11"

private val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)

fun main() {

    fun part1(): Long {
        val universe = expandedUniverse(2)
        return universe.getSumGalaxyDistance()
    }

    fun part2(): Long {
        val universe = expandedUniverse(1_000_000)
        return universe.getSumGalaxyDistance()
    }

    part1().println()
    part2().println()
}

data class Universe(val galaxies: List<Galaxy>) {
    fun getSumGalaxyDistance(): Long {
        var sum = 0L

        for (i in 0..galaxies.lastIndex) {
            for (j in i + 1..galaxies.lastIndex) {
                sum += galaxies[i].position distanceTo galaxies[j].position
            }
        }

        return sum
    }
}

data class Galaxy(val position: Position)

data class Position(val x: Int, val y: Int) {
    infix fun distanceTo(otherPosition: Position): Int {
        return abs(otherPosition.x - x) + abs(otherPosition.y - y)
    }
}

private fun expandedUniverse(expansionFactor: Int): Universe {
    var rowExpansionFactor = 0
    val galaxies: MutableList<Galaxy> = emptyList<Galaxy>().toMutableList()

    for (i in 0..input.lastIndex) {
        var columnExpansionFactor = 0
        if (!input[i].contains('#')) {
            rowExpansionFactor += expansionFactor - 1
        }
        for (j in 0..input[i].lastIndex) {
            if (!input.columnContainGalaxy(j)) {
                columnExpansionFactor += expansionFactor - 1
            }
            if (input[i][j] == '#') {
                galaxies.add(Galaxy(Position(j + columnExpansionFactor, i + rowExpansionFactor)))
            }
        }
    }
    return Universe(galaxies)
}

private fun List<String>.columnContainGalaxy(column: Int): Boolean {
    var containGalaxy = false
    for (i in 0..this.lastIndex) {
        if (this[i][column] == '#') {
            containGalaxy = true
        }
    }
    return containGalaxy
}