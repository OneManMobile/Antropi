package model

/**
 * An Ant can move around freely on the grid, and it can choose to carry the color it stand on
 * And swap it with any adjecent color
 *
 * To see the chaos available to the Ant:
 * 1. Take a ChaosAnt
 * 2. Make a list of any possible move from position
 * 3. From those moves, determine the move that allows maximum access to future entropy
 * 4. Determine maximum access by: Moving ChaosAnt
 */
data class Ant(
    val x: Int,
    val y: Int,
)
