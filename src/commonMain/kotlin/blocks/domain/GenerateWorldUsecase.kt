package blocks.domain

import blocks.model.*
import kotlin.random.*

class GenerateWorldUsecase {

    fun execute(pebbles: Int): Set<Block> {

        val xs = mutableSetOf<Int>()
        val ys = mutableSetOf<Int>()

        val world = mutableSetOf<Block>()

        val ant = Block.Ant(Random.nextInt(WorldConfig.WORLD_SIZE), Random.nextInt(WorldConfig.WORLD_SIZE),)
        world.add(ant)
        xs.add(ant.x)
        ys.add(ant.y)

        // Generate pebbles ensuring no two blocks (pebble or ant) share the same position
        var pebblesAdded = 0
        while (pebblesAdded < pebbles) {
            val x = Random.nextInt(WorldConfig.WORLD_SIZE)
            val y = Random.nextInt(WorldConfig.WORLD_SIZE)

            // Check if the position is already occupied
            if (!(x in xs && y in ys)) {
                val pebble = Block.Pebble(x, y)
                world.add(pebble)
                xs.add(x)
                ys.add(y)
                pebblesAdded++
            }
        }

        return world
    }

}
