import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import model.*
import usecase.*
import java.awt.*

const val WIDTH = 512
const val HEIGHT = 512


suspend fun main() = Korge(width = WIDTH, height = HEIGHT, bgcolor = Colors["#FFFFFF"], title = "Antropi") {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo({ MyScene() })
}

class MyScene : Scene() {

	override suspend fun SContainer.sceneMain() {

        var world = GenerateWorldUsecase().execute(WorldRequest(
            25,
            listOf(Color.RED, Color.BLUE, Color.GREEN),
            backgroundColor = Color.WHITE,
            backgroundProminence = 0.5f
        ))

        var lastWorld = world
		while (true) {
            drawWorld(lastWorld)
            lastWorld = GetNextWorldUseCase().execute(lastWorld)
            delay(250.milliseconds)
        }
	}

    private fun SContainer.drawWorld(world: World) {
        val size = world.grid.size
        val ant = world.ant

        for (i in 0 until size)
            for (j in 0 until size){

                val ellipse = ellipse {
                    radiusX = (WIDTH / (size * 2)).toDouble()
                    radiusY = (HEIGHT / (size * 2)).toDouble()

                    anchor(0.5, 0.5)
                    color = RGBA(world.grid[i][j])

                    val xPos = (WIDTH / size) * i + ((WIDTH / size) * 0.5)
                    val yPos = (HEIGHT / size) * j + ((HEIGHT / size) * 0.5)

                    position(xPos, yPos)
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
    }
}
