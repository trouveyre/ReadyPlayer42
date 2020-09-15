package launcher.ui

import core.history.GameHistory
import tornadofx.*


class ResultatView(val history: GameHistory) : View() {

    override val root = borderpane {
        center = button("BACK TO THE MENU") {
            action {
                find<FrameView>().root.center = find<MenuView>().root
            }
        }
    }
}
