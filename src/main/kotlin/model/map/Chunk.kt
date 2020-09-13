package model.map


data class Chunk(
    val length: Double,
    val platforms: Set<Platform>,
    val startPointX: Double,
    val startPointY: Double,
    val cameraStart: Double
)