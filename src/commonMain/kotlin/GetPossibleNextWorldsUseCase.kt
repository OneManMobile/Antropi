import model.*

class GetPossibleNextWorldsUseCase {

    val worldTransformer = WorldTransformer()

     fun execute(world: World): List<World> {

        // move
        val nextWorld = worldTransformer.moveAnt(world)

        if(nextWorld == null)
            return emptyList()

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
        ).filterNotNull()

    }

    private fun newWorldDirection(nextWorld: World, direction: Direction?, swapOnMove: Boolean): World? {

        val nextWorldAnt = nextWorld.ant

        if(nextWorldAnt.x < 0 || nextWorldAnt.x >= nextWorld.grid.size)
            return null

        if(nextWorldAnt.y < 0 || nextWorldAnt.y >= nextWorld.grid.size)
            return null

        return nextWorld.copy(
            ant = nextWorld.ant.copy(
                nextMove = direction, swapOnMove = swapOnMove
            )
        )
    }

}
