package core.rule

import core.game.Game
import core.player.PlayerData
import kotlin.math.floor


typealias ScoreRule = (game: Game, cameraOldX: Double, scores: MutableMap<PlayerData, Int>) -> Unit

class FirstPlayerScoreRule : ScoreRule {

    override fun invoke(game: Game, cameraOldX: Double, scores: MutableMap<PlayerData, Int>) {
        val firstPlayer = game.history.chronicles.last().playersRemaining.maxByOrNull { it.character.x }
//        val secondPlayer = game.history.chronicles.last().playersRemaining.filterNot { it == firstPlayer }.maxByOrNull { it.character.x }
        if (firstPlayer != null && game.cameraX > cameraOldX)
            scores[firstPlayer] = (scores[firstPlayer] ?: 0) + floor(game.cameraX).toInt() - floor(cameraOldX).toInt()
    }


    companion object {
        const val LEAD_MINIMUM: Double = 0.1
    }
}