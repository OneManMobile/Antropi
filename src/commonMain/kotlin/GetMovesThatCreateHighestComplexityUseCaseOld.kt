
import model.*

class GetMovesThatCreateHighestComplexityUseCaseOld {

    val complexityService = ComplexityService()
    val getPossibleNextWorldsUseCase = GetPossibleNextWorldsUseCase()

    companion object{
        const val REACH = 5
    }

    data class Moves(
        val worlds: List<WorldOld>,
        val endComplexity: Int
    )

    fun execute(input: WorldOld): Moves{

        val getMoves = getMoves(input, REACH)

        return getMoves.maxBy {
            it.endComplexity
        }.also {
            println("PossibleMoves: " + getMoves.size)
        }
    }

    private fun getMoves(world: WorldOld, reach: Int): List<Moves> {

        var list = listOf<List<WorldOld>>(listOf(world))

        for(i in 0..reach){

            val newList = mutableListOf<List<WorldOld>>()

            list.forEach { currentMoves ->

                val nextMoves = getPossibleNextWorldsUseCase.execute(currentMoves.last())

                nextMoves.forEach { nextMove ->
                    newList.add(currentMoves + nextMove)
                }

            }

            list = newList
        }

        return list.map {
            Moves(it, complexityService.getComplexity(it.last().grid))
        }
    }


}
