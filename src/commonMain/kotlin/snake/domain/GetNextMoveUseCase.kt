package snake.domain

import snake.model.*

class GetNextMoveUseCase {

    val getPossibleNextWorldsUseCase = GetPossibleNextWorldsUseCase()

    fun execute(input: WorldOld): WorldOld {

        val worldStates = getPossibleNextWorldsUseCase.execute(input)

        var max: Int = 0
        val bestWorld = worldStates.maxBy {

            val newMax = GetComplexityForMoveUseCase().execute(it, 10)

            if(newMax > max)
                max = newMax
            newMax
        }

        println("Best world found!: " + max)


        return bestWorld

    }



}
