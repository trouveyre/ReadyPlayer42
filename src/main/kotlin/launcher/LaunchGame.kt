package launcher

import com.jme3.system.AppSettings
import core.game.Game
import core.history.GameHistory
import game.ui.ReadyPlayer42Game
import java.awt.Toolkit


fun play(game: Game): GameHistory {
    ReadyPlayer42Game(game).apply {
        isShowSettings = false
        setSettings(AppSettings(true).apply {
            isFullscreen = true
            val size = Toolkit.getDefaultToolkit().screenSize
            width = size.width
            height = size.height
            frequency = 75
        })
    }.start()
    return game.history
}