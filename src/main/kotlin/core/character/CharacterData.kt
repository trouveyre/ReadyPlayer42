package core.character

import org.dyn4j.geometry.Vector2


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
        const val SPEED_DEFAULT_VALUE: Double = 30.0
        const val SPEED_SUPER_VALUE: Double = 50.0
        const val SPEED_REDUCTION_VALUE: Double = 9.0
        val JUMP_DEFAULT_FORCE: Vector2 = Vector2(0.0, 500.0)
        val JUMP_FALL_REDUCTION_FORCE: Vector2 = Vector2(0.0, 5.0)
        const val DENSITY: Double = 1.0
        const val FRICTION: Double = 0.0
        const val RESTITUTION: Double = 0.0
    }
}