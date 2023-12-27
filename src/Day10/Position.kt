package Day10


data class Position(val row: Int, val column: Int) {
    fun isAtStartingRow() = row == 0

    fun isAtStartingColumn() = column == 0

    fun isAtEndingRow(lastIndex: Int) = row == lastIndex

    fun isAtEndingColumn(lastIndex: Int) = column == lastIndex

    fun movedBack(previous: Position): Boolean = row < previous.row || column < previous.column
}