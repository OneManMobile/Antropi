package snake.model

import java.awt.Color

data class WorldRequest(
    val size: Int,
    val colors: List<Color>,
    val backgroundColor: Color,
    val backgroundProminence: Float = 0.5f
)
