package blocks.model

class WorldExplorer {

    fun getPossibleNextStates(world: Set<Block>): Set<Set<Block>> {
        val possibleStates = mutableSetOf<Set<Block>>()

        world.filterIsInstance<Block.Ant>().forEach { ant ->
            val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1) // Up, Down, Left, Right movements
            directions.forEach { dir ->
                val newX = ant.x + dir.first
                val newY = ant.y + dir.second

                // Check if the new position is within bounds and not occupied
                if (isValidPosition(newX, newY, world)) {
                    val movedAnt = Block.Ant(newX, newY)
                    val newState = world.toMutableSet()
                    newState.remove(ant)
                    newState.add(movedAnt)
                    possibleStates.add(newState)
                }

                // Check for a pebble to push
                val pebbleToPush: Block? = world.find { it is Block.Pebble && it.x == newX && it.y == newY }
                if (pebbleToPush != null) {
                    val newPebbleX = pebbleToPush.x + dir.first
                    val newPebbleY = pebbleToPush.y + dir.second
                    // Check if the new pebble position is within bounds and not occupied
                    if (isValidPosition(newPebbleX, newPebbleY, world)) {
                        val movedAnt = Block.Ant(newX, newY)
                        val movedPebble = Block.Pebble(newPebbleX, newPebbleY)
                        val newState = world.toMutableSet()
                        newState.remove(ant)
                        newState.remove(pebbleToPush)
                        newState.add(movedAnt)
                        newState.add(movedPebble)
                        possibleStates.add(newState)
                    }
                }
            }
        }

        return possibleStates
    }

    private fun isValidPosition(x: Int, y: Int, world: Set<Block>): Boolean {
        if (x !in 0 until WorldConfig.WORLD_SIZE || y !in 0 until WorldConfig.WORLD_SIZE) return false
        return world.none { it.x == x && it.y == y }
    }

}
