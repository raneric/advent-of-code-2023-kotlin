package Day10

class Area(
    val elements: List<List<Element>>,
    val startingPoint: Position,
) {

    private val startingPipe = elements[startingPoint.row][startingPoint.column] as Element.Pipe

    fun getFarthestStep(): Int {
        var stepCount = 1

        var previousPosition1 = startingPoint
        var previousPosition2 = startingPoint

        var (pipeShape1, position1) = getStartingLoop(startingPipe.shape, startingPoint, true)
        var (pipeShape2, position2) = getStartingLoop(startingPipe.shape, startingPoint, false)

        do {
            stepCount++
            val (currentShape1, currentPosition1) = runThroughPipe(pipeShape1, position1, previousPosition1)
            val (currentShape2, currentPosition2) = runThroughPipe(pipeShape2, position2, previousPosition2)

            previousPosition1 = position1
            previousPosition2 = position2

            pipeShape1 = currentShape1
            position1 = currentPosition1!!

            pipeShape2 = currentShape2
            position2 = currentPosition2!!
        } while (position1 != position2)

        return stepCount
    }

    fun countTiles(): Int {
        val pipeLoopList = emptyList<Element.Pipe>().toMutableList()

        var (pipeShape, position) = getStartingLoop(startingPipe.shape, startingPoint, true)
        var previousPosition = startingPoint

        do {
            val (currentShape, currentPosition) = runThroughPipe(pipeShape, position, previousPosition)

            previousPosition = position

            pipeShape = currentShape
            position = currentPosition!!

            pipeLoopList.add(currentShape)
        } while (currentPosition != startingPoint)

        return 0
    }

    private fun getStartingLoop(
        shape: PipeShape,
        startingPoint: Position,
        moveForward: Boolean,
    ): Pair<Element.Pipe, Position> {

        val (forward, backward) = move(startingPoint, shape)
        return if (moveForward) {
            val pipe = elements[forward!!.row][forward.column] as Element.Pipe
            Pair(pipe, forward)
        } else {
            val pipe = elements[backward!!.row][backward.column] as Element.Pipe
            Pair(pipe, backward)
        }
    }

    private fun runThroughPipe(
        currentPipe: Element.Pipe,
        currentPosition: Position,
        previousPosition: Position,
    ): Pair<Element.Pipe, Position?> {
        val (p1, p2) = move(currentPosition, currentPipe.shape)
        return if (p1 == previousPosition) {
            val pipe = elements[p2!!.row][p2.column] as Element.Pipe
            Pair(pipe, p2)
        } else {
            val pipe = elements[p1!!.row][p1.column] as Element.Pipe
            Pair(pipe, p1)
        }
    }

    private fun move(currentPosition: Position, shape: PipeShape): Pair<Position?, Position?> {
        return when (shape) {
            PipeShape.VERTICAL      -> {
                val p1 = if (currentPosition.isAtEndingRow(elements.lastIndexOfRow))
                    null
                else
                    Position(currentPosition.row + 1, currentPosition.column)

                val p2 = if (currentPosition.isAtStartingRow())
                    null
                else
                    Position(
                        currentPosition.row - 1, currentPosition.column
                    )

                Pair(p1, p2)
            }

            PipeShape.HORIZONTAL    -> {
                val p1 = if (currentPosition.isAtEndingColumn(elements.lastIndexOfColumn))
                    null
                else
                    Position(currentPosition.row, currentPosition.column + 1)

                val p2 = if (currentPosition.isAtStartingColumn())
                    null
                else
                    Position(currentPosition.row, currentPosition.column - 1)

                Pair(p1, p2)
            }

            // L shape
            PipeShape.NORTH_TO_EAST -> {
                val p1 = if (currentPosition.isAtEndingColumn(elements.lastIndexOfColumn))
                    null
                else
                    Position(currentPosition.row, currentPosition.column + 1)

                val p2 = if (currentPosition.isAtStartingRow())
                    null
                else
                    Position(currentPosition.row - 1, currentPosition.column)

                Pair(p1, p2)
            }

            //J shape
            PipeShape.NORTH_TO_WEST -> {
                val p1 = if (currentPosition.isAtStartingColumn())
                    null
                else
                    Position(currentPosition.row, currentPosition.column - 1)

                val p2 = if (currentPosition.isAtStartingRow())
                    null
                else
                    Position(currentPosition.row - 1, currentPosition.column)
                Pair(p1, p2)
            }

            //7 shape
            PipeShape.SOUTH_TO_WEST -> {
                val p1 = if (currentPosition.isAtStartingColumn())
                    null
                else
                    Position(currentPosition.row, currentPosition.column - 1)

                val p2 = if (currentPosition.isAtEndingRow(elements.lastIndexOfRow))
                    null
                else
                    Position(currentPosition.row + 1, currentPosition.column)

                Pair(p1, p2)
            }

            //F shape
            PipeShape.SOUTH_TO_EAST -> {
                val p1 = if (currentPosition.isAtEndingColumn(elements.lastIndexOfColumn))
                    null
                else
                    Position(currentPosition.row, currentPosition.column + 1)

                val p2 = if (currentPosition.isAtEndingRow(elements.lastIndexOfRow))
                    null
                else
                    Position(currentPosition.row + 1, currentPosition.column)
                Pair(p1, p2)
            }
        }
    }
}

val List<List<Element>>.lastIndexOfRow: Int
    get() = this.lastIndex

val List<List<Element>>.lastIndexOfColumn: Int
    get() = this[0].lastIndex