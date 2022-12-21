package model

data class WorldStep(
    val currentWorld: World,
    val biases: Map<World, Double>
)
