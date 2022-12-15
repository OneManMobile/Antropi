import model.*
import kotlin.random.*

class GenerateWorldUsecase {

    fun execute(input: WorldRequest): World{

        val grid = Array(input.size) {
            IntArray(input.size) {
                if (Random.nextDouble() > input.backgroundProminence) {
                    input.colors.get(Random.nextInt(input.colors.size)).rgb
                } else
                    input.backgroundColor.rgb
            }.toList()
        }.toList()

        return World(
            grid,
            Ant(
                grid.size / 2,
                grid.size / 2,
            )
        )
    }

}
