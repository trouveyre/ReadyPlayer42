package model.rule

import controler.Player
import model.history.GameHistory


typealias DeathRule = (history: GameHistory) -> Set<Player>

class ClassicDeathRule(val fallLimit: Double = FALL_LIMIT_DEFAULT_VALUE): DeathRule {   //TODO better name ?

    override fun invoke(history: GameHistory): Set<Player> {    //TODO to complete
        val lastChronicle = history.chronicles.last()
        return lastChronicle.playersRemaining.filter { it.character.y < fallLimit }.onEach {
            lastChronicle.newDeath(it)
        }.toSet()
    }


    companion object {
        const val FALL_LIMIT_DEFAULT_VALUE: Double = -15.0
    }
}