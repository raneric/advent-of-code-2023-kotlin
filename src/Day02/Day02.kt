package Day02

import println
import readInput

const val TEST_DATA = false
const val TEST_FILE = "Day02/Day02_test"
const val INPUT_FILE = "Day02/Day02"
const val blueCubes = 14
const val greenCubes = 13
const val redCubes = 12

fun main() {
    val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)

    fun part1(input: List<String>): Int {
        return countPossibleGame(input.gamesAsMap())
    }

    fun part2(input: List<String>): Int {
        return sumOfMinimumPower(input.gamesAsMap())
    }

    part1(input).println()
    part2(input).println()
}

private fun countPossibleGame(gameData: Map<Int, List<List<String>>>): Int {
    var sumOfGameID = 0

    gameData.forEach { game ->
        var possibleGame = true
        for (hand in game.value) {
            if (!hand.isPossible()) {
                possibleGame = false
                break
            }
        }
        if (possibleGame) {
            sumOfGameID += game.key
        }
    }

    return sumOfGameID
}

private fun sumOfMinimumPower(gameData: Map<Int, List<List<String>>>): Int {
    var sumOfPower = 0

    gameData.forEach { game ->

        var maxBlue = 0
        var maxRed = 0
        var maxGreen = 0

        for (hand in game.value) {
            hand.forEach { cube ->
                when (cube.getCubesColor()) {
                    "blue" -> {
                        maxBlue = cube.getCubesCount().coerceAtLeast(maxBlue)
                    }

                    "red" -> {
                        maxRed = cube.getCubesCount().coerceAtLeast(maxRed)
                    }

                    "green" -> {
                        maxGreen = cube.getCubesCount().coerceAtLeast(maxGreen)
                    }
                }
            }
        }
        sumOfPower += maxBlue * maxGreen * maxRed
    }
    return sumOfPower
}

private fun List<String>.isPossible(): Boolean {
    var blueOk = true
    var redOk = true
    var greenOk = true

    this.forEach { cube ->
        when (cube.getCubesColor()) {
            "blue" -> {
                blueOk = cube.getCubesCount() <= blueCubes
            }

            "red" -> {
                redOk = cube.getCubesCount() <= redCubes
            }

            "green" -> {
                greenOk = cube.getCubesCount() <= greenCubes
            }
        }
    }

    return blueOk && redOk && greenOk
}

private fun String.getCubesCount(): Int {
    return this.split(" ").first().toInt()
}

private fun String.getCubesColor(): String {
    return this.substring(this.indexOf(" ")).trimStart()
}

private fun List<String>.gamesAsMap(): Map<Int, List<List<String>>> {
    val data = emptyMap<Int, List<List<String>>>().toMutableMap()
    this.map { row ->
        val id = row.substring(0, row.indexOf(":")).substring(row.indexOf(" ")).trimStart().toInt()
        val rowList = row.substring(row.indexOf(":") + 2)
                .split(";")
                .map { gameRow ->
                    gameRow.split(",")
                            .map { it.trimEnd().trimStart() }
                }
        data[id] = rowList.toList()
    }
    return data
}