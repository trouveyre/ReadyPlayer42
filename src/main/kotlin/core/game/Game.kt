package core.game

import core.player.PlayerData
import core.history.ChunkHistory
import core.history.GameHistory
import core.history.History
import org.dyn4j.geometry.Vector2
import org.dyn4j.world.PhysicsWorld

interface Game {

    val history: GameHistory
    val chronicle: ChunkHistory
    val cameraX: Double
    val playersRunning: Set<PlayerData>
    val scores: Map<PlayerData, Int>

    fun nextChronicle(elapsedTime: Double): History?
    fun cleanUp()


    companion object {
        val GRAVITY: Vector2 = Vector2(0.0, -100.0)
    }
}