package blocks.domain

import blocks.model.*
import blocks.model.WorldExplorer

class GetPossibleNextWorldsUseCase {

    val worldTransformer = WorldExplorer()

     fun execute(oldWorld: Set<Block>): Set<Set<Block>> {

        val possibleWorlds = worldTransformer.getPossibleNextStates(oldWorld)

         return possibleWorlds
    }

}
