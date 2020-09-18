package game.ui

import com.jme3.app.SimpleApplication
import core.game.Game
import javafx.application.Platform
import launcher.ui.FrameView
import launcher.ui.MenuView
import launcher.ui.ResultsView
import tornadofx.find


class ReadyPlayer42Game(private val game: Game) : SimpleApplication(InGameAppState(game)) {

    override fun simpleInitApp() {}

    override fun destroy() {
        super.destroy()
        Platform.runLater {
            find<FrameView>().content = ResultsView(game.history, MenuView()).root
        }
        game.cleanUp()
    }
}