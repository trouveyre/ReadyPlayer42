package model.rule

import controler.player.Player
import model.history.GameHistory
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


typealias DeathPenaltyRule = (history: GameHistory, newDeath: Player?) -> Unit

@OptIn(ExperimentalTime::class)
class RandomKillDeathPenaltyRule: DeathPenaltyRule {

    override fun invoke(history: GameHistory, newDeath: Player?) {
        val lastChronicle = history.chronicles.last()
        if (newDeath != null)
            lastChronicle.newDeath(
                    lastChronicle.playersRemaining.filter { lastChronicle.arrivals[it] == Duration.INFINITE }.random()
            )
    }
}