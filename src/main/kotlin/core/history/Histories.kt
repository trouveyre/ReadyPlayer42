package core.history

import core.player.PlayerData
import core.rule.ScoreRule
import core.map.Chunk
import core.rule.CameraRule
import core.rule.DeathPenaltyRule
import core.rule.DeathRule
import core.rule.WinRule
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource


@OptIn(ExperimentalTime::class)
sealed class History {

    protected var startTime: TimeMark? = null
    val hasStarted: Boolean
        get() = startTime != null

    abstract val isEnded: Boolean


    fun start() {
        if (!hasStarted)
            startTime = TimeSource.Monotonic.markNow()
        onStart()
    }

    open fun onStart() {}
}


@OptIn(ExperimentalTime::class)
class GameHistory(
        map: core.map.Map,
        val players: Set<PlayerData>,
        val winRule: WinRule,
        val deathRule: DeathRule,
        val deathPenaltyRule: DeathPenaltyRule,
        val cameraRule: CameraRule,
        val scoreRule: ScoreRule
): History() {

    val mapName: String = map::class.simpleName ?: "Unknown"

    private val _chronicles: MutableList<Chronicle> = mutableListOf(Chronicle(map.nextChunk(), players))
    val chronicles: List<Chronicle>
        get() = _chronicles

    val playersDead: Set<PlayerData>
        get() = players.filter { it in chronicles.fold(mutableSetOf<PlayerData>()) {
                acc, history -> acc.apply { addAll(history.playersDead) }
        } }.toSet()

    private var _winner: PlayerData? = null
    val winner: PlayerData?
        get() = _winner

    private var _time: Duration = Duration.ZERO
    val time: Duration
        get() = _time

    private var _scores: Map<PlayerData, Int>? = null
    val scores: Map<PlayerData, Int>
        get() = _scores ?: players.associateWith { 0 }

    override val isEnded: Boolean
        get() = _scores != null


    fun newChronicle(chronicle: Chronicle) {
        if (!isEnded && hasStarted)
            _chronicles.add(chronicle)
    }

    fun end(winner: PlayerData?, scores: Map<PlayerData, Int>) {
        if (!isEnded && hasStarted) {
            val timeElapsed = startTime?.elapsedNow()
            if (timeElapsed != null)
                _time = timeElapsed
            _scores = scores
            _winner = winner
        }
    }

    override fun onStart() {
        chronicles.last().start()
    }
}


@OptIn(ExperimentalTime::class)
class Chronicle(val chunk: Chunk, val players: Set<PlayerData>): History() {

    private val _arrivals: MutableMap<PlayerData, Duration> = players.associateWith { Duration.INFINITE }.toMutableMap()
    val arrivals: Map<PlayerData, Duration>
        get() = _arrivals

    private val _playersDead: MutableSet<PlayerData> = mutableSetOf()
    val playersDead: Set<PlayerData>
        get() = _playersDead
    val playersRemaining: Set<PlayerData>
        get() = players.filterNot { it in playersDead }.toSet()


    override val isEnded: Boolean
        get() = players.all { it in playersDead || arrivals[it] != Duration.INFINITE }


    fun newArrival(player: PlayerData) {
        val startTime = startTime
        if (!isEnded && startTime != null && _arrivals[player] == Duration.INFINITE && player !in playersDead)
            _arrivals[player] = startTime.elapsedNow()
    }

    fun newDeath(player: PlayerData) {
        if (!isEnded && hasStarted)
            _playersDead.add(player)
    }

    override fun toString(): String {
        return """
            ChunkHistory(${players.map { it.character}})\n
            \tstatus: ${if (isEnded) "ENDED" else if (hasStarted) "STARTED" else "READY"}\n
            \tarrivals: $arrivals\n
            \tdeaths: $playersDead\n
        """.trimIndent()
    }
}