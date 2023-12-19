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

        var pipeShape1 = getStartingLoop(startingPipe, true)
        var pipeShape2 = getStartingLoop(startingPipe, false)

        do {
            stepCount++
            val currentShape1 = runThroughPipe(pipeShape1, previousPosition1)
            val currentShape2 = runThroughPipe(pipeShape2, previousPosition2)

            previousPosition1 = pipeShape1.position
            previousPosition2 = pipeShape2.position

            pipeShape1 = currentShape1
            pipeShape2 = currentShape2

        } while (pipeShape1 != pipeShape2)

        return stepCount
    }

    private fun getStartingLoop(
        shape: Element.Pipe,
        moveForward: Boolean,
    ): Element.Pipe {

        val (forward, backward) = move(shape)
        return if (moveForward) {
            val pipe = elements[forward!!.row][forward.column] as Element.Pipe
            pipe
        } else {
            val pipe = elements[backward!!.row][backward.column] as Element.Pipe
            pipe
        }
    }

    private fun runThroughPipe(
        currentPipe: Element.Pipe,
        previousPosition: Position,
    ): Element.Pipe {
        val (p1, p2) = move(currentPipe)
        return if (p1 == previousPosition) {
            val pipe = elements[p2!!.row][p2.column] as Element.Pipe
            pipe
        } else {
            val pipe = elements[p1!!.row][p1.column] as Element.Pipe
            pipe
        }
    }

    private fun move(currentPipe: Element.Pipe): Pair<Position?, Position?> {
        return when (currentPipe.shape) {
            PipeShape.VERTICAL      -> {
                val p1 = if (currentPipe.position.isAtEndingRow(elements.lastIndexOfRow))
                    null
                else
                    Position(currentPipe.position.row + 1, currentPipe.position.column)

                val p2 = if (currentPipe.position.isAtStartingRow())
                    null
                else
                    Position(
                        currentPipe.position.row - 1, currentPipe.position.column
                    )

                Pair(p1, p2)
            }

            PipeShape.HORIZONTAL    -> {
                val p1 = if (currentPipe.position.isAtEndingColumn(elements.lastIndexOfColumn))
                    null
                else
                    Position(currentPipe.position.row, currentPipe.position.column + 1)

                val p2 = if (currentPipe.position.isAtStartingColumn())
                    null
                else
                    Position(currentPipe.position.row, currentPipe.position.column - 1)

                Pair(p1, p2)
            }

            // L shape
            PipeShape.NORTH_TO_EAST -> {
                val p1 = if (currentPipe.position.isAtEndingColumn(elements.lastIndexOfColumn))
                    null
                else
                    Position(currentPipe.position.row, currentPipe.position.column + 1)

                val p2 = if (currentPipe.position.isAtStartingRow())
                    null
                else
                    Position(currentPipe.position.row - 1, currentPipe.position.column)

                Pair(p1, p2)
            }

            //J shape
            PipeShape.NORTH_TO_WEST -> {
                val p1 = if (currentPipe.position.isAtStartingColumn())
                    null
                else
                    Position(currentPipe.position.row, currentPipe.position.column - 1)

                val p2 = if (currentPipe.position.isAtStartingRow())
                    null
                else
                    Position(currentPipe.position.row - 1, currentPipe.position.column)
                Pair(p1, p2)
            }

            //7 shape
            PipeShape.SOUTH_TO_WEST -> {
                val p1 = if (currentPipe.position.isAtStartingColumn())
                    null
                else
                    Position(currentPipe.position.row, currentPipe.position.column - 1)

                val p2 = if (currentPipe.position.isAtEndingRow(elements.lastIndexOfRow))
                    null
                else
                    Position(currentPipe.position.row + 1, currentPipe.position.column)

                Pair(p1, p2)
            }

            //F shape
            PipeShape.SOUTH_TO_EAST -> {
                val p1 = if (currentPipe.position.isAtEndingColumn(elements.lastIndexOfColumn))
                    null
                else
                    Position(currentPipe.position.row, currentPipe.position.column + 1)

                val p2 = if (currentPipe.position.isAtEndingRow(elements.lastIndexOfRow))
                    null
                else
                    Position(currentPipe.position.row + 1, currentPipe.position.column)
                Pair(p1, p2)
            }
        }
    }
}

val List<List<Element>>.lastIndexOfRow: Int
    get() = this.lastIndex

val List<List<Element>>.lastIndexOfColumn: Int
    get() = this[0].lastIndex