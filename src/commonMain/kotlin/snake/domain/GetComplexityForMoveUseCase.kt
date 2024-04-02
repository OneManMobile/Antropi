package snake.domain
import snake.model.*

class GetComplexityForMoveUseCase {

    val complexityService = ComplexityService()
    val getPossibleNextWorldsUseCase = GetPossibleNextWorldsUseCase()

    var calculationsMade = 0

    fun execute(input: WorldOld, reach: Int): Int{

        if(reach == 0){
            val complexityFoundForMove = complexityService.getComplexity(input.grid)
            return complexityFoundForMove
        }

        // simulate Ant movement
        val worldStates = getPossibleNextWorldsUseCase.execute(input)

        if(worldStates.isEmpty())
            return Int.MIN_VALUE // we are looking for the max so this will be discarded

        return worldStates.map { world ->
            calculationsMade++
            execute(world, reach - 1)
        }.max()
    }


}
