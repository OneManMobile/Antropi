import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.*
import com.soywiz.korim.color.*
import model.*
import usecase.*
import java.awt.*
import kotlin.random.*

const val WIDTH = 512
const val HEIGHT = 512


suspend fun main() = Korge(width = WIDTH, height = HEIGHT, bgcolor = Colors["#FFFFFF"], title = "Antropi") {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo({ MyScene() })
}

class MyScene : Scene() {

	override suspend fun SContainer.sceneMain() {

        var world = GenerateWorldUsecase().execute(WorldRequest(
            11,
            listOf(Color.BLUE, Color.RED, Color.GREEN),
            backgroundColor = Color.WHITE,
            backgroundProminence = 0.7f
        ))

        var lastWorld = world

        val size = world.grid.size
        val ant = world.ant

        val ellipseGrid = Array( world.grid.size) {
            Array<Ellipse?>( world.grid.size) {
               null
          }
        }

        for (iblex in 0 until size){
            for (j in 0 until size){

                val ellipse: Ellipse = ellipse {
                    radiusX = (WIDTH / (size * 2)).toDouble()
                    radiusY = (HEIGHT / (size * 2)).toDouble()

                    anchor(0.5, 0.5)
                    color = RGBA(world.grid[iblex][j])

                    val xPos = (WIDTH / size) * iblex + ((WIDTH / size) * 0.5)
                    val yPos = (HEIGHT / size) * j + ((HEIGHT / size) * 0.5)

                    position(xPos, yPos)
                }

                ellipseGrid[iblex][j] = ellipse
            }
        }

        val antEllipse = ellipse {
            radiusX = (WIDTH / (size * 3)).toDouble()
            radiusY = (HEIGHT / (size * 3)).toDouble()

            anchor(0.5, 0.5)
            color = RGBA(RGBA(0, 0, 0, 220))

            val xPos = (WIDTH / size) * ant.x + ((WIDTH / size) * 0.5)
            val yPos = (HEIGHT / size) * ant.y + ((HEIGHT / size) * 0.5)

            position(xPos, yPos)
        }


		while (true) {
            val referenceWorld = lastWorld.copy()

            lastWorld = GetNextWorldUseCase().execute(lastWorld)

            val xPos = (WIDTH / size) * lastWorld.ant.x + ((WIDTH / size) * 0.5)
            val yPos = (HEIGHT / size) * lastWorld.ant.y + ((HEIGHT / size) * 0.5)
            antEllipse.zIndex = 1.0
            antEllipse.tweenNoWait(antEllipse::x[xPos], antEllipse::y[yPos], time = 300.milliseconds)

            val movedColor = findSwappedPositions(referenceWorld.grid, lastWorld.grid)

            if(movedColor != null){

                val movedFrom = movedColor.first
                val movedTo = movedColor.second

                println(movedFrom.toString() + " to " + movedTo.toString())

                val ellipse = ellipseGrid[movedFrom.first][movedFrom.second]!!
                val xPos = (WIDTH / size) * movedTo.first + ((WIDTH / size) * 0.5)
                val yPos = (HEIGHT / size) * movedTo.second + ((HEIGHT / size) * 0.5)
                ellipse.zIndex = 0.7
                ellipse.tweenNoWait(ellipse::x[xPos], ellipse::y[yPos], time = 300.milliseconds)


                val swapWithEllipse = ellipseGrid[movedTo.first][movedTo.second]!!
                val xPosSwapped = (WIDTH / size) * movedFrom.first + ((WIDTH / size) * 0.5)
                val yPosSwapped = (HEIGHT / size) * movedFrom.second + ((HEIGHT / size) * 0.5)
                swapWithEllipse.zIndex = 0.5
                swapWithEllipse.tweenNoWait(swapWithEllipse::x[xPosSwapped],swapWithEllipse::y[yPosSwapped], time = 300.milliseconds )
                ellipseGrid[movedFrom.first][movedFrom.second] = swapWithEllipse
                ellipseGrid[movedTo.first][movedTo.second] = ellipse
            }
            delay(300.milliseconds)
        }
	}

    fun findSwappedPositions(grid1: List<List<Int>>, grid2: List<List<Int>>): Pair<Pair<Int, Int>, Pair<Int, Int>>? {
        val rows = grid1.size
        val cols = grid1[0].size

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (grid1[i][j] != grid2[i][j]) {
                    // Check if any adjacent integers in the first grid have been swapped with each other in the second grid
                    if (j < cols - 1 && grid1[i][j + 1] == grid2[i][j] && grid1[i][j] == grid2[i][j + 1]) {
                        return Pair(Pair(i, j), Pair(i, j + 1))
                    }
                    if (i < rows - 1 && grid1[i + 1][j] == grid2[i][j] && grid1[i][j] == grid2[i + 1][j]) {
                        return Pair(Pair(i, j), Pair(i + 1, j))
                    }

                    var swappedX = 0
                    var swappedY = 0
                    for (k in 0 until rows) {
                        for (l in 0 until cols) {
                            if (grid1[i][j] == grid2[k][l]) {
                                swappedX = k
                                swappedY = l
                            }
                        }
                    }
                    return Pair(Pair(i, j), Pair(swappedX, swappedY))
                }
            }
        }

        return null
    }

}
