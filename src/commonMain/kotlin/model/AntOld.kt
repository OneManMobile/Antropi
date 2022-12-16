package model

/**
 * An Ant can move around freely on the grid, and it can choose to carry the color it stand on
 * And swap it with any adjecent color
 */
data class AntOld(
    val x: Int,
    val y: Int,
    val swapOnMove: Boolean = false,
    val nextMove: Direction? = null
)
