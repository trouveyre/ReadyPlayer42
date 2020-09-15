package core.character


interface CharacterData {

    val x: Double
    val y: Double
    val action: Move
    val orientation: Orientation

    val isRunningRight: Boolean
    val isRunningLeft: Boolean
    val speed: Double

    companion object {
        const val WIDTH: Double = 2.0
        const val HEIGHT: Double = 6.0
        const val SPEED_DEFAULT_VALUE: Double = 25.0
        const val SPEED_REDUCTION_VALUE: Double = 7.0
        const val JUMP_STRENGTH_DEFAULT_VALUE: Double = 5000.0
        const val DENSITY: Double = 10.0
    }
}