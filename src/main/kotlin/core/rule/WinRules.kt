package core.rule

import core.game.Game
import core.player.PlayerData
import core.history.GameHistory


typealias WinRule = (game: Game) -> PlayerData?

class FirstScoreWinRule(val winningScore: Int = WINNING_SCORE_DEFAULT_VALUE) : WinRule {

    override fun invoke(game: Game): PlayerData? {
        val betterPlayer = game.history.chronicles.last().playersRemaining.maxByOrNull { game.scores[it] ?: 0 }
        return if (game.scores[betterPlayer] ?: 0 >= winningScore)
            betterPlayer
        else null
    }


    companion object {
        const val WINNING_SCORE_DEFAULT_VALUE: Int = 150
    }
}