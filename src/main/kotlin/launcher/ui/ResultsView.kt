package launcher.ui

import core.history.GameHistory
import core.player.PlayerData
import javafx.geometry.Pos
import tornadofx.*


class ResultsView(val history: GameHistory, menu: View) : View() {

    private val scores = tableview<Biography> {
        readonlyColumn("color", Biography::player).cellFormat {
            text = ""
            style {
                backgroundColor += c(it.color)
            }
        }
        readonlyColumn("player", Biography::player).cellFormat {
            text = it.name
        }
        readonlyColumn("score", Biography::score)
    }

    override val root = borderpane {
        center = scores
        bottom = hbox {
            alignment = Pos.CENTER
            button("BACK TO THE MENU") {
                action {
                    find<FrameView>().content = menu.root
                }
            }
        }
    }


    init {
        val winner = history.winner
        find<FrameView>().tip = if (winner != null)
            "And the winner is... ${winner.name} ! (in ${history.time})"
        else
            "There is no winner this game :o"
        scores.items.setAll(history.players.map { Biography(it, history.scores[it] ?: 0) })
    }


    data class Biography(val player: PlayerData, val score: Int)
}
