package launcher.ui

import javafx.geometry.Pos
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import tornadofx.*

class InGameView : View() {

    override val root = vbox {
        alignment = Pos.CENTER
        label(MESSAGE_GAME_IS_RUNNING) {
            font = Font.font(font.family, FontWeight.BOLD, 50.0)
        }
    }


    companion object {
        const val MESSAGE_GAME_IS_RUNNING: String = "You are in game !"
    }
}
