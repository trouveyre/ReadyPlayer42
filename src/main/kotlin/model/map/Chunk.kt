package model.map


data class Chunk(
    val length: Double,
    val platforms: Set<Platform>,
    val movementFacility: Double = 1.0
)