package Day03

import println
import readInput

private val TEST_DATA = false
private const val TEST_FILE = "Day03/Day03_test"
private const val INPUT_FILE = "Day03/Day03"
val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)
val templateRegex = "\\W?\\d+\\W?".toRegex()
val regexHorizontalGear = "\\d+\\*\\d+".toRegex()

fun main() {

    fun part1(input: List<String>): Int {

        return input.mapIndexed { index, row ->
            row.findAdjacentInRow(index, input)
        }.sumOf { it }
    }

    fun part2(input: List<String>): Int {

        val sumOfRow = input.map { row ->
            calculateGearRatioInRow(row)
        }.sumOf { it }

        val sumOfTopAndBottom = input.windowed(size = 3, step = 1) {
            calculateTopAndBottom(it)
        }.sumOf { it }

        return sumOfRow + sumOfTopAndBottom
    }

    part1(input).println()
    part2(input).println()
}

private fun calculateGearRatioInRow(row: String): Int {
    return regexHorizontalGear
            .findAll(row)
            .map {
                it.value.split("*").map { gearNumber ->
                    gearNumber.toInt()
                }.reduce { acc, next -> acc * next }
            }.sumOf { it }
}

private fun calculateTopAndBottom(data: List<String>): Int {
    var result = 0
    data[1].forEachIndexed { starPosition, element ->
        if (element == '*') {
            result += getIfHaveGear(starPosition, data)
        }
    }

    return result
}

fun getIfHaveGear(starPosition: Int, data: List<String>): Int {
    val topRow = data[0]
    val bottomRow = data[2]
    val startIndex = starPosition - 1

    val toValue = findGearNumber(startIndex, topRow)
    val bottomValue = findGearNumber(startIndex, bottomRow)

    return toValue * bottomValue
}

fun findGearNumber(startIndex: Int, topRow: String): Int {
    var digits = ""
    var digitFound = false
    var value = 0

    for (index in startIndex..startIndex + 2) {
        if (!(index >= 0 && topRow[index].isDigit())) continue

        var digitPosition = 0
        for (i in index downTo 0) {
            if (topRow[i].isDigit()) {
                digitPosition = i
                digitFound = true
            } else {
                break
            }
        }
        if (digitFound) {
            for (i in digitPosition..topRow.lastIndex) {
                if (topRow[i].isDigit()) {
                    digits += topRow[i]
                } else {
                    break
                }
            }
            value = digits.toInt()
            digits = ""
            digitFound = false
        }
    }

    return value
}

fun String.findAdjacentInRow(index: Int, data: List<String>): Int {
    var valueInRow = 0
    var top = ""
    var bottom = ""
    val availableValue = this.findAllMatchNumber()
    availableValue.forEach { matchNumber ->
        if (index > 0) {
            top = data[index - 1].substring(matchNumber.key, matchNumber.key + matchNumber.value.length)
        }

        if (index < data.lastIndex) {
            bottom = data[index + 1].substring(matchNumber.key, matchNumber.key + matchNumber.value.length)
        }

        if (matchNumber.value.containSymbol() || top.containSymbol() || bottom.containSymbol()) {
            valueInRow += matchNumber.value.extractNumber()
        }
    }
    return valueInRow
}

private fun String.findAllMatchNumber(): Map<Int, String> {
    var startDigit = true
    var endDigit = false
    var resultList = emptyList<String>().toMutableList()
    var tempValue = ""

    var result = emptyMap<Int, String>().toMutableMap()
    var mapKey = 0

    for (i in this.indices) {
        if (this[i].isDigit()) {
            if (startDigit && i > 0) {
                mapKey = i - 1
                tempValue += this[i - 1]
            }
            startDigit = false
            endDigit = true
            tempValue += this[i]
            if (i == this.lastIndex) {
                resultList.add(tempValue)
                result[mapKey] = tempValue
                continue
            }
        } else if (!this[i].isDigit() && endDigit) {
            tempValue += this[i]
            resultList.add(tempValue)
            result[mapKey] = tempValue
            endDigit = false
            startDigit = true
            tempValue = ""
        }
    }
    return result
}

private fun String.extractNumber(): Int {
    return this.filter { it.isDigit() }.toInt()
}

private fun String.containSymbol(): Boolean {
    val regexSymbol = "\\W".toRegex()
    val removedDot = this.replace(".", "")
    return regexSymbol.containsMatchIn(removedDot)
}
