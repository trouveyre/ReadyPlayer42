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
import launcher.play
import tornadofx.*


class GameLobbyView(val lobby: Lobby) : View(), LobbyObserver {

    private val playersView: TableView<PlayerData> = TableView<PlayerData>().apply {
        readonlyColumn("player", PlayerData::name)
    }

    override val root = borderpane {
        center = hbox {
            add(playersView)
        }
        bottom = hbox {
            alignment = Pos.CENTER
            button("PLAY") {
                action {
                    find<FrameView>().apply {
                        root.center = find<InGameView>().root
                        tip = "What a game !"
                    }
                    val game = LocalGame(
                        RandomMap(ChunkCollection.values().map { it.chunk }),
                        lobby.leave(),
                        FirstScoreWinRule(150)
                    )
                    play(game)
                }
            }
        }
    }


    init {
        lobby.observer = this
    }


    override fun onPlayerChange(players: Set<PlayerData>) {
        println(players)
        playersView.items.setAll(players)
    }
}