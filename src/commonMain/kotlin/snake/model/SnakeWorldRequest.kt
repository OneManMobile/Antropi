package snake.model

import com.soywiz.korim.color.*

data class SnakeWorldRequest(
    val size: Int,
    val snakeColor: RGBA,
    val foodColor: RGBA,
    val backgroundColor: RGBA,
)
