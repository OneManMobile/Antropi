
import com.soywiz.korma.random.*
import model.*
import kotlin.random.*

class GetWorldThatCreateHighestPotentialEntropyUseCase {

    val getPossibleNextWorldsUseCase = GetPossibleNextWorldsUseCase()

    companion object{
        const val REACH = 9
    }

    /**
     * 1. Get possible next worlds
     * 2. For each of the next worlds, choose the step which maximises future freedom of action
     * 3. In other words, choose the next world which provides the maximum amount of possible entropy generation
     * 4. In other words, choose the next world which allows maximum future entropy generation
     * 5. Define Maximum future entropy generation as the maximum average entropy across all end-paths within reach from the next step
     */
    fun execute(input: World): World{

        val nextMoves = getPossibleNextWorldsUseCase.execute(input)

        var bestNextWorldComplexity = Integer.MIN_VALUE
        var bestNextWorld: World? = null
        var lastBestNextWorld: World? = null

        nextMoves.forEach { world ->
            val endWorlds = endComplexityFromPossibleMovesIn(setOf(world), REACH)

            val endMoves = if(endWorlds.isEmpty()) {
                println("No Complexity found: " + world.toString())
                Integer.MIN_VALUE
            }else {
                println("endWorlds: " + endWorlds.size)
                endWorlds.size
            }

            if(endMoves > bestNextWorldComplexity){
                bestNextWorldComplexity = endMoves
                lastBestNextWorld = bestNextWorld
                bestNextWorld = world
            }
        }

        return if (lastBestNextWorld != null){
            if(Random.nextDouble() < 0.25 )
                lastBestNextWorld!!
            else bestNextWorld!!
        }else bestNextWorld!!
    }

    /**
     * 1. Perform moves out to the possible reach
     * 2. Calculate end complexity for each/all moves
     * 3. The first moves score is the sum of complexity of all end-moves
     *
     * ----
     *
     *  /**
     * 1. Get possible next worlds
     * 2. For each of the next worlds, choose the step which maximises future freedom of action
     * 3. In other words, choose the next world which provides the maximum amount of possible entropy generation
     * 4. In other words, choose the next world which allows maximum future entropy generation
     * 5. Define Maximum future entropy generation as the maximum average entropy across all end-paths within reach from the next step
        */
     */
    private fun endComplexityFromPossibleMovesIn(nextWorlds: Set<World>, reach: Int): Set<World> {

        if(reach == 0){
            return nextWorlds.toSet()
        }

        val nextSet = mutableSetOf<World>()

        nextWorlds.forEach {
            val nextMoves = getPossibleNextWorldsUseCase.execute(it, reach)
            nextSet.addAll(nextMoves)
        }

        return endComplexityFromPossibleMovesIn(nextSet, reach - 1)

    }


}
