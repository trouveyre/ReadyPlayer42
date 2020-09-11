package controler

import model.character.Character
import model.character.Move
import model.character.Orientation


class LocalPlayer(private val character: Character) : Player {

    override val characterX: Float
        get() = character.x

    override val characterY: Float
        get() = character.y

    override val characterAction: Move
        get() = character.action

    override val characterOrientation: Orientation
        get() = character.orientation

    override val characterSpeed: Float
        get() = character.speed

    override val characterIsRunningRight: Boolean
        get() = character.isRunningRight

    override val characterIsRunningLeft: Boolean
        get() = character.isRunningLeft


    override fun react(secondPerFrame: Float, isOnFloor: Boolean, forceX: Float, forceY: Float) {
        character.speed = Character.SPEED_DEFAULT_VALUE * secondPerFrame
        if (!isOnFloor)
            character.action = Move.Jump
        else if (character.action == Move.Jump)
            character.action = Move.None
    }

    fun makeCharacter(move: Move, orientation: Orientation = character.orientation) {
        character.orientation = orientation
        character.action = move
        when (move) {
            Move.Run -> {
                if (orientation == Orientation.Right)
                    character.isRunningRight = true
                else
                    character.isRunningLeft = true
            }
            Move.Jump -> {

            }
            Move.Crouch -> character.stopRunning()
            Move.None -> character.stopRunning()
        }
    }
}