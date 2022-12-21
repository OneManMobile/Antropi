package usecase

import ComplexityService
import GetPossibleNextWorldsUseCase
import model.*
import kotlin.random.*

class GetNextWorldUseCase {

    companion object{
        const val SAMPLE_PATHS = 100
        const val STEPS = 250

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

        val occurenceMap = mutableMapOf<World, Int>()
        paths.forEach {
            it.forEach {
                val currentCount = occurenceMap.get(it) ?: 0
                occurenceMap.put(it, currentCount + 1)
            }
        }

        val allWorldOccurences = occurenceMap.values.reduce { acc, i -> acc + i }

        val entropyAvailable = -1 * occurenceMap.keys.fold(0.0) { acc, world ->

            val probability = occurenceMap.get(world)!!.toDouble() / allWorldOccurences.toDouble()

            acc + (probability * Math.log(probability))
        }

        // println("ENTROPY: " + entropyAvailable + " unique worlds: " + occurenceMap.size + " allWorldOccurences: " + allWorldOccurences)

        return entropyAvailable
    }


    private fun getSetOfNextPossibleWorlds(world: World, sampleSizeMax: Int = Integer.MAX_VALUE): List<World> {

        val ant = world.ant
        val lastIndex = world.grid.lastIndex

        val possibleWorlds = mutableListOf<World>()

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




}
