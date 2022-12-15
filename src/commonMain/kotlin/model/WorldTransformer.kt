package model

class WorldTransformer {

    fun moveAnt(world: World): World?{

        val oldAnt = world.ant

        val swapX = oldAnt.x
        val swapY = oldAnt.y

        val ant = when(oldAnt.nextMove){
            Direction.Down -> oldAnt.copy(
                y = oldAnt.y -1
            )
            Direction.Left ->  oldAnt.copy(
                x = oldAnt.x -1
            )
            Direction.Right  ->  oldAnt.copy(
                x = oldAnt.x +1
            )
            Direction.Up ->  oldAnt.copy(
                y = oldAnt.y +1
            )
            null -> oldAnt
        }


        if(ant.x < 0 || ant.x >= world.grid.size)
            return null

        if(ant.y < 0 || ant.y >= world.grid.size)
            return null



        return if(oldAnt.swapOnMove){
            val color = world.grid[swapY][swapX]
            val otherColor = world.grid[ant.y][ant.x]

            val newGrid = world.grid.toMutableList().map {
                it.toMutableList()
            }

            newGrid[ant.y][ant.x] = color
            newGrid[swapY][swapX] = otherColor

            world.copy(ant = ant,grid = newGrid)
        }else
            world.copy(ant = ant)

    }


}
