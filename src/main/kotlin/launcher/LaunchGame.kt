package launcher

import com.jme3.system.AppSettings
import core.game.Game
import core.history.GameHistory
import game.ui.ReadyPlayer42Game
import java.awt.Toolkit


fun playFullscreen(game: Game) {
    val app = ReadyPlayer42Game(game).apply {
        isShowSettings = false
        setSettings(AppSettings(true).apply {
            isFullscreen = false
            val size = Toolkit.getDefaultToolkit().screenSize
            width = size.width
            height = size.height
            frequency = 75
        })
    }
    app.start()
}