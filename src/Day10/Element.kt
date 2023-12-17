package Day10


sealed class Element() {

    data class Earth(val position: Position) : Element()

    data class Pipe(
        val shape: PipeShape,
        val position: Position,
    ) : Element()
}