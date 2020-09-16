package launcher.ui

import core.history.GameHistory
import tornadofx.View
import tornadofx.action
import tornadofx.borderpane
import tornadofx.button


class ResultsView(val history: GameHistory, menu: View) : View() {

    override val root = borderpane {
        center = button("BACK TO THE MENU") {
            action {
                find<FrameView>().content = menu.root
            }
        }
    }
}
