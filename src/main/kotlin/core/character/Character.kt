package core.character

import org.dyn4j.collision.CollisionBody
import org.dyn4j.collision.narrowphase.NarrowphaseDetector
import org.dyn4j.dynamics.Body
import org.dyn4j.geometry.Geometry
import org.dyn4j.geometry.MassType
import org.dyn4j.geometry.Vector2
import org.dyn4j.world.CollisionData
import org.dyn4j.world.CollisionWorld
import org.dyn4j.world.World


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

    private var _action: Move = Move.None
    override val action: Move
        get() = _action

    override var orientation: Orientation = Orientation.Right

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

    private var wantToJump: Boolean = false
        set(value) {
            if (value) {
                applyImpulse(CharacterData.JUMP_DEFAULT_FORCE)
            }
            field = value
        }


    init {
        addFixture(
            Geometry.createRectangle(CharacterData.WIDTH, CharacterData.HEIGHT),
            CharacterData.DENSITY,
            CharacterData.FRICTION,
            CharacterData.RESTITUTION
        )
        setMass(MassType.FIXED_ANGULAR_VELOCITY)
        isAtRestDetectionEnabled = false
    }


    fun nextPosition(elapsedTime: Double) {
        linearVelocity.x = when {
            isRunningRight -> speed
            isRunningLeft -> -speed
            else -> 0.0
        }
        if (action == Move.Jump) {
            speed -= CharacterData.SPEED_REDUCTION_VALUE * elapsedTime
            if (wantToJump)
                applyForce(CharacterData.JUMP_FALL_REDUCTION_FORCE)
        }
    }

    fun newAction(move: Move, stopDoing: Boolean = false) {
        when (move) {
            Move.Run -> {
                when (orientation) {
                    Orientation.Right -> isRunningRight = !stopDoing
                    Orientation.Left -> isRunningLeft = !stopDoing
                }
            }
            Move.Jump -> {
                if (stopDoing) {
                    wantToJump = false
                }
                else if (action != Move.Jump) {
                    wantToJump = true
                }
            }
            Move.Crouch -> {}
            Move.None -> stopRunning()
        }
        if (action != Move.Jump)
            if (stopDoing && action == move && action != Move.None)
                _action = Move.None
            else if (!stopDoing)
                _action = move
    }

    private fun stopRunning() {
        isRunningRight = false
        isRunningLeft = false
    }

    fun land() {
        if (action == Move.Jump) {
            _action = if (isRunningLeft || isRunningRight) Move.Run else Move.None
            speed = CharacterData.SPEED_DEFAULT_VALUE
        }
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