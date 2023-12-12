package Day08

import println
import readInput
import kotlin.math.cbrt
import kotlin.streams.asStream


private const val TEST_DATA = false
private const val TEST_FILE = "Day08/Day08_test"
private const val INPUT_FILE = "Day08/Day08"

val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)


fun main() {
    val maps: Maps = extractNodes(input)
    fun part1(): Int {
        return maps.reachOutSteps()
    }

    fun part2(): Long {

        return maps.reachOutGhostSteps()
    }

    part1().println()
    part2().println()
}

data class Node(val left: String, val right: String)

class Maps(val instructions: String, val maps: MutableMap<String, Node>) {

    fun reachOutSteps(): Int {
        var nextNode = START_NODE
        var step = 0
        do {
            for (instruction in instructions) {
                val node = maps[nextNode]
                step++
                nextNode = if (instruction == 'L') node!!.left else node!!.right
                if (nextNode == OUT_NODE) break
            }
        } while (nextNode != OUT_NODE)

        return step
    }

    fun reachOutGhostSteps(): Long {
        val currentNode = getStartNode().toMutableList()
        var step = 0L
        do {
            for (instruction in instructions) {
                for (i in 0..currentNode.lastIndex) {
                    val node = maps[currentNode[i]]
                    currentNode[i] = if (instruction == 'L') node!!.left else node!!.right
                }
                step++
                if (currentNode.allEndWithZ()) break
            }
            println(step)
        } while (!currentNode.allEndWithZ())
        return step
    }

    private fun getStartNode(): List<String> {
        return maps.keys.filter {
            it.endsWith('A')
        }
    }

    private fun List<String>.allEndWithZ(): Boolean {
        return this.map {
            it.endsWith('Z')
        }.reduce { acc, current ->
            current && acc
        }
    }

    companion object {
        const val OUT_NODE = "ZZZ"
        const val START_NODE = "AAA"
    }
}


fun extractNodes(input: List<String>): Maps {
    val instruction = input[0]
    val nodeList = input.subList(2, input.size)
    val maps = Maps(instruction, emptyMap<String, Node>().toMutableMap())
    nodeList.map { rowData ->
        val row = rowData.split(" = ")
        val nodeData = row[1].split(", ").map { it.trimStart('(').trimEnd(')') }
        maps.maps.put(row[0], Node(nodeData[0], nodeData[1]))
    }
    return maps
}
