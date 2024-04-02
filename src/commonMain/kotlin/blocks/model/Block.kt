package blocks.model

sealed class Block(x: Int, y: Int) {

    data class Ant(val x: Int, val y: Int) : Block(x, y)

    data class Pebble(val x: Int, val y: Int) : Block(x, y)

}
