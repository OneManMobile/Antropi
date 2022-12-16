package model

data class WorldStep(
    val grid: List<List<Int>>,
    val chaosAnts: List<Ant>,
    val entropy: Int
)
