package blocks

import HEIGHT
import WIDTH
import com.soywiz.klock.milliseconds
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.anchor
import com.soywiz.korge.view.ellipse
import com.soywiz.korge.view.position
import com.soywiz.korim.color.RGBA
import snake.domain.GenerateSnakeWorldUsecase
import snake.model.SnakeWorldRequest
import snake.usecase.GetNextSnakeWorldUseCase

class BlockScene : Scene() {

	override suspend fun SContainer.sceneMain() {

        val snakeWorldData = SnakeWorldRequest(
            size = 40,
            snakeColor = RGBA.invoke(155, 155, 155),
            foodColor = RGBA.invoke(155, 0, 0),
            backgroundColor = RGBA.invoke(255, 255, 255),
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
                        RGBA(0, 0, 0)
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
