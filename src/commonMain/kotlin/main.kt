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

        val snakeWorldData = SnakeWorldRequest(
            size = 50,
            snakeColor = RGBA.Companion.invoke(155,155,155),
            foodColor = RGBA.Companion.invoke(155,0,0),
            backgroundColor = RGBA.Companion.invoke(255,255,255),
        )

        val snakeWorld = GenerateSnakeWorldUsecase().execute(
            snakeWorldData
        )

        var lastWorld = snakeWorld

        val size = snakeWorld.worldSize

		while (true) {

            lastWorld = GetNextSnakeWorldUseCase().execute(lastWorld)

            removeChildren()

            // FOOD
            ellipse {
                radiusX = (WIDTH / (size * 2)).toDouble()
                radiusY = (HEIGHT / (size * 2)).toDouble()

                anchor(0.5, 0.5)
                color = RGBA(snakeWorldData.foodColor)

                val xPos = (WIDTH / size) * lastWorld.food.x + ((WIDTH / size) * 0.5)
                val yPos = (HEIGHT / size) * lastWorld.food.y + ((HEIGHT / size) * 0.5)

                position(xPos, yPos)
            }

            lastWorld.snake.body.forEachIndexed { index1, it ->
                ellipse {
                    radiusX = (WIDTH / (size * 2)).toDouble()
                    radiusY = (HEIGHT / (size * 2)).toDouble()

                    anchor(0.5, 0.5)
                    color = if(index1 == 0)
                        RGBA(0,0,0)
                    else RGBA(snakeWorldData.snakeColor)

                    val xPos = (WIDTH / size) * it.x + ((WIDTH / size) * 0.5)
                    val yPos = (HEIGHT / size) * it.y + ((HEIGHT / size) * 0.5)

                    position(xPos, yPos)
                }
            }

            delay(200.milliseconds)
        }
	}

}
