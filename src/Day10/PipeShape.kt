package Day10


enum class PipeShape {
    HORIZONTAL,
    VERTICAL,

    // J shape
    NORTH_TO_WEST,

    // L shape
    NORTH_TO_EAST,

    // 7 shape
    SOUTH_TO_WEST,

    //F shape
    SOUTH_TO_EAST;

    fun haveBottomConnection(): Boolean {
        return this == VERTICAL || this == SOUTH_TO_WEST || this == SOUTH_TO_EAST
    }

    fun haveLeftConnection(): Boolean {
        return this == HORIZONTAL || this == NORTH_TO_WEST || this == SOUTH_TO_WEST
    }

    fun haveRightConnection(): Boolean {
        return this == HORIZONTAL || this == NORTH_TO_EAST || this == SOUTH_TO_EAST
    }

    fun haveTopConnection(): Boolean {
        return this == VERTICAL || this == NORTH_TO_EAST || this == NORTH_TO_WEST
    }
}