package core.rule

import core.history.GameHistory
import kotlin.math.max
import kotlin.math.min


typealias CameraRule = (history: GameHistory, cameraX: Double) -> Double

class FollowingFirstPlayerCameraRule(
        val delay: Double = DELAY_DEFAULT_VALUE,
        val margin: Double = MARGIN_DEFAULT_VALUE
): CameraRule {

    override fun invoke(history: GameHistory, cameraX: Double): Double {
        val lastChronicle = history.chronicles.last()
        val firstPlayer = lastChronicle.playersRemaining.maxByOrNull { it.character.x }
        return if (firstPlayer != null) {
            val biggerX = firstPlayer.character.x - delay
            min(max(biggerX, cameraX), lastChronicle.chunk.length - margin)
        }
        else 0.0
    }


    companion object {
        const val DELAY_DEFAULT_VALUE: Double = 20.0
        const val MARGIN_DEFAULT_VALUE: Double = 50.0
    }
}
