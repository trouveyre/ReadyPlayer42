package launcher.ui

import core.character.Character
import core.game.LocalGame
import core.map.ChunkCollection
import core.map.RandomMap
import core.player.LocalPlayer
import core.player.PlayerData
import core.rule.FirstScoreWinRule
import javafx.geometry.Pos
import javafx.scene.control.TextField
import javafx.scene.control.ToggleButton
import launcher.lobby.ClientLobby
import launcher.lobby.ServerLobby
import launcher.play
import tornadofx.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.URL

class MenuView : View() {

    private val publicIp: String by lazy {
        BufferedReader(InputStreamReader(URL("http://bot.whatismyipaddress.com").openStream())).readLine().trim()
    }
    private val pseudoProvider = TextField(PlayerData.ManufacturedNames.random())
    private val hostSelector = ToggleButton("host the game")
    private val addressProvider = TextField().apply {
        editableProperty().bind(hostSelector.selectedProperty().not())
        textProperty().bind(hostSelector.selectedProperty().stringBinding { if (it == true) publicIp else "" })
    }

    override val root = hbox {
        alignment = Pos.CENTER
        spacing = 50.0
        vbox {
            alignment = Pos.CENTER_LEFT
            hbox {
                alignment = Pos.BASELINE_CENTER
                label("pseudo : ")
                add(pseudoProvider)
            }
            hbox {
                alignment = Pos.BASELINE_CENTER
                add(hostSelector)
            }
            hbox {
                alignment = Pos.BASELINE_CENTER
                label {
                    textProperty().bind(hostSelector.selectedProperty().stringBinding {
                        if (it == true)
                            "your address is "
                        else
                            "host's address : "
                    })
                }
                add(addressProvider)
            }
            hbox {
                alignment = Pos.BASELINE_CENTER
                button {
                    textProperty().bind(hostSelector.selectedProperty().stringBinding {
                        if (it == true) "HOST" else "JOIN"
                    })
                    action {
                        find<FrameView>().apply {
                            root.center = GameLobbyView(
                                if (hostSelector.isSelected)
                                    ServerLobby.apply {
                                        open(LocalPlayer(pseudoProvider.text, Character()))
                                    }
                                else
                                    ClientLobby.apply {
                                        join(addressProvider.text, LocalPlayer(pseudoProvider.text, Character()))
                                    }
                            ).root
                            tip = "Let's just have fun :)"
                        }
                    }
                }
            }
        }
        label(" or ")
        button("PLAY AT 2 ON THE SAME KEYBORD") {
            action {
                val map = RandomMap(ChunkCollection.values().map { it.chunk })
                val game = LocalGame(
                    map,
                    setOf(
                        LocalPlayer(PlayerData.ManufacturedNames.random(), Character()),
                        LocalPlayer(PlayerData.ManufacturedNames.random(), Character())
                    ),
                    FirstScoreWinRule(150)
                )
                find<FrameView>().apply {
                    root.center = find<InGameView>().root
                    tip = "What a game !"
                }
                play(game)
            }
        }
    }
}
