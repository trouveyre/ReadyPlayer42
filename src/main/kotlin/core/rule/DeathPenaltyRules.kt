package core.rule

import core.game.Game
import core.player.PlayerData
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


typealias DeathPenaltyRule = (game: Game, newDeath: PlayerData?) -> Unit

class WithoutDeathPenaltyRule: DeathPenaltyRule {

    override fun invoke(game: Game, newDeath: PlayerData?) {}
}

@OptIn(ExperimentalTime::class)
class RandomKillDeathPenaltyRule: DeathPenaltyRule {

    override fun invoke(game: Game, newDeath: PlayerData?) {
        val lastChronicle = game.history.chronicles.last()
        if (newDeath != null) {
            val runners = lastChronicle.playersRemaining.filter { lastChronicle.arrivals[it] == Duration.INFINITE }
            if (runners.isNotEmpty())
                lastChronicle.newDeath(runners.random())
        }
    }
}