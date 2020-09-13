package model.rule

import controler.player.Player
import model.history.GameHistory


typealias WinRule = (history: GameHistory, scores: Map<Player, Int>) -> Player?

class FirstScoreWinRule(val winningScore: Int = WINNING_SCORE_DEFAULT_VALUE) : WinRule {

    override fun invoke(history: GameHistory, scores: Map<Player, Int>): Player? {
        val betterPlayer = history.chronicles.last().playersRemaining.maxByOrNull { scores[it] ?: 0 }
        return if (scores[betterPlayer] ?: 0 >= winningScore)
            betterPlayer
        else null
    }


    companion object {
        const val WINNING_SCORE_DEFAULT_VALUE: Int = 1000
    }
}