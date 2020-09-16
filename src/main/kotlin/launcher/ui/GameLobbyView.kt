package launcher.ui

import core.game.LocalGame
import core.map.ChunkCollection
import core.map.RandomMap
import core.player.PlayerData
import core.rule.FirstScoreWinRule
import javafx.geometry.Pos
import javafx.scene.control.TableView
import launcher.lobby.Lobby
import launcher.lobby.LobbyObserver
import tornadofx.*


class GameLobbyView(val lobby: Lobby) : View(), LobbyObserver {

    private val playersView: TableView<PlayerData> = TableView<PlayerData>().apply {
        readonlyColumn("player", PlayerData::name)
        readonlyColumn("color", PlayerData::color).cellFormat {
            text = ""
            style {
                backgroundColor += c(it)
            }
        }
    }

    override val root = borderpane {
        center = hbox {
            add(playersView)
        }
        bottom = hbox {
            alignment = Pos.CENTER
            button("PLAY") {
                action {
                    val game = LocalGame(
                            RandomMap(ChunkCollection.values().map { it.chunk }),
                            lobby.leave(),
                            FirstScoreWinRule(150)
                    )
                    find<FrameView>().content = InGameView(game).root
                }
            }
        }
    }


    init {
        lobby.observer = this
    }


    override fun onPlayerChange(players: Set<PlayerData>) {
        playersView.items.setAll(players)
    }
}
