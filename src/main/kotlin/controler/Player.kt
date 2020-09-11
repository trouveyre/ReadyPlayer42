package controler

import model.character.Move
import model.character.Orientation


interface Player {

    val characterX: Float
    val characterY: Float
    val characterAction: Move
    val characterOrientation: Orientation
    val characterSpeed: Float
    val characterIsRunningRight: Boolean
    val characterIsRunningLeft: Boolean

    fun react(
            secondPerFrame: Float,
            isOnFloor: Boolean,
            forceX: Float = 0f,
            forceY: Float = 0f
    )
}