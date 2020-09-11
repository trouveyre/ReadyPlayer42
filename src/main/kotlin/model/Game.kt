package model

import controler.Player
import model.character.MutableCharacter
import model.map.Chunk
import model.map.Map
import model.map.Platform
import org.dyn4j.dynamics.BodyFixture
import org.dyn4j.dynamics.PhysicsBody
import org.dyn4j.geometry.Vector2
import org.dyn4j.world.BroadphaseCollisionData
import org.dyn4j.world.ManifoldCollisionData
import org.dyn4j.world.NarrowphaseCollisionData
import org.dyn4j.world.World
import org.dyn4j.world.listener.CollisionListener


data class Game(
    private val map: Map,
    val players: Set<Player>
): World<PhysicsBody>() {

    private var currentChunk: Chunk = map.nextChunk()
        set(value) {
            clear()
            field = value
            addBodies()
        }

    val platforms : Set<Platform>
        get() = currentChunk.platforms


    init {
        addCollisionListener(object : CollisionListener<PhysicsBody, BodyFixture> {

            override fun collision(collision: BroadphaseCollisionData<PhysicsBody, BodyFixture>) = true
            override fun collision(collision: NarrowphaseCollisionData<PhysicsBody, BodyFixture>): Boolean {
                val body1 = collision.body1
                val body2 = collision.body2
                if (body1 is MutableCharacter)
                    body1.land()
                if (body2 is MutableCharacter)
                    body2.land()
                return true
            }
            override fun collision(collision: ManifoldCollisionData<PhysicsBody, BodyFixture>) = true
        })
        setGravity(GRAVITY)
        addBodies()
    }


    private fun addBodies() {
        platforms.forEach {
            addBody(it)
        }
        players.forEach {
            addBody(it.character)
        }
    }

    fun nextChunk() {
        currentChunk = map.nextChunk()
    }


    companion object {
        val GRAVITY: Vector2 = Vector2(0.0, -25.0)
    }
}