package core.game

import core.character.Character
import core.history.ChunkHistory
import core.history.GameHistory
import core.history.History
import core.map.Map
import core.map.Platform
import core.player.Player
import core.player.PlayerData
import core.rule.*
import org.dyn4j.dynamics.BodyFixture
import org.dyn4j.dynamics.PhysicsBody
import org.dyn4j.world.BroadphaseCollisionData
import org.dyn4j.world.ManifoldCollisionData
import org.dyn4j.world.NarrowphaseCollisionData
import org.dyn4j.world.World
import org.dyn4j.world.listener.CollisionListener
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
class LocalGame(
        private val map: Map,
        players: Set<PlayerData>,
        winRule: WinRule = FirstScoreWinRule(),
        deathRule: DeathRule = LeftAndBottomBordersDeathRule(),
        deathPenaltyRule: DeathPenaltyRule = WithoutDeathPenaltyRule(),
        cameraRule: CameraRule = FollowingFirstPlayerCameraRule(),
        scoreRule: ScoreRule = FirstPlayerScoreRule()
): Game {

    private val world: World<PhysicsBody> = World()

    override val history: GameHistory = GameHistory(map, players, winRule, deathRule, deathPenaltyRule, cameraRule, scoreRule)

    override val chronicle: ChunkHistory
        get() = history.chronicles.last()

    private var _cameraX: Double = chronicle.chunk.cameraStart
    override val cameraX: Double
        get() = _cameraX

    override val playersRunning: Set<PlayerData>
        get() = chronicle.playersRemaining.filter { chronicle.arrivals[it] == Duration.INFINITE }.toSet()

    private val _scores: MutableMap<PlayerData, Int> = players.associateWith { 0 }.toMutableMap()
    override val scores: kotlin.collections.Map<PlayerData, Int>
        get() = _scores


    init {
        world.addCollisionListener(object : CollisionListener<PhysicsBody, BodyFixture> {

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
        world.gravity = Game.GRAVITY
        addBodies()
    }


    private fun addBodies() {
        chronicle.chunk.platforms.forEach {
            world.addBody(it)
        }
        chronicle.playersRemaining.filterIsInstance<Player>().forEach {
            world.addBody(it.character.apply {
                x = chronicle.chunk.startPointX
                y = chronicle.chunk.startPointY
            })
        }
    }

    override fun nextChronicle(elapsedTime: Double): History? {
        world.update(elapsedTime)
        val oldX = _cameraX
        _cameraX = history.cameraRule(history, _cameraX)
        history.scoreRule(this, oldX, _scores)
        playersRunning.apply {
            filterIsInstance<Player>().forEach { it.react(elapsedTime) }
            filter { it.character.x > chronicle.chunk.length }.forEach { chronicle.newArrival(it) }
        }
        history.deathRule(this).forEach { history.deathPenaltyRule(this, it) }
        chronicle.playersDead.forEach { world.removeBody(it.character as PhysicsBody) }
        val winner = history.winRule(this)
        return when {
            winner != null -> history.apply {
                world.removeAllBodies()
                end(winner, scores)
            }
            chronicle.playersRemaining.isEmpty() -> history.apply {
                world.removeAllBodies()
                end(null, scores)
            }
            chronicle.isEnded -> {
                world.removeAllBodies()
                val story = chronicle
                val value = map.nextChunk()
                history.newChronicle(ChunkHistory(value, chronicle.playersRemaining))
                _cameraX = value.cameraStart
                addBodies()
                story
            }
            else -> null
        }
    }

    override fun cleanUp() {
        world.removeAllBodies()
    }
}