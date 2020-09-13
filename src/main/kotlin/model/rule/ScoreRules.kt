package model.rule

import controler.player.Player
import model.history.GameHistory
import kotlin.math.floor


typealias ScoreRule = (history: GameHistory, cameraNewX: Double, cameraOldX: Double, scores: MutableMap<Player, Int>) -> Unit

class ClassicScoreRule : ScoreRule {

    override fun invoke(history: GameHistory, cameraNewX: Double, cameraOldX: Double, scores: MutableMap<Player, Int>) {
        val firstPlayer = history.chronicles.last().playersRemaining.maxByOrNull { it.character.x }
        if (firstPlayer != null && cameraNewX > cameraOldX)
            scores[firstPlayer] = (scores[firstPlayer] ?: 0) + floor(cameraNewX).toInt() - floor(cameraOldX).toInt()
    }
}