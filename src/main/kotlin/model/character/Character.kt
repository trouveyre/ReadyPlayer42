package model.character

import org.dyn4j.dynamics.Body
import org.dyn4j.geometry.Geometry
import org.dyn4j.geometry.MassType
import org.dyn4j.geometry.Vector2


class Character : Body(), CharacterData {

    override var x: Double
        get() = worldCenter.x
        set(value) {
            translate(value - worldCenter.x, 0.0)
        }

    override var y: Double
        get() = worldCenter.y
        set(value) {
            translate(0.0, value - worldCenter.y)
        }

    override var action: Move = Move.None
        set(value) {
            when (value) {
                Move.Run -> when (orientation) {
                    Orientation.Right -> isRunningRight = true
                    Orientation.Left -> isRunningLeft = true
                }
                Move.Jump -> if (canJump) {
                    applyImpulse(Vector2(0.0, CharacterData.JUMP_STRENGTH_DEFAULT_VALUE))
                    canJump = false
                }
                Move.Crouch -> stopRunning()
                Move.None -> stopRunning()
            }
            field = value
        }

    override var orientation: Orientation = Orientation.Right

    private var canJump: Boolean = true
    override var isRunningRight: Boolean = false
        set(value) {
            if (value)
                isRunningLeft = false
            field = value
        }
    override var isRunningLeft: Boolean = false
        set(value) {
            if (value)
                isRunningRight = false
            field = value
        }

    override var speed: Double = CharacterData.SPEED_DEFAULT_VALUE


    init {
        addFixture(Geometry.createRectangle(2.0, 6.0))  //TODO change for a real character's body
        setMass(MassType.FIXED_ANGULAR_VELOCITY)
        isAtRestDetectionEnabled = false
    }


    fun land() {
        canJump = true
        action = if (isRunningLeft || isRunningRight) Move.Run else Move.None
    }

    private fun stopRunning() {
        isRunningRight = false
        isRunningLeft = false
    }

    override fun toString(): String {
        return "Character($x, $y) ${
            when (true) {
                isRunningRight -> "is running right"
                isRunningLeft -> "is running left"
                else -> "is not running"
            }
        }"
    }
}