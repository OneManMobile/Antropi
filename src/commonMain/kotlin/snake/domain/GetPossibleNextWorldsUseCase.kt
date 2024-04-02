package snake.domain

import snake.model.*

class GetPossibleNextWorldsUseCase {

    val worldTransformer = WorldTransformer()

     fun execute(world: WorldOld, take: Int = 9): Set<WorldOld> {

        // move
        val nextWorld = worldTransformer.moveAnt(world)

        if(nextWorld == null) {
            return emptySet()
        }

        return listOf(
            newWorldDirection(nextWorld, Direction.Up, false),
            newWorldDirection(nextWorld, Direction.Down, false),
            newWorldDirection(nextWorld, Direction.Left, false),
            newWorldDirection(nextWorld, Direction.Right, false),
            newWorldDirection(nextWorld, null, false),
            newWorldDirection(nextWorld, Direction.Up, true),
            newWorldDirection(nextWorld, Direction.Down, true),
            newWorldDirection(nextWorld, Direction.Left, true),
            newWorldDirection(nextWorld, Direction.Right, true),
        ).filterNotNull().shuffled().take(take).toSet()
    }

    private fun newWorldDirection(nextWorld: WorldOld, direction: Direction?, swapOnMove: Boolean): WorldOld? {

        val nextWorldAnt = nextWorld.ant

        if(nextWorldAnt.x < 0 || nextWorldAnt.x >= nextWorld.grid.size)
            return null

        if(nextWorldAnt.y < 0 || nextWorldAnt.y >= nextWorld.grid.size)
            return null

        if(nextWorldAnt.x == 0 && nextWorldAnt.nextMove == Direction.Left)
            return null

        if(nextWorldAnt.x == nextWorld.grid.lastIndex && nextWorldAnt.nextMove == Direction.Right)
            return null

        if(nextWorldAnt.y == 0 && nextWorldAnt.nextMove == Direction.Up)
            return null

        if(nextWorldAnt.y == nextWorld.grid.lastIndex && nextWorldAnt.nextMove == Direction.Down)
            return null

        return nextWorld.copy(
            ant = nextWorld.ant.copy(
                nextMove = direction, swapOnMove = swapOnMove
            )
        )
    }

}
