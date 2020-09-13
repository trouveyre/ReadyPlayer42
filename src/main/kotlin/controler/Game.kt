package controler

import model.history.ChunkHistory
import model.character.Character
import model.history.GameHistory
import model.history.History
import model.map.Chunk
import model.map.Map
import model.map.Platform
import model.rule.*
import org.dyn4j.dynamics.BodyFixture
import org.dyn4j.dynamics.PhysicsBody
import org.dyn4j.geometry.Vector2
import org.dyn4j.world.BroadphaseCollisionData
import org.dyn4j.world.ManifoldCollisionData
import org.dyn4j.world.NarrowphaseCollisionData
import org.dyn4j.world.World
import org.dyn4j.world.listener.CollisionListener
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
class Game(
        private val map: Map,
        players: Set<Player>,
        winRule: WinRule = FirstScoreWinRule(),
        deathRule: DeathRule = ClassicDeathRule(),
        deathPenaltyRule: DeathPenaltyRule = RandomKillDeathPenaltyRule(),
        cameraRule: CameraRule = FollowingFirstPlayerCameraRule(),
        scoreRule: ScoreRule = ClassicScoreRule()
): World<PhysicsBody>() {

    val history: GameHistory = GameHistory(map, players, winRule, deathRule, deathPenaltyRule, cameraRule, scoreRule)

    val chronicle: ChunkHistory
        get() = history.chronicles.last()

    private var _cameraX: Double = chronicle.chunk.cameraStart
    val cameraX: Double
        get() {
            val oldX = _cameraX
            _cameraX = history.cameraRule(history, _cameraX)
            history.scoreRule(history, _cameraX, oldX, _scores)
            return _cameraX
        }

    val playersRunning: Set<Player>
        get() = chronicle.playersRemaining.filter { chronicle.arrivals[it] == Duration.INFINITE }.toSet()

    private val _scores: MutableMap<Player, Int> = players.associateWith { 0 }.toMutableMap()
    val scores: kotlin.collections.Map<Player, Int>
        get() = _scores


    init {
        addCollisionListener(object : CollisionListener<PhysicsBody, BodyFixture> {

            override fun collision(collision: BroadphaseCollisionData<PhysicsBody, BodyFixture>): Boolean {
                return !(collision.body1 is Character && collision.body2 is Character)
            }
            override fun collision(collision: NarrowphaseCollisionData<PhysicsBody, BodyFixture>): Boolean {
                val body1 = collision.body1
                val body2 = collision.body2
                if (body1 is Character && body2 is Platform && body1.y > body2.y)
                    body1.land()
                if (body2 is Character && body1 is Platform && body2.y > body1.y)
                    body2.land()
                return true
            }
            override fun collision(collision: ManifoldCollisionData<PhysicsBody, BodyFixture>) = true
        })
        setGravity(GRAVITY)
        addBodies()
    }


    private fun addBodies() {
        chronicle.chunk.platforms.forEach {
            addBody(it)
        }
        chronicle.playersRemaining.filterIsInstance<LocalPlayer>().forEach {   //TODO pas sur de ça pour le jeu en réseau
            addBody(it.character.apply {
                x = chronicle.chunk.startPointX
                y = chronicle.chunk.startPointY
            })
        }
    }

    fun nextChronicle(): History? {
        playersRunning.filter { it.character.x > chronicle.chunk.length }.forEach { chronicle.newArrival(it) }
        history.deathRule(history).forEach { history.deathPenaltyRule(history, it) }
        chronicle.playersDead.forEach { removeBody(it.character as PhysicsBody) }
        val winner = history.winRule(history, scores)
        return when {
            winner != null -> history.apply { end(winner, scores) }
            chronicle.playersRemaining.isEmpty() -> history.apply { end(null, scores) }
            chronicle.isEnded -> {
                val story = chronicle
                removeAllBodies()
                val value = map.nextChunk()
                history.newChronicle(ChunkHistory(value, chronicle.playersRemaining))
                _cameraX = value.cameraStart
                addBodies()
                story
            }
            else -> null
        }
    }

    override fun update(elapsedTime: Double): Boolean {
        val result = super.update(elapsedTime)
        playersRunning.forEach {
            if (it is LocalPlayer) {
                it.react(elapsedTime)
            }
        }
        return result
    }

    companion object {
        val GRAVITY: Vector2 = Vector2(0.0, -25.0)
    }
}