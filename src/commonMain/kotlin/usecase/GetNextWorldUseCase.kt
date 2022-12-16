package usecase

import ComplexityService
import GetPossibleNextWorldsUseCase
import model.*

class GetNextWorldUseCase {

    val complexityService = ComplexityService()

    companion object{
        const val TAU = 5
        val nextWorldsMap = mutableMapOf<World, Set<World>>()
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

        var worldSets = setOf<World>()
        
        worldSets = getSetOfNextPossibleWorlds(input)

        val bestWorld = worldSets.maxBy { possibleNextWorld ->
            val entropy = getAccessibleEntropy(setOf(possibleNextWorld), TAU, setOf(possibleNextWorld))

            println("ENTROPY: " + entropy)

            entropy
        }
        
        
        return bestWorld

    }

    private fun getAccessibleEntropy(currentWorlds: Set<World>, tau: Int, allWorldsOnPath: Set<World>): Int {

        if(tau == 0){
            //val accessibleEnropy = allWorldsOnPath.fold(0){ acc: Int, world: World ->
            //    acc + complexityService.getComplexity(world.grid)
            //}

            //println("allWorldsOnPath: " + allWorldsOnPath.size)

            return allWorldsOnPath.size
        }

        val nextWorlds = mutableSetOf<World>()

        currentWorlds.forEach { world ->

            val entry = nextWorldsMap.get(world)

            if(entry != null)
                nextWorlds.addAll(entry)
            else{
                //always do every possible move with a swap and any move without in combination
                val setOfNextPossibleWorlds = getSetOfNextPossibleWorlds(world)
                nextWorldsMap.put(world, setOfNextPossibleWorlds)
                nextWorlds.addAll(setOfNextPossibleWorlds)
            }
        }

        return getAccessibleEntropy(nextWorlds, tau - 1, allWorldsOnPath + nextWorlds)
    }

    private fun getSetOfNextPossibleWorlds(world: World): Set<World> {

        val ant = world.ant
        val grid = world.grid
        val lastIndex = world.grid.lastIndex

        val possibleWorlds = mutableSetOf<World>()

        if(ant.x != 0){
            possibleWorlds.addAll(addAntMovement(ant.copy(ant.x - 1), world))
        }
        if(ant.x != lastIndex){
            possibleWorlds.addAll(addAntMovement(ant.copy(ant.x + 1), world))
        }
        if(ant.y != 0){
            possibleWorlds.addAll(addAntMovement(ant.copy(ant.y - 1), world))
        }
        if(ant.y != lastIndex){
            possibleWorlds.addAll(addAntMovement(ant.copy(ant.y + 1), world))
        }

        return possibleWorlds
    }

    fun addAntMovement(movedAnt: Ant, world: World): Set<World>{

        val ant = world.ant

        val swappedGrid = world.grid.toMutableList().map { it.toMutableList() }

        val tempColor1 = swappedGrid[ant.x][ant.y]
        val tempColor2 = swappedGrid[movedAnt.x][movedAnt.y]
        swappedGrid[movedAnt.x][movedAnt.y] = tempColor1
        swappedGrid[ant.x][ant.y] = tempColor2

        return setOf<World>(
            world.copy(ant = movedAnt),
            world.copy(ant = movedAnt, grid = swappedGrid),
            )
    }




}
