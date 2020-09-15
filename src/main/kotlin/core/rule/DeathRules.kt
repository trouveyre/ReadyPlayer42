package core.rule

import core.game.Game
import core.player.PlayerData


typealias DeathRule = (game: Game) -> Set<PlayerData>

class LeftAndBottomBordersDeathRule(
        val latenessLimit: Double = LATENESS_LIMIT_DEFAULT_VALUE,
        val fallLimit: Double = FALL_LIMIT_DEFAULT_VALUE
): DeathRule {

    override fun invoke(game: Game): Set<PlayerData> {
        val lastChronicle = game.history.chronicles.last()
        return lastChronicle.playersRemaining.filter {
            game.cameraX - it.character.x > latenessLimit || it.character.y < fallLimit
        }.onEach {
            lastChronicle.newDeath(it)
        }.toSet()
    }


    companion object {
        const val LATENESS_LIMIT_DEFAULT_VALUE: Double = 55.0
        const val FALL_LIMIT_DEFAULT_VALUE: Double = -15.0
    }
}