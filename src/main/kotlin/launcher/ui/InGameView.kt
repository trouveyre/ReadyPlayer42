package launcher.ui

import core.game.Game
import javafx.geometry.Pos
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import game.playFullscreen
import tornadofx.View
import tornadofx.label
import tornadofx.vbox

class InGameView(game: Game) : View() {

    override val root = vbox {
        alignment = Pos.CENTER
        label(MESSAGE_GAME_IS_RUNNING) {
            font = Font.font(font.family, FontWeight.BOLD, 50.0)
        }
    }


    init {
        playFullscreen(game)
        find<FrameView>().tip = "What a game !"
    }


    companion object {
        const val MESSAGE_GAME_IS_RUNNING: String = "You are in game !"
    }
}
