package model.map


data class Chunk(
    val length: Float,
    val platforms: Set<Platform>,
    val movementFacility: Float = 1f
)