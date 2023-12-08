package Day05

import println
import readInput
import kotlin.streams.asStream

private val TEST_DATA = true
private const val TEST_FILE = "Day05/Day05_test"
private const val INPUT_FILE = "Day05/Day05"
val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)
val almanacTitle = listOf(
        "seed-to-soil map:",
        "soil-to-fertilizer map:",
        "fertilizer-to-water map:",
        "water-to-light map:",
        "light-to-temperature map:",
        "temperature-to-humidity map:",
        "humidity-to-location map:")

fun main() {
    val seeds = extractSeeds()
    val mapper = stepList(almanacTitle)
    val game = Garden(seeds, mapper)

    fun part1(): Long {
        game.findNearestLocation()
        return game.lowestLocation
    }

    fun part2(): Long {
        game.findNearestLocationFromRange()
        return game.lowestRangeLocation
    }

    part1().println()
    part2().println()
}

class Garden(private val seeds: Seeds, private val mappers: List<MapperClass>) {

    private val seedsRange
        get() = seedsRange()

    private var _lowestLocation = Long.MAX_VALUE
    val lowestLocation
        get() = _lowestLocation

    private var _lowestRangeLocation = Long.MAX_VALUE
    val lowestRangeLocation
        get() = _lowestRangeLocation

    fun findNearestLocation() {
        seeds.seedNumbers.forEach { seed ->
            _lowestLocation = findMinInMappedValue(seed, lowestLocation)
        }
    }

    fun findNearestLocationFromRange() {
        for (range in seedsRange) {
            range.asSequence().asStream().parallel().forEach { seed ->
                _lowestRangeLocation = findMinInMappedValue(seed, lowestRangeLocation)
            }
        }
    }

    private fun findMinInMappedValue(seed: Long, currentMinValue: Long): Long {
        var convertedValue = seed
        for (map in mappers) {
            convertedValue = map.findCorrespondingValue(convertedValue)
        }
        return currentMinValue.coerceAtMost(convertedValue)
    }

    private fun seedsRange(): List<LongRange> {
        return seeds.seedNumbers.windowed(2, 2).map {
            LongRange(it[0], it[0] + it[1] - 1)
        }
    }
}

data class Seeds(val seedNumbers: List<Long>)

class MapperClass(private val mappingData: List<Triple<Long, Long, Long>>) {

    fun findCorrespondingValue(valueToFind: Long): Long {
        var result = valueToFind
        for (data in mappingData) {
            if (findRange(data.second, data.third).contains(valueToFind)) {
                result = data.first + valueToFind - data.second
                break
            }
        }
        return result
    }

    private fun findRange(start: Long, nb: Long): LongRange {
        val end = start + nb - 1
        return start..end
    }
}

fun extractSeeds(): Seeds {
    val seedLine = input[0]
    val seedsList = seedLine.split(" ").filter { it[0].isDigit() }.map { it.toLong() }
    return Seeds(seedsList)
}

fun stepList(almanacTitle: List<String>): List<MapperClass> {

    return almanacTitle.map {
        extractData(it, input)
    }.toList()
}

fun extractData(item: String, data: List<String>): MapperClass {
    val intData = emptyList<Triple<Long, Long, Long>>().toMutableList()
    for (i in data.indexOf(item) + 1..data.lastIndex) {
        if (data[i] == "") break
        val tempList = data[i].split(" ").map { it.toLong() }
        intData.add(Triple(tempList[0], tempList[1], tempList[2]))
    }
    intData.sortBy { it.second }
    return MapperClass(intData)
}


