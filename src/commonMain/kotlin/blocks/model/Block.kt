package blocks.model

sealed class Block(open val x: Int, open val y: Int) {

    data class Ant(override val x: Int, override val y: Int) : Block(x, y)

    data class Pebble(override val x: Int, override val y: Int) : Block(x, y)

}
