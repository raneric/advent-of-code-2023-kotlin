package Day10

import println
import readInput

private const val TEST_DATA = false
private const val TEST_FILE = "Day10/Day10_test"
private const val INPUT_FILE = "Day10/Day10"

private val input = if (TEST_DATA) readInput(TEST_FILE) else readInput(INPUT_FILE)

fun main() {
    val area = extractData()
    fun part1(): Int {
        return area.getFarthestStep()
    }

    fun part2(): Int {
        return 0
    }

    part1().println()
    part2().println()
}

private fun extractData(): Area {
    val dataList = emptyList<MutableList<Element>>().toMutableList()
    var startingPointPosition: Position? = null

    input.mapIndexed { rowIndex, rowData ->

        val row = emptyList<Element>().toMutableList()

        rowData.mapIndexed { columnIndex, currentChar ->

            val currentPosition = Position(rowIndex, columnIndex)

            if (currentChar == '.') {
                row.add(columnIndex, Element.Earth(currentPosition))
            } else if (currentChar != 'S') {
                val pipeShape = getPipeShape(currentChar)
                row.add(columnIndex, Element.Pipe(shape = pipeShape!!, position = currentPosition))
            }

            if (currentChar == 'S') {
                startingPointPosition = currentPosition
                val pipeShape: PipeShape = getStartingPointShape(currentPosition)
                val startingPoint = Element.Pipe(pipeShape, currentPosition)
                row.add(columnIndex, startingPoint)
            }

        }
        dataList.add(rowIndex, row)
    }

    return Area(dataList, startingPointPosition!!)
}

private fun getStartingPointShape(currentPosition: Position): PipeShape {

    val connection = BooleanArray(4) { false }

    val leftShape =
        if (!currentPosition.isAtStartingColumn())
            getPipeShape(input[currentPosition.row][currentPosition.column - 1])
        else
            null

    val rightShape = if (!currentPosition.isAtEndingColumn(input[currentPosition.row].lastIndex))
        getPipeShape(input[currentPosition.row][currentPosition.column + 1])
    else
        null

    val topShape = if (!currentPosition.isAtStartingRow())
        getPipeShape(input[currentPosition.row - 1][currentPosition.column])
    else
        null


    val bottomShape = if (!currentPosition.isAtEndingRow(input.lastIndex))
        getPipeShape(input[currentPosition.row + 1][currentPosition.column])
    else
        null

    topShape?.let {
        connection[0] = topShape.haveBottomConnection()
    }

    rightShape?.let {
        connection[1] = rightShape.haveLeftConnection()
    }

    bottomShape?.let {
        connection[2] = bottomShape.haveTopConnection()
    }

    leftShape?.let {
        connection[3] = leftShape.haveRightConnection()
    }

    return if (connection[0] && connection[2]) {
        PipeShape.VERTICAL
    } else if (connection[1] && connection[3]) {
        PipeShape.HORIZONTAL
    } else if (connection[0] && connection[3]) {
        PipeShape.NORTH_TO_WEST
    } else if (connection[2] && connection[3]) {
        PipeShape.SOUTH_TO_WEST
    } else if (connection[0] && connection[1]) {
        PipeShape.NORTH_TO_EAST
    } else if (connection[2] && connection[1]) {
        PipeShape.SOUTH_TO_EAST
    } else {
        throw Exception("Invalid pipe schema")
    }
}


private fun getPipeShape(pipe: Char): PipeShape? {
    return when (pipe) {
        '|'  -> PipeShape.VERTICAL

        '-'  -> PipeShape.HORIZONTAL

        'L'  -> PipeShape.NORTH_TO_EAST

        'J'  -> PipeShape.NORTH_TO_WEST

        '7'  -> PipeShape.SOUTH_TO_WEST

        'F'  -> PipeShape.SOUTH_TO_EAST

        else -> {
            null
        }
    }
}

