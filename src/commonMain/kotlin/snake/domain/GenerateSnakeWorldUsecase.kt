package snake.domain

import model.*
import snake.model.*
import kotlin.random.*

class GenerateSnakeWorldUsecase {

    fun execute(input: SnakeWorldRequest): SnakeWorld {

        val snakeHead = BodyPoint(x = Random.nextInt(input.size), y = Random.nextInt(input.size))

        var index = 0
        var foodPoint = BodyPoint(x = Random.nextInt(input.size), y = Random.nextInt(input.size))
        while (index < Int.MAX_VALUE){
            if(foodPoint.x != snakeHead.x && foodPoint.y != snakeHead.y)
                break
            foodPoint = BodyPoint(x = Random.nextInt(input.size), y = Random.nextInt(input.size))
            index++
        }

        return SnakeWorld(
            snake = Snake(body = listOf(BodyPoint(x = Random.nextInt(input.size), y = Random.nextInt(input.size))), 100),
            food = foodPoint,
            worldSize = input.size
        )
    }

}
