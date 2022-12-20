package usecase

import ComplexityService
import GetPossibleNextWorldsUseCase
import model.*
import kotlin.random.*

class GetNextWorldUseCaseOld {

    val complexityService = ComplexityService()

    companion object{
        const val TAU = 8
        const val SAMPLES = 4
        val nextWorldsMap = mutableMapOf<World, List<World>>()
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
            val occurenceMap = mutableMapOf<World, Int>(Pair(possibleNextWorld,1))

            getAccessibleEntropy(listOf(possibleNextWorld), TAU, occurenceMap)

            val allWorldOccurences = occurenceMap.values.reduce { acc, i -> acc + i }

            val entropyAvailable = -1 * occurenceMap.keys.fold(0.0){ acc, world ->

                val probability = occurenceMap.get(world)!!.toDouble() / allWorldOccurences.toDouble()

                acc + (probability * Math.log(probability))
            }

            println("ENTROPY: " + entropyAvailable + " unique worlds: " + occurenceMap.size + " allWorldOccurences: " + allWorldOccurences)

            entropyAvailable
        }


        return bestWorld

    }

    private fun getAccessibleEntropy(currentWorlds: List<World>, tau: Int, allWorldsOnPath: MutableMap<World, Int>): Int {

        if(tau == 0){
            //val accessibleEnropy = allWorldsOnPath.fold(0){ acc: Int, world: World ->
            //    acc + complexityService.getComplexity(world.grid)
            //}

            //println("allWorldsOnPath: " + allWorldsOnPath.size)

            // What is the probability of landing in a given world, given the next step
            // And I suppose you want the probabilities to be as

            return allWorldsOnPath.size
        }

        val nextWorlds = mutableListOf<World>()

        currentWorlds.forEach { world ->

            val entry = nextWorldsMap.get(world)
            if(entry != null)
                nextWorlds.addAll(entry)
            else{
                //always do every possible move with a swap and any move without in combination
                val setOfNextPossibleWorlds = getSetOfNextPossibleWorlds(world, Random.nextInt(1, 9))
                nextWorldsMap.put(world, setOfNextPossibleWorlds)
                nextWorlds.addAll(setOfNextPossibleWorlds)
            }
        }

        nextWorlds.forEach {
            if(allWorldsOnPath.contains(it)){
                allWorldsOnPath.put(it, allWorldsOnPath.get(it)!! + 1)
            }else{
                allWorldsOnPath.put(it,1)
            }
        }

        return getAccessibleEntropy(nextWorlds, tau - 1, allWorldsOnPath)
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

    fun addAntMovement(movedAnt: Ant, world: World): List<World>{

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
