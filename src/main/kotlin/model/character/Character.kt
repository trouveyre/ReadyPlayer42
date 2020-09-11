package model.character

import org.dyn4j.dynamics.PhysicsBody


interface Character : PhysicsBody {

    val x: Double
    val y: Double
    val action: Move
    val orientation: Orientation

    val isRunningRight: Boolean
    val isRunningLeft: Boolean
    val speed: Double


    companion object {
        const val SPEED_DEFAULT_VALUE: Double = 15.0
        const val JUMP_STRENGTH_DEFAULT_VALUE: Double = 300.0
    }
}