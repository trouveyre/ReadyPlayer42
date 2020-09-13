package model.character


interface CharacterData {

    val x: Double
    val y: Double
    val action: Move
    val orientation: Orientation

    val isRunningRight: Boolean
    val isRunningLeft: Boolean
    val speed: Double


    companion object {
        const val SPEED_DEFAULT_VALUE: Double = 15.0
        const val JUMP_STRENGTH_DEFAULT_VALUE: Double = 320.0
    }
}