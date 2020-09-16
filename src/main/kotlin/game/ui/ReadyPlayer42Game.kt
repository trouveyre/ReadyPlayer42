package game.ui

import com.jme3.app.SimpleApplication
import com.jme3.input.KeyInput
import core.character.Character
import core.game.Game
import core.game.LocalGame
import core.map.Chunk
import core.map.ChunkCollection
import core.map.RandomMap
import core.player.LocalPlayer
import core.player.PlayerData
import core.rule.FirstScoreWinRule
import javafx.application.Platform
import launcher.ui.FrameView
import launcher.ui.MenuView
import launcher.ui.ResultsView
import tornadofx.find
import tornadofx.launch


class ReadyPlayer42Game(private val game: Game) : SimpleApplication(InGameAppState(game)) {

    override fun simpleInitApp() {}

    override fun destroy() {
        super.destroy()
        Platform.runLater {
            find<FrameView>().content = ResultsView(game.history, MenuView()).root
        }
        game.cleanUp()
    }


    companion object {
        const val LOCAL_PLAYERS_MAXIMUM_NUMBER: Int = 2
    }
}


fun main(vararg args: String) {

    val map = RandomMap(ChunkCollection.values().map { it.chunk })
    val game = LocalGame(
        map,
        setOf(
            LocalPlayer(PlayerData.ManufacturedNames.random(), Character()),
            LocalPlayer(PlayerData.ManufacturedNames.random(), Character(),
                KeyInput.KEY_RIGHT, KeyInput.KEY_LEFT, KeyInput.KEY_UP, KeyInput.KEY_DOWN)
        )
    )
    ReadyPlayer42Game(game).start()
}