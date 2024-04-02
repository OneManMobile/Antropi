package blocks.domain

import blocks.model.*
import kotlin.random.*

class GenerateWorldUsecase {

    fun execute(worldSize: Int, pebbles: Int): Set<Block> {

        val xs = mutableSetOf<Int>()
        val ys = mutableSetOf<Int>()

        val world = mutableSetOf<Block>()

        val ant = Block.Ant(Random.nextInt(worldSize), Random.nextInt(worldSize),)
        world.add(ant)
        xs.add(ant.x)
        ys.add(ant.y)

        // Generate pebbles ensuring no two blocks (pebble or ant) share the same position
        var pebblesAdded = 0
        while (pebblesAdded < pebbles) {
            val x = Random.nextInt(worldSize)
            val y = Random.nextInt(worldSize)

            // Check if the position is already occupied
            if (!(x in xs && y in ys)) {
                val pebble = Block.Pebble(x, y) // Assuming a Pebble class exists
                world.add(pebble)
                xs.add(x)
                ys.add(y)
                pebblesAdded++
            }
        }

        return world
    }

}
