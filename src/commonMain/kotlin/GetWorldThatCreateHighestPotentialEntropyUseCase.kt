
import model.*

class GetWorldThatCreateHighestPotentialEntropyUseCase {

    val complexityService = ComplexityService()
    val getPossibleNextWorldsUseCase = GetPossibleNextWorldsUseCase()

    companion object{
        const val REACH = 1
    }

    /**
     * 1. Get possible next worlds
     * 2. For each of the next worlds, choose the step which maximises future freedom of action
     * 3. In other words, choose the next world which provides the maximum amount of possible entropy generation
     * 4. In other words, choose the next world which allows maximum future entropy generation
     * 5. Define Maximum future entropy generation as the
     */
    fun execute(input: World): World{

        val nextMoves = getPossibleNextWorldsUseCase.execute(input)

        val nextWorld = nextMoves.maxBy {
            val complexity = endComplexityFromPossibleMovesIn(listOf(it), REACH, emptyList())

            println("Complexity found: " + complexity)

            complexity
        }

        return nextWorld
    }


    /**
     * 1. Perform moves out to the possible reach
     * 2. Calculate end complexity for each/all moves
     * 3. The first moves score is the sum of complexity of all end-moves
     */
    private fun endComplexityFromPossibleMovesIn(nextWorlds: List<World>, reach: Int, visitedWorlds: List<World>): Int {

        if(reach == 0){

            val entropyAvg = nextWorlds.fold(0) { acc: Int, world: World ->
                acc + complexityService.getComplexity(world.grid)
            }.run {
                this / nextWorlds.size
            }

            return entropyAvg
        }

        return nextWorlds.fold(0){ acc: Int, world: World ->
            val nextMoves = getPossibleNextWorldsUseCase.execute(world)
            acc + endComplexityFromPossibleMovesIn(nextMoves, reach -1, listOf(world) + visitedWorlds)
        }.run {
            this / nextWorlds.size
        }

    }


}
