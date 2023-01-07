package usecase

import ComplexityService
import GetPossibleNextWorldsUseCase
import model.*
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.random.*

class GetNextWorldUseCase {

    companion object{
        const val SAMPLE_PATHS = 100
        const val STEPS = 50

        var lastEntropy = 0.0
    }

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
    fun execute(input: World): World{

        // given step t+1, what is the access to entropy at Tau

        val bestWorld = getSetOfNextPossibleWorlds(input).maxBy { possibleNextWorld ->

            val accessibleEntropy = GetAccessibleEntropyFromWorld(possibleNextWorld)

            if(accessibleEntropy > lastEntropy){
                println("BETTER ENTROPY: " + accessibleEntropy)
                lastEntropy = accessibleEntropy
            }

            accessibleEntropy
        }

        return bestWorld

    }

    private fun GetAccessibleEntropyFromWorld(possibleNextWorld: World): Double {
        val paths = mutableListOf<List<World>>()

        for (i in 0..SAMPLE_PATHS) {

            val newPath = mutableListOf<World>()
            var nextWorld = getSetOfNextPossibleWorlds(possibleNextWorld, 1).get(0)

            for (j in 0..STEPS) {
                newPath.add(nextWorld)
                nextWorld = getSetOfNextPossibleWorlds(nextWorld, 1).get(0)
            }

            paths.add(newPath)
        }

        val entropy = paths.map {  it.map { it.grid } }.map {calculatePermutationEntropy(it, 3) }.fold(0.0){ acc: Double, num: List<Double> ->
            acc + num.reduce { acc, d -> acc+d }
        }
        // println("ENTROPY: " + entropyAvailable + " unique worlds: " + occurenceMap.size + " allWorldOccurences: " + allWorldOccurences)

        return entropy
    }


    private fun getSetOfNextPossibleWorlds(world: World, sampleSizeMax: Int = Integer.MAX_VALUE): List<World> {

        val ant = world.ant
        val lastIndex = world.grid.lastIndex

        val possibleWorlds = mutableListOf<World>(world)

        if(ant.x != 0){
            possibleWorlds.addAll(addAntMovement(ant.copy(x= ant.x - 1), world))
        }
        if(ant.x != lastIndex){
            possibleWorlds.addAll(addAntMovement(ant.copy(x= ant.x + 1), world))
        }
        if(ant.y != 0){
            possibleWorlds.addAll(addAntMovement(ant.copy(y =ant.y - 1), world))
        }
        if(ant.y != lastIndex){
            possibleWorlds.addAll(addAntMovement(ant.copy( y =ant.y + 1), world))
        }

        return possibleWorlds.shuffled().take(sampleSizeMax)
    }

    private fun addAntMovement(movedAnt: Ant, world: World): List<World>{

        val ant = world.ant

        val swappedGrid = world.grid.toMutableList().map { it.toMutableList() }

        val tempColor1 = swappedGrid[ant.x][ant.y]
        val tempColor2 = swappedGrid[movedAnt.x][movedAnt.y]
        swappedGrid[movedAnt.x][movedAnt.y] = tempColor1
        swappedGrid[ant.x][ant.y] = tempColor2

        return listOf<World>(
            world.copy(ant = movedAnt),
            world.copy(ant = movedAnt, grid = swappedGrid),
        )
    }

    fun calculatePermutationEntropy(grids: List<List<List<Int>>>, permutationLength: Int): List<Double> {
        // Initialize a list to store the permutation entropies of the grids
        val entropies = mutableListOf<Double>()

        // Iterate through the list of grids
        for (grid in grids) {
            // Initialize a map that maps each permutation to the number of times it appears in the grid
            val counts = mutableMapOf<List<Int>, Int>()

            // Iterate through the grid and extract the permutations
            for (row in 0 until grid.size) {
                for (col in 0 until grid[0].size) {
                    // Calculate the start and end indices of the window
                    val startRow = max(0, row - permutationLength / 2)
                    val endRow = min(grid.size, row + permutationLength / 2 + 1)
                    val startCol = max(0, col - permutationLength / 2)
                    val endCol = min(grid[0].size, col + permutationLength / 2 + 1)

                    // Extract the window of integers
                    val window = mutableListOf<Int>()
                    for (r in startRow until endRow) {
                        for (c in startCol until endCol) {
                            window.add(grid[r][c])
                        }
                    }

                    // Calculate the permutation of the integers in the window
                    val permutation = window.sorted()

                    // Update the count for this permutation
                    counts[permutation] = counts.getOrDefault(permutation, 0) + 1
                }
            }

            // Calculate the total number of permutations in the grid
            val total = counts.values.sum().toDouble()

            // Initialize the permutation entropy to zero
            var entropy = 0.0

            // Iterate through the map and calculate the permutation entropy for each permutation
            for ((permutation, count) in counts) {
                // Calculate the probability of occurrence for this permutation
                val probability = count / total

                // Calculate the permutation entropy for this permutation
                entropy += -probability * kotlin.math.ln(probability)
            }

            // Add the permutation entropy of the grid to the list
            entropies.add(entropy)
        }

        // Return the list of permutation entropies
        return entropies
    }


}
