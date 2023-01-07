package model

data class SnakeWorld(
    val snake: Snake,
    val food: BodyPoint,
    val worldSize: Int
)
